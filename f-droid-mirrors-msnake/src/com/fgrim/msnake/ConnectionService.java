package com.fgrim.msnake;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;

public class ConnectionService extends IntentService implements
		BluetoothAdapter.LeScanCallback {
	// variables declaration

	private static final String TAG = "BluetoothGattActivity";

	private static final String DEVICE_NAME = "SensorTag";
	TestGesture t = new TestGesture();

	/* Acceleromter configuration servcie */
	private static final UUID ACCELEROMETER_SERVICE = UUID
			.fromString("f000aa10-0451-4000-b000-000000000000");
	private static final UUID ACCELEROMETER_DATA_CHAR = UUID
			.fromString("f000aa11-0451-4000-b000-000000000000");
	private static final UUID ACCELEROMETER_CONFIG_CHAR = UUID
			.fromString("f000aa12-0451-4000-b000-000000000000");
	private static final UUID ACCELEROMETER_PERIOD_CHAR = UUID
			.fromString("f000aa13-0451-4000-b000-000000000000");

	/* Client Configuration Descriptor */
	private static final UUID CONFIG_DESCRIPTOR = UUID
			.fromString("00002902-0000-1000-8000-00805f9b34fb");

	private BluetoothAdapter mBluetoothAdapter;
	private SparseArray<BluetoothDevice> mDevices;

	private BluetoothGatt mConnectedGatt;

	// variable declaration end

	public ConnectionService() {
		super("ConnectionService");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
		mBluetoothAdapter = manager.getAdapter();
		mDevices = new SparseArray<BluetoothDevice>();
		/*
		 * A progress dialog will be needed while the connection process is
		 * taking place
		 */

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.i("ok", "hanldeIntent");
		startScan();

	}

	private void broadCast() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("com.quchen.flappycow");
		intent.putExtra("data", "3");
		sendBroadcast(intent);
	}

	public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
		// TODO Auto-generated method stub
		Log.i("scan", "in onlescan method");
		Log.i(TAG, "New LE Device: " + device.getName() + " @ " + rssi);
		/*
		 * We are looking for SensorTag devices only, so validate the name that
		 * each device reports before adding it to our collection
		 */
		if (DEVICE_NAME.equals(device.getName())) {

			mDevices.put(device.hashCode(), device);
			mConnectedGatt = device.connectGatt(this, false, mGattCallback);
			mBluetoothAdapter.stopLeScan(this);
			t.train();
			// Update the overflow menu
			// invalidateOptionsMenu();
		}
	}

	private void startScan() {
		Log.i("start", "scan");
		if (mBluetoothAdapter.startLeScan(this)) {
			Log.i("scan", "started");
		} else {
			Log.i("scan", "not started");
		}

	}

	/*
	 * In this callback, we've created a bit of a state machine to enforce that
	 * only one characteristic be read or written at a time until all of our
	 * sensors are enabled and we are registered to get notifications.
	 */
	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		/* State Machine Tracking */
		private int mState = 0;

		private void reset() {
			mState = 0;
		}

		private void advance() {
			mState++;
		}

		/*
		 * Send an enable command to each sensor by writing a configuration
		 * characteristic. This is specific to the SensorTag to keep power low
		 * by disabling sensors you aren't using.
		 */
		private void enableNextSensor(BluetoothGatt gatt) {
			Log.i("sensor", "enable");
			BluetoothGattCharacteristic characteristic;
			switch (mState) {
			case 0:
				Log.d(TAG, "Enabling accelerometer");
				characteristic = gatt.getService(ACCELEROMETER_SERVICE)
						.getCharacteristic(ACCELEROMETER_CONFIG_CHAR);
				characteristic.setValue(new byte[] { 0x01 });
				break;
			case 1:
				Log.d(TAG, "Enabling accelerometer");
				characteristic = gatt.getService(ACCELEROMETER_SERVICE)
						.getCharacteristic(ACCELEROMETER_PERIOD_CHAR);
				characteristic.setValue(new byte[] { (byte) 10 });
				break;

			default:
				Log.i(TAG, "All Sensors Enabled 1");
				return;
			}

			gatt.writeCharacteristic(characteristic);
		}

		private void readNextSensor(BluetoothGatt gatt) {
			BluetoothGattCharacteristic characteristic;
			switch (mState) {
			case 0:
				Log.d(TAG, "Reading accelerometer");
				characteristic = gatt.getService(ACCELEROMETER_SERVICE)
						.getCharacteristic(ACCELEROMETER_DATA_CHAR);
				break;
			default:
				Log.i(TAG, "All Sensors Enabled 2");
				return;
			}

			gatt.readCharacteristic(characteristic);
		}

		/*
		 * * Enable notification of changes on the data characteristic for each
		 * sensor by writing the ENABLE_NOTIFICATION_VALUE flag to that
		 * characteristic's configuration descriptor.
		 */
		private void setNotifyNextSensor(BluetoothGatt gatt) {
			BluetoothGattCharacteristic characteristic;
			switch (mState) {
			case 0:
				Log.d(TAG, "Set notify accelerometer");
				characteristic = gatt.getService(ACCELEROMETER_SERVICE)
						.getCharacteristic(ACCELEROMETER_DATA_CHAR);
				break;
			default:
				// mHandler.sendEmptyMessage(MSG_DISMISS);
				Log.i(TAG, "All Sensors Enabled 3");
				return;
			}

			// Enable local notifications
			gatt.setCharacteristicNotification(characteristic, true);
			// Enabled remote notifications
			BluetoothGattDescriptor desc = characteristic
					.getDescriptor(CONFIG_DESCRIPTOR);
			desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			gatt.writeDescriptor(desc);
		}

		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			Log.d(TAG, "Connection State Change: " + status + " -> "
					+ connectionState(newState));
			if (status == BluetoothGatt.GATT_SUCCESS
					&& newState == BluetoothProfile.STATE_CONNECTED) {
				/*
				 * Once successfully connected, we must next discover all the
				 * services on the device before we can read and write their
				 * characteristics.
				 */
				gatt.discoverServices();
			} else if (status == BluetoothGatt.GATT_SUCCESS
					&& newState == BluetoothProfile.STATE_DISCONNECTED) {
				/*
				 * If at any point we disconnect, send a message to clear the
				 * weather values out of the UI
				 */
			} else if (status != BluetoothGatt.GATT_SUCCESS) {
				/*
				 * If there is a failure at any stage, simply disconnect
				 */
				gatt.disconnect();
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			Log.d(TAG, "Services Discovered: " + status);
			/*
			 * With services discovered, we are going to reset our state machine
			 * and start working through the sensors we need to enable
			 */
			reset();
			enableNextSensor(gatt);
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// For each read, pass the data up to the UI thread to update the
			// display
			if (ACCELEROMETER_DATA_CHAR.equals(characteristic.getUuid())) {
				Log.i("sensor", "accelerometer");
			}
			// After reading the initial value, next we enable notifications
			setNotifyNextSensor(gatt);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// After writing the enable flag, next we read the initial value
			readNextSensor(gatt);
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			/*
			 * After notifications are enabled, all updates from the device on
			 * characteristic value changes will be posted here. Similar to
			 * read, we hand these up to the UI thread to update the display.
			 */
			if (ACCELEROMETER_DATA_CHAR.equals(characteristic.getUuid())) {
				// mHandler.sendMessage(Message.obtain(null, MSG_ACCELEROMETER,
				// characteristic));
				// Log.i("sensor", "acceleromter changed");
				updateAccelerometerCals(characteristic);
			}

		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			// Once notifications are enabled, we move to the next sensor and
			// start over with enable
			advance();
			enableNextSensor(gatt);
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			Log.d(TAG, "Remote RSSI: " + rssi);
		}

		private String connectionState(int status) {
			switch (status) {
			case BluetoothProfile.STATE_CONNECTED:
				return "Connected";
			case BluetoothProfile.STATE_DISCONNECTED:
				return "Disconnected";
			case BluetoothProfile.STATE_CONNECTING:
				return "Connecting";
			case BluetoothProfile.STATE_DISCONNECTING:
				return "Disconnecting";
			default:
				return String.valueOf(status);
			}
		}
	};

	double x1, y1, z1, d = 0.0f, norm;
	Boolean trigger = false;
	ArrayList<String> dataPoints = new ArrayList<String>();

	private void updateAccelerometerCals(
			BluetoothGattCharacteristic characteristic) {
		Float[] values = SensorTagData.extractAccelerometerReading(
				characteristic, 0);
		double x, y, z;
		x = values[0];
		y = values[1];
		z = values[2];

		d = Math.sqrt(Math.pow((x - x1), 2) + Math.pow((y - y1), 2)
				+ Math.pow((z - z1), 2));
		if (d >= 0.3 && !trigger) {
			Log.i("start", "start");
			trigger = true;
		} else if (d <= 0.1 && trigger) {
			Log.i("end", "end");
			trigger = false;
			try {

				File returnType = whichGesture(dataPoints);
				if (dataPoints.size() > 6) {
					String string = t.test(returnType);
					if (string == "fly") {
						sendPatternTrigger("fly");
					} else if (string == "stomp") {
						sendPatternTrigger("stomp");
					} else if (string == "left") {
						sendPatternTrigger("left");
					} else if (string == "right") {
						sendPatternTrigger("right");
					} else if (string == "back") {
						sendPatternTrigger("back");
					}
				}

			} catch (Exception e) {
				Log.i("error", "test failing");
			}
			dataPoints.clear();
		}
		if (trigger) {
			dataPoints.add("[ " + x + " " + y + " " + z + " ] ;");
		}
		// update previous values
		x1 = x;
		y1 = y;
		z1 = z;

	}

	private void sendPatternTrigger(String string) {
		Intent intent = new Intent("myproject");
		intent.putExtra("data", string);
		sendBroadcast(intent);
	}

	Boolean gesture = false;
	StringBuffer buffer = new StringBuffer();

	private File whichGesture(ArrayList<String> datapointsList)
			throws Exception {

		File outputDir = getApplicationContext().getCacheDir(); // context being
																// the Activity
																// pointer
		File outputFile = File.createTempFile("learn", ".seq", outputDir);
		if (outputFile.exists()) {
			outputFile.delete();
			outputFile = File.createTempFile("learn", ".seq", outputDir);
		}
		// code to work with list of values
		List<String> dataPoints = datapointsList;
		for (int i = 0; i < dataPoints.size(); i++) {
			buffer.append(datapointsList.get(i));
		}
		buffer.append(System.getProperty("line.separator"));

		FileWriter writer = new FileWriter(outputFile);
		writer.write(buffer.toString());
		buffer.delete(0, buffer.length());
		writer.close();
		return outputFile;
	}

}
