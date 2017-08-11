package com.adrenalinelife.custom;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.adrenalinelife.R;
import com.adrenalinelife.utils.ExceptionHandler;
import com.adrenalinelife.utils.ImageLoader;
import com.adrenalinelife.utils.ImgTouchEffect;
import com.adrenalinelife.utils.StaticData;
import com.adrenalinelife.utils.TouchEffect;

/**
 * This is a common activity that all other activities of the app can extend to
 * inherit the common behaviors like implementing a common interface that can be
 * used in all child activities.
 */
public class CustomActivity extends FragmentActivity implements OnClickListener
{

	/**
	 * Apply this Constant as touch listener for views to provide alpha touch
	 * effect. The view must have a Non-Transparent background.
	 */
	public static final TouchEffect TOUCH = new TouchEffect();

	/** The Constant iTOUCH. */
	public static final ImgTouchEffect iTOUCH = new ImgTouchEffect();

	/** The loader. */
	protected ImageLoader loader;

	/** The this. */
	public CustomActivity THIS;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		StaticData.init(this);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		THIS = this;

		setupActionBar();
	}

	/**
	 * This method will setup the top title bar (Action bar) content and display
	 * values. It will also setup the custom background theme for ActionBar. You
	 * can override this method to change the behavior of ActionBar for
	 * particular Activity
	 */
	protected void setupActionBar()
	{
		final ActionBar actionBar = getActionBar();
		if (actionBar == null)
			return;
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setLogo(null);
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bg_6));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(R.string.app_name);
	}

	/**
	 * Sets the touch and click listener for a view with given id.
	 *
	 * @param id
	 *            the id
	 * @return the view on which listeners applied
	 */
	public View setTouchNClick(int id)
	{

		View v = setClick(id);
		if (v != null)
			v.setOnTouchListener(TOUCH);
		return v;
	}

	/*
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		new AlertDialog.Builder(this)
				.setMessage("Are you sure you want to exit?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
							Toast.makeText(getParent(),"Popping Back Stack",Toast.LENGTH_SHORT).show();
							getSupportFragmentManager().popBackStack();
						} else
							Toast.makeText(getParent(),"Nothing To Pop Back To",Toast.LENGTH_SHORT).show();
						//CustomActivity.super.onBackPressed();
					}
				})
				.setNegativeButton("No", null)
				.show();

	}

*/


	/**
	 * Sets the click listener for a view with given id.
	 *
	 * @param id
	 *            the id
	 * @return the view on which listener is applied
	 */
	public View setClick(int id)
	{

		View v = findViewById(id);
		if (v != null)
			v.setOnClickListener(this);
		return v;
	}

	@Override
	public void onClick(View v)
	{

	}
	/**
	 * Show progress dia.
	 *
	 * @param msg the msg
	 * @return the progress dialog
	 */
	public ProgressDialog showProgressDia(int msg)
	{
		return ProgressDialog.show(THIS, null, getString(msg));
	}

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
