package com.fgrim.msnake;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {

	private Button game;
	private Button chart;
	
	Database db = new Database(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		game = (Button) findViewById(R.id.game);
		chart = (Button) findViewById(R.id.chart);
		
		
		/*if(checkDataBase() == true){
			ArrayList<Count> values = (ArrayList<Count>) db.getData();
			int size = values.size();
			int totalLeft = 0;
			int totalRight = 0;
			int totalUp = 0;
			int totalDown = 0;
			int totalBack = 0;
			Count count2 = values.get(size-1);
			int totalCount = count2.count;
			for (int i = 0; i < size; i++) {
				Count count1 = values.get(i);
				totalLeft += count1.left;
				totalRight += count1.right;
				totalUp += count1.up;
				totalDown += count1.down;
				totalBack += count1.back;
			}
			StringBuilder sb = new StringBuilder();
			sb.append("Total Gestures Done : ").append(totalCount).append("\n").append("Total Left Gestures : ").append(totalLeft).append("\n").append("Total Right Gestures : ").append(totalRight).append("\n").append("Total Up Gestures : ").append(totalUp).append("\n").append("Total Down Gestures : ").append(totalDown).append("\n").append("Total Back Gestures : ").append(totalBack);
			String string = sb.toString();
			textView.setText(string);
		}*/
		

		game.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(HomeActivity.this, MSnake.class);
				startActivity(i);
			}
		});

		chart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(HomeActivity.this, GraphActivity.class);
				startActivity(i);
			}
		});
	}
	
	private boolean checkDataBase() {
	    SQLiteDatabase checkDB = null;
	    try {
	        checkDB = SQLiteDatabase.openDatabase("msnake.db", null,
	                SQLiteDatabase.OPEN_READONLY);
	        checkDB.close();
	    } catch (SQLiteException e) {
	        // database doesn't exist yet.
	    }
	    return checkDB != null ? true : false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
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
