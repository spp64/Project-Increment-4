package com.fgrim.msnake;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.fgrim.msnake.Database.Location;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {

	private GoogleMap googleMap;
	Database db = new Database(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		try {
			// Loading map
			initilizeMap();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	private void initilizeMap() {

		ArrayList<Location> mapCoordinates = (ArrayList<Location>) db
				.getLocation();
		int size = mapCoordinates.size();

		// latitude and longitude

		double latitude1 = 39.030408;
		double longitude1 = -94.57443490000003;

		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			// create marker
			/*for (int i = 0; i < size; i++) {
				Location coordinates = mapCoordinates.get(i);
				double latitude = coordinates.lat;
				double longitude = coordinates.longi;
				MarkerOptions marker = new MarkerOptions().position(
						new LatLng(latitude, longitude)).title("Played here!");

				// adding marker
				googleMap.addMarker(marker);
			}*/
			

			MarkerOptions marker = new MarkerOptions().position(
					new LatLng(latitude1, longitude1)).title("Played here!");

			// adding marker
			googleMap.addMarker(marker);

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
