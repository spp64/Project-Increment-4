package com.fgrim.msnake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class GraphActivity extends Activity {

	private Button gamePie;
	private Button allPie;
	private Button map;
	private Button barGraph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_graph);
		gamePie = (Button) findViewById(R.id.gamePieChart);
		allPie = (Button) findViewById(R.id.totalPie);
		map = (Button) findViewById(R.id.maps);
		barGraph = (Button) findViewById(R.id.barGraph);

		gamePie.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(GraphActivity.this, GamePieChart.class);
				startActivity(i);

			}
		});

		allPie.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(GraphActivity.this, TotalPieActivity.class);
				startActivity(i);

			}
		});

		map.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(GraphActivity.this, MapActivity.class);
				startActivity(i);

			}
		});
		
		barGraph.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(GraphActivity.this, BarGraphActivity.class);
				startActivity(i);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.graph, menu);
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
