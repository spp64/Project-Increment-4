package com.fgrim.msnake;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import com.fgrim.msnake.Database.Count;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GamePieChart extends Activity {

	Database db = new Database(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_pie_chart);

		openChart();
	}

	private void openChart() {

		// Pie Chart Section Names
		String[] code = new String[] { "Left Gesture", "Right Gesture",
				"Up Gesture", "Down Gesture", "Back Gesture" };
		
		
		//Draws pie chart with the data saved from the last played game.
		ArrayList<Count> values = (ArrayList<Count>) db.getData();
		int size = values.size();
		Count count = values.get(size-1);
		int countLeft = count.left;
		int countRight = count.right;
		int countUp = count.up;
		int countDown = count.down;
		int countBack = count.back;


		// Pie Chart Section Value
		double[] distribution = { countLeft, countRight, countUp, countDown, countBack };
		

		// Color of each Pie Chart Sections
		int[] colors = { Color.BLUE, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.YELLOW};

		// Instantiating CategorySeries to plot Pie Chart
		CategorySeries distributionSeries = new CategorySeries("Game Pie Chart");

		for (int i = 0; i < distribution.length; i++) {
			// Adding a slice with its values and name to the Pie Chart
			distributionSeries.add(code[i], distribution[i]);
		}
		

		// Instantiating a renderer for the Pie Chart
		DefaultRenderer defaultRenderer = new DefaultRenderer();

		for (int i = 0; i < distribution.length; i++) {
			SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
			seriesRenderer.setColor(colors[i]);
			seriesRenderer.setDisplayChartValues(true);
			// Adding a renderer for a slice
			defaultRenderer.addSeriesRenderer(seriesRenderer);
		}
/*		for (int i = 0; i < distribution1.length; i++) {
			SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
			seriesRenderer.setColor(colors[i]);
			seriesRenderer.setDisplayChartValues(true);
			// Adding a renderer for a slice
			defaultRenderer1.addSeriesRenderer(seriesRenderer);
		}*/

		defaultRenderer.setChartTitle("Game Pie Chart");
		defaultRenderer.setChartTitleTextSize(30);
		defaultRenderer.setZoomButtonsVisible(true);
		/*defaultRenderer1.setChartTitle("All Games Pie Chart");
		defaultRenderer1.setChartTitleTextSize(20);
		defaultRenderer1.setZoomButtonsVisible(true);*/

		// Creating an intent to plot bar chart using dataset and
		// multipleRenderer
		Intent intent = ChartFactory
				.getPieChartIntent(getBaseContext(), distributionSeries,
						defaultRenderer, "AChartEnginePieChartDemo");

		// Start Activity
		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_pie_chart, menu);
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
