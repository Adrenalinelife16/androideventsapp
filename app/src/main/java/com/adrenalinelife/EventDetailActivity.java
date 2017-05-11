package com.adrenalinelife;

import android.os.Bundle;
import android.view.MenuItem;

import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.model.Event;
import com.adrenalinelife.ui.EventDetail;
import com.adrenalinelife.utils.Const;

public class EventDetailActivity extends CustomActivity
{
	/* (non-Javadoc)
	I'm adding this code for Push Testing 2
	 * @see com.food.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getActionBar().setTitle(
				((Event) getIntent().getSerializableExtra(Const.EXTRA_DATA))
						.getTitle());

		addFragment();
	}

	/**
	 * Attach the appropriate fragment with this activity.
	 */
	private void addFragment()
	{
		EventDetail ed = new EventDetail();
		Bundle b = new Bundle();
		b.putSerializable(Const.EXTRA_DATA,
				getIntent().getSerializableExtra(Const.EXTRA_DATA));
		ed.setArg(b);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, ed).commit();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
