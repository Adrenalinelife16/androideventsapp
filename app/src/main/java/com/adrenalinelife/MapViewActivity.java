package com.adrenalinelife;

import android.os.Bundle;

import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.ui.MapViewer;

/**
 * The MapViewActivity is the activity class that shows Map fragment. This
 * activity is only created to show Back button on ActionBar.
 */
public class MapViewActivity extends CustomActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setTitle("Maps");

		addFragment();
	}

	/**
	 * Attach the appropriate MapViewer fragment with activity.
	 */
	private void addFragment()
	{
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, new MapViewer()).commit();
	}
}
