package com.fgrim.msnake;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.fgrim.msnake.Database.Time;

public class BarGraphActivity extends Activity {

	Database db = new Database(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bar_graph);
		openChart();
	}

	private void openChart() {
		// int[] x = { 0, 1, 2, 3, 4, 5, 6, 7 };

		// int[] expense = { 2200, 2700, 2900, 2800, 2600, 3000, 3300, 3400 };

		List<Time> time = (ArrayList<Time>) db.getTime();
		int size = time.size();
		float[] playedTime = new float[size];
		String[] date1 = new String[size];

		for (int i = 0; i < size; i++) {
			Time data = time.get(i);
			String date = data.time;
			float playTime = data.playTime / 1000;
			playedTime[i] = playTime;
			date1[i] = date;

		}

		// Creating an XYSeries for Income
		XYSeries timePlayedSeries = new XYSeries("TimePlayed");

		// Adding data to Income and Expense Series
		for (int i = 0; i < size; i++) {
			timePlayedSeries.add(i, playedTime[i]);

		}

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(timePlayedSeries);

		// Creating XYSeriesRenderer to customize incomeSeries
		XYSeriesRenderer timeRenderer = new XYSeriesRenderer();
		timeRenderer.setColor(Color.rgb(130, 130, 230));
		timeRenderer.setFillPoints(true);
		timeRenderer.setLineWidth(2);
		timeRenderer.setDisplayChartValues(true);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Time vs Time Played");
		multiRenderer.setXTitle("Time");
		multiRenderer.setYTitle("Time in Seconds");
		multiRenderer.setBarSpacing(.5);
		multiRenderer.setAxisTitleTextSize(16);
		multiRenderer.setChartTitleTextSize(20);
		multiRenderer.setLabelsTextSize(15);
		multiRenderer.setLegendTextSize(15);
		multiRenderer.setXAxisMin(0);

		multiRenderer.setXAxisMax(5);
		multiRenderer.setZoomButtonsVisible(true);
		for (int i = 0; i < size; i++) {
			multiRenderer.addXTextLabel(i, date1[i]);
		}

		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to
		// multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(timeRenderer);

		// Creating an intent to plot bar chart using dataset and
		// multipleRenderer
		Intent intent = ChartFactory.getBarChartIntent(getBaseContext(),
				dataset, multiRenderer, Type.DEFAULT);

		// Start Activity
		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bar_graph, menu);
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
