package com.fgrim.msnake;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	private static final String CHART = "CHART";
	private static final String COL_LEFT = "LEFT";
	private static final String COL_RIGHT = "RIGHT";
	private static final String COL_UP = "UP";
	private static final String COL_DOWN = "DOWN";
	private static final String COL_BACK = "BACK";
	
	private static final String COL_COUNT = "COUNT";
	private static final String TIME = "TIME";
	private static final String DATE = "DATE";
	private static final String SECONDS = "PLAY_TIME";
	private static final String GPS = "GPS";
	private static final String LAT = "LATITUDE";
	private static final String LONG = "LONGITUDE";

	public Database(Context context) {
		super(context, "msnake.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = String
				.format("create table %s (_id INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL)",
						CHART, COL_LEFT, COL_RIGHT, COL_UP, COL_DOWN, COL_BACK, COL_COUNT);
		String sql2 = String
				.format("create table %s (_id INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s INTEGER NOT NULL)",
						TIME, DATE, SECONDS);
		String sql3 = String.format("create table %s (_id INTEGER PRIMARY KEY, %s REAL NOT NULL, %s REAL NOT NULL)", GPS, LAT, LONG);
		db.execSQL(sql);
		db.execSQL(sql2);
		db.execSQL(sql3);
		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void storeData(int left, int right, int up, int down, int back, int total) {
		SQLiteDatabase db = getReadableDatabase();

		ContentValues values = new ContentValues();

		values.put(COL_LEFT, left);
		values.put(COL_RIGHT, right);
		values.put(COL_UP, up);
		values.put(COL_DOWN, down);
		values.put(COL_BACK, back);
		values.put(COL_COUNT, total);

		db.insert(CHART, null, values);

		db.close();

	}

	public List<Count> getData() {
		List<Count> values = new ArrayList<Count>();
		SQLiteDatabase db = getWritableDatabase();

		String sql = String.format(
				"SELECT %s, %s, %s, %s, %s, %s FROM %s ORDER BY _id", COL_LEFT,
				COL_RIGHT, COL_UP, COL_DOWN, COL_BACK, COL_COUNT, CHART);
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			
				int left = cursor.getInt(0);
				int right = cursor.getInt(1);
				int up = cursor.getInt(2);
				int down = cursor.getInt(3);
				int back = cursor.getInt(4);
				int total = cursor.getInt(5);
				Count count = new Count(left, right, up, down, back, total);
				values.add(count);

			
		}

		
		db.close();
		return values;
	}
	
	public void storePlayTime(String date, long playTime){
		SQLiteDatabase db = getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(DATE,date);
		values.put(SECONDS, playTime);
		
		db.insert(TIME, null, values);
		
		db.close();
	}
	
	public void storeLocation(double latitude, double longitude){
		SQLiteDatabase db = getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put(LAT,latitude);
		values.put(LONG, longitude);
		
		db.insert(TIME, null, values);
		
		db.close();
	}
	
	public List<Location> getLocation(){
		List<Location> values = new ArrayList<Location>();
		SQLiteDatabase db = getWritableDatabase();

		String sql = String.format(
				"SELECT %s, %s FROM %s ORDER BY _id", LAT
				, LONG, GPS);
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			
				double latitude = cursor.getDouble(0);
				double longitude = cursor.getDouble(1);
				Location arr = new Location(latitude, longitude);
				if(!values.contains(arr))
				values.add(arr);

		}

		
		db.close();
		return values;
	}
	
	public List<Time> getTime(){
		List<Time> values = new ArrayList<Time>();
		SQLiteDatabase db = getWritableDatabase();
		
		String sql = String.format("SELECT %s, %s FROM %s ORDER BY _id", DATE, SECONDS, TIME);
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			
				String date = cursor.getString(0);
				long timePlayed  = cursor.getLong(1);
				Time arr = new Time(date, timePlayed);
				if(!values.contains(arr))
				values.add(arr);

		}

		
		db.close();
		return values;
		
	}
	
	
	
	class Location{
		double lat;
		double longi;
		public Location(double lat,double log){
			this.lat =lat;
			this.longi = log;
		}
		@Override
		public boolean equals(Object o) {
		
			if(o !=null && o instanceof Location){
				Location lol = (Location)o;
				if(this.lat == lol.lat && this.longi == lol.longi ){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
			
			
		}
		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return super.hashCode();
		}
		
	}
	
	class Time{
		String time;
		long playTime;
		public Time(String time, long playTime){
			this.time = time;
			this.playTime = playTime;
			
		}
	}
	
	class Count{
		int left;
		int right;
		int up;
		int down;
		int back;
		int count;
		public Count(int left, int right, int up, int down, int back, int count){
			this.left = left;
			this.right = right;
			this.up = up;
			this.down = down;
			this.back = back;
			this.count = count;
			
		}
	}

}
