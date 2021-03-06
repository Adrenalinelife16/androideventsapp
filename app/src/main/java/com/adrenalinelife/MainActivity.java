package com.adrenalinelife;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.adrenalinelife.calendar.CalendarView;
import com.adrenalinelife.create_event.editCreateEvent;
import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.custom.CustomFragment;
import com.adrenalinelife.model.Data;
import com.adrenalinelife.ui.DiscoverEvents;
import com.adrenalinelife.ui.FavEvents;
import com.adrenalinelife.ui.FeedList;
import com.adrenalinelife.ui.LeftNavAdapter;
import com.adrenalinelife.ui.More;
import com.adrenalinelife.ui.Events;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.StaticData;

import io.fabric.sdk.android.Fabric;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.onesignal.OneSignal;

import java.util.ArrayList;

/**
 * The Class MainActivity is the base activity class of the application. This
 * activity is launched after the Splash and it holds all the Fragments used in
 * the app. It also creates the Navigation Drawer on left side.
 */
public class MainActivity extends CustomActivity {
	public String TAG = "initial";

	/** The drawer layout. */
	private DrawerLayout drawerLayout;

	/** ListView for left side drawer. */
	private ListView drawerLeft;

	/** The drawer toggle. */
	private ActionBarDrawerToggle drawerToggle;

	/** The left navigation list adapter. */
	private LeftNavAdapter adapter;

	/** The tab. */
	private View tab;
	private int PERMISSIONS_REQUEST_CODE = 11;

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/** Fabric Initializing **/
		Fabric.with(this, new Answers());
		Fabric.with(this, new Crashlytics());
		final Fabric fabric = new Fabric.Builder(this)
				.kits(new Crashlytics())
				.debuggable(true)
				.build();
		Fabric.with(fabric);

		/** Recieve Notifications **/
		OneSignal.startInit(this)
				.inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
				.unsubscribeWhenNotificationsAreDisabled(true)
				.init();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
            }
        }
		setupContainer();
		setupDrawer();
	}
	//

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == PERMISSIONS_REQUEST_CODE) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				//Granted
				StyleableToast.makeText(this, "Location Granted", Toast.LENGTH_LONG, R.style.ToastGranted).show();
            } else {
				//Denied
				StyleableToast.makeText(this, "Location Denied", Toast.LENGTH_LONG, R.style.ToastDenied).show();
			}
		}
	}

	//
	/**
	 * Setup the drawer layout. This method also includes the method calls for
	 * setting up the Left side drawer.
	 */
	public void setupDrawer()
	{
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view)
			{
				setActionBarTitle();
			}

			@Override
			public void onDrawerOpened(View drawerView)
			{
				getActionBar().setTitle(R.string.app_name);
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		drawerLayout.closeDrawers();

		setupLeftNavDrawer();
	}

	/**
	 * Setup the left navigation drawer/slider. You can add your logic to load
	 * the contents to be displayed on the left side drawer. You can also setup
	 * the Header and Footer contents of left drawer if you need them.
	 */
	private void setupLeftNavDrawer()
	{
		drawerLeft = (ListView) findViewById(R.id.left_drawer);

		adapter = new LeftNavAdapter(this, getDummyLeftNavItems());
		drawerLeft.setAdapter(adapter);
		drawerLeft.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3)
			{
				drawerLayout.closeDrawers();
				launchFragment(pos);
			}
		});
	}
	/**
	 * This method returns a list of dummy items for left navigation slider. You
	 * can write or replace this method with the actual implementation for list
	 * items.
	 * @return the dummy items
	 */
	private ArrayList<Data> getDummyLeftNavItems(){
	if (!StaticData.pref.contains(Const.USER_ID))
	{
		ArrayList<Data> al = new ArrayList<Data>();
		al.add(new Data("Event Feed", R.drawable.events_, R.drawable.events_sel));
		al.add(new Data("Discover Events", R.drawable.discover_, R.drawable.discover_sel));
		al.add(new Data("My Events", R.drawable.ic_nav3,
				R.drawable.ic_nav3_sel));
		al.add(new Data("Social Life", R.drawable.ic_nav2, R.drawable.ic_nav2_sel));
		al.add(new Data("More", R.drawable.ic_nav4, R.drawable.ic_nav4_sel));
		al.add(new Data("Rate this app", R.drawable.ic_nav6,
				R.drawable.ic_nav6_sel));
		al.add(new Data("Login/Register", R.drawable.ic_nav5, R.drawable.ic_nav5_sel));
		return al;
	}
	else
	{
		ArrayList<Data> al = new ArrayList<Data>();
		al.add(new Data("Event Feed", R.drawable.events_, R.drawable.events_sel));
		al.add(new Data("Discover Events", R.drawable.discover_, R.drawable.discover_sel));
		al.add(new Data("My Events", R.drawable.ic_nav3,
				R.drawable.ic_nav3_sel));
		al.add(new Data("Social Life", R.drawable.ic_nav2, R.drawable.ic_nav2_sel));
		al.add(new Data("More", R.drawable.ic_nav4, R.drawable.ic_nav4_sel));
		al.add(new Data("Rate this app", R.drawable.ic_nav6,
				R.drawable.ic_nav6_sel));
		return al;
		}
	}
	/**
	 * This method can be used to attach Fragment on activity view for a
	 * particular tab position. You can customize this method as per your need.
	 * 
	 * @param pos the position of tab selected.
	 */
	public void launchFragment(int pos)
	{
		CustomFragment f = null;
		String title = null;
		if (pos == 0)
		{
			TAG = "events";
			title = getString(R.string.Events);
			f = new Events();
			f.setArg(null);
		}
		else if (pos == 1)
		{
			/** Fabric "Discover Events Page" **/
			Answers.getInstance().logCustom(new CustomEvent("Discover Events Page")
					.putCustomAttribute("Activity", "Nav Bar"));

			title = getString(R.string.Discover_Events);
			f = new DiscoverEvents();
		}
		else if (pos == 3)
		{
			title = getString(R.string.feed);
			f = new FeedList();

		}
		else if (pos == 2)
		{
			title = getString(R.string.my_cal);
			f = new CalendarView();
		}
		else if (pos == 21)
		{
			if (StaticData.pref.contains(Const.USER_ID)) {
				title = getString(R.string.my_fav);
				f = new FavEvents();
				f.setArg(new Bundle());
				pos = 2;
			} if (!StaticData.pref.contains(Const.USER_ID)){
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setMessage(R.string.err_login)
					.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent intent = new Intent(MainActivity.this, Login.class);
							startActivity(intent);
						}
					})
					.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
						}
					});

			AlertDialog action = builder.create();
			action.show();
			pos = 2;
		}

		}
		else if (pos == 22)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setMessage("Sorry, Feature Coming Soon!")
					.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// User cancelled the dialog
						}
					});

			AlertDialog action = builder.create();
			action.show();
			pos = 2;
		}
		else if (pos == 4)
		{
			title = getString(R.string.more);
			f = new More();
		}
		else if (pos == 5)
		{
			Intent i = new Intent(Intent.ACTION_VIEW,
					Uri.parse("market://details?id=" + getPackageName()));
			startActivity(i);
		}
		else if (pos == 6)
		{
			Intent intent = new Intent(THIS, Login.class);
			startActivity(intent);
		}
		if (f != null)
		{
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content_frame, f, TAG)
					.addToBackStack(title)
					.commit();

			if (adapter != null)
				adapter.setSelection(pos);
		}
	}
	/**
	 * Setup the container fragment for drawer layout. The current
	 * implementation of this method simply calls launchFragment method for tab
	 * position 0. You can customize this method as per your need to display
	 * specific content.
	 */
	private void setupContainer()
	{
		getSupportFragmentManager().addOnBackStackChangedListener(
				new OnBackStackChangedListener() {

					@Override
					public void onBackStackChanged()
					{
						setActionBarTitle();
					}
				});
		launchFragment(0);
	}
	/**
	 * Set the action bar title text.
	 */
	private void setActionBarTitle()
	{
		if (drawerLayout.isDrawerOpen(drawerLeft))
		{
			getActionBar().setTitle(R.string.app_name);
			return;
		}
		if (getSupportFragmentManager().getBackStackEntryCount() == 0)
			return;
		String title = getSupportFragmentManager().getBackStackEntryAt(
				getSupportFragmentManager().getBackStackEntryCount() - 1)
				.getName();
		getActionBar().setTitle(title);
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		drawerToggle.onConfigurationChanged(newConfig);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (drawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		if (item.getItemId() == R.id.menu_fav && StaticData.pref.contains(Const.USER_ID)){

			/** Fabric **/
			Answers.getInstance().logCustom(new CustomEvent("Create Event")
					.putCustomAttribute("Page", 1));

			Intent intent = new Intent(MainActivity.this, editCreateEvent.class);
			startActivity(intent);
		} if (item.getItemId() == R.id.menu_fav && !StaticData.pref.contains(Const.USER_ID))
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage(R.string.err_login)
				.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(MainActivity.this, Login.class);
						startActivity(intent);
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});
		AlertDialog action = builder.create();
		action.show();
	}
		return super.onOptionsItemSelected(item);
	}
	//This will allow the back button to back through each view in the stack, if no more, ask the user to exit the app
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Log.e("back stack = ", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

			if (getSupportFragmentManager().getBackStackEntryCount() > 2) {
				FragmentManager fm = this.getFragmentManager();
				fm.popBackStack();
				//fm.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}	else {
				//Toast.makeText(this,"Nothing To Pop Back To",Toast.LENGTH_SHORT).show();
				new AlertDialog.Builder(this)
						.setMessage("Are you sure you want to exit?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@TargetApi(Build.VERSION_CODES.LOLLIPOP)
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finishAndRemoveTask();
							}
						})
						.setNegativeButton("No", null)
						.show();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		StaticData.clear();
	}

}


