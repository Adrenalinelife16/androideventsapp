package com.adrenalinelife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.utils.Const;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;

import io.fabric.sdk.android.Fabric;

/**
 * The Class MoreDetail is the Activity class that is launched when the user
 * clicks on any options in More screen and this will simply show TextView like
 * for Help Center, it will show Help center text.
 */
public class MoreDetail extends CustomActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		/** Fabric Initializing **/
		Fabric.with(this, new Answers());
		Fabric.with(this, new Crashlytics());
		final Fabric fabric = new Fabric.Builder(this)
				.kits(new Crashlytics())
				.debuggable(true)
				.build();
		Fabric.with(fabric);

		getActionBar().setTitle(
				getIntent().getIntExtra(Const.EXTRA_DATA1, R.string.app_name));

		((TextView) findViewById(R.id.lbl)).setText(getIntent().getIntExtra(
				Const.EXTRA_DATA, 0));

	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/html");
		i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.help_email_subject));
		i.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "adrenalinelife16@gmail.com" });
		startActivity(Intent.createChooser(i, getString(R.string.send_email)));
	}
}
