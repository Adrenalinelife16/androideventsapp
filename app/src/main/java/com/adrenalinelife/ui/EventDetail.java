package com.adrenalinelife.ui;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adrenalinelife.BookTkt;
import com.adrenalinelife.Login;
import com.adrenalinelife.utils.StaticData;
import com.adrenalinelife.R;
import com.adrenalinelife.custom.CustomFragment;
import com.adrenalinelife.model.Event;
import com.adrenalinelife.utils.Commons;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.ImageLoader;
import com.adrenalinelife.utils.Log;
import com.adrenalinelife.utils.Utils;
import com.adrenalinelife.web.WebHelper;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

import static com.adrenalinelife.web.WebAccess.GET_FAV_EVENTS;
import static com.adrenalinelife.web.WebAccess.executePostRequest;
import static com.adrenalinelife.web.WebAccess.getUserParams;

/**
 * The Class EventDetail is the Fragment class that shows the details about an
 * Event. This Fragment is used inside the EventDetailActivity class. It also
 * show a Map with a marker on map for showing the location of that event. You
 * need to write your own logic for loading actual contents related to Events
 * and also need to show actual location for Event.
 */
public class EventDetail extends CustomFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

	/** The map view. */
	private MapView mMapView;

	/** The Google map. */
	private GoogleMap mMap;

	/** Location */
	public GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private FusedLocationProviderApi mFusedLocationProviderApi = LocationServices.FusedLocationApi;
	private double mMyLatitude;
	private double mMyLongitude;
	public double mLocationMiles;
	public double mLocationMeters;
	private TextView mDistance;
	private int mMiles;
	public int locationDenied = 0;

	/** The e. */
	private Event e;

	/** Share Image */
	private Uri shareImageUri;
	public ImageView imgShare;


	@RequiresApi(api = Build.VERSION_CODES.N)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.event_detail, null);
		e = (Event) getArg().getSerializable(Const.EXTRA_DATA);
		setHasOptionsMenu(true);

		/** Fabric Initializing **/
		Fabric.with(getActivity(), new Answers());
		Fabric.with(getActivity(), new Crashlytics());
		final Fabric fabric = new Fabric.Builder(getActivity())
				.kits(new Crashlytics())
				.debuggable(true)
				.build();
		Fabric.with(fabric);

		/** Fabric **/
		Answers.getInstance().logCustom(new CustomEvent("Event Pressed")
				.putCustomAttribute("Event Name", e.getTitle()));

		if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			StyleableToast.makeText(getContext(), "Location Denied", Toast.LENGTH_LONG, R.style.ToastGrey).show();
			locationDenied = 0;


		} else {
			//Initiate Location
			locationDenied = 1;
			mLocationRequest = new LocationRequest();
			mLocationRequest.setFastestInterval(100); //Call Immediately
			mLocationRequest.setNumUpdates(1); //Only call this 1 time
			mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			mGoogleApiClient = new GoogleApiClient.Builder(getContext())
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();
			mGoogleApiClient.connect();
			Log.e("Google Client");

			setupMap(v, savedInstanceState);
			////////////////////
		}

		setTouchNClick(v.findViewById(R.id.btnReg));
		showDetails(v);
		//setupMap(v, savedInstanceState);

		return v;
	}

	/**
	 * Show details.
	 *
	 * @param v the v
	 */
	@RequiresApi(api = Build.VERSION_CODES.N)
	private void showDetails(View v) {
		TextView lbl = (TextView) v.findViewById(R.id.lblTitle);
		lbl.setText(e.getTitle());

		lbl = (TextView) v.findViewById(R.id.lblAddress);
		lbl.setText(e.getAddress());

		lbl = (TextView) v.findViewById(R.id.lblAdress2);
		lbl.setText(e.getCitystate() + " " + e.getZip());

		Log.e("Description = ", e.getDesc());
		lbl = (TextView) v.findViewById(R.id.lblDesc);
		lbl.setText(e.getDesc());

		lbl = (TextView) v.findViewById(R.id.lblDate);
		lbl.setText(Commons.millsToDate(e.getStartDateTime()));

		lbl = (TextView) v.findViewById(R.id.lblDate2);
		if (e.getStartTime().equals(e.getEndTime())) {
			lbl.setVisibility(View.GONE);
		} else {
			lbl.setText(Commons.millsToTime(e.getStartDateTime())
					+ " to "
					+ Commons.millsToTime(e.getEndDateTime()));
		}

		if (locationDenied == 1) {
			mDistance = (TextView) v.findViewById(R.id.distance_event);
			mDistance.setText(" " + mMiles);
		}
		if (locationDenied == 0) {
			mDistance = (TextView) v.findViewById(R.id.distance_event);
			mDistance.setVisibility(View.GONE);
		}
	}

	@Override
	public void onPause() {
		if (locationDenied == 1) {
			mMapView.onPause();
			mGoogleApiClient.disconnect();
		}
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if (locationDenied == 1) {
			mMapView.onDestroy();
			mGoogleApiClient.disconnect();
		}
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (locationDenied == 1) {

			if (mGoogleApiClient.isConnected()) {
				locationGranted();
			}
			mMapView.onResume();

			mMap = mMapView.getMap();
			if (mMap != null) {
				mMap.setMyLocationEnabled(true);
				mMap.setInfoWindowAdapter(null);
				setupMarker();
			}
		}

	}

	//////////////////////////////////////////////////////////////// - Google Location API
	@Override
	public void onConnected(@Nullable Bundle bundle) {
		//Add Request Permission Here
		locationGranted();
	}

	public void locationGranted() {
		mFusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	@Override
	public void onLocationChanged(Location location) {
		//Toast.makeText(getActivity(), "Location: " + location.getLatitude() + " , " + location.getLongitude(), Toast.LENGTH_SHORT).show();
		mMyLatitude = location.getLatitude();
		mMyLongitude = location.getLongitude();
		LocationToMiles();
		mDistance.setText(" " + mMiles);
	}

	public double LocationToMiles(){
		double eLatitude = e.getLatitude();
		double eLongitude = e.getLongitude();

		//Event Location
		Location startPoint = new Location("locationA");
		startPoint.setLatitude(eLatitude);
		startPoint.setLongitude(eLongitude);

		//User Location
		Location endPoint = new Location("locationB");
		endPoint.setLatitude(mMyLatitude);
		endPoint.setLongitude(mMyLongitude);

		//Convert Meters to Miles
		mLocationMeters = startPoint.distanceTo(endPoint);
		mLocationMiles = mLocationMeters * 0.000621371;
		mMiles = (int) mLocationMiles;

		return mMiles;
	}
	//////////////////////////////////////////////////////////////// - Google Location API
	/**
	 * Setup and initialize the Google map view.
	 * 
	 * @param v
	 *            the root view
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	private void setupMap(View v, Bundle savedInstanceState)
	{
		try {
			MapsInitializer.initialize(getActivity());
		}catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		mMapView = (MapView) v.findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
	}
	/**
	 * This method simply place a few dummy location markers on Map View. You
	 * can write your own logic for loading the locations and placing the marker
	 * for each location as per your need.
	 */
	private void setupMarker()
	{
		mMap.clear();
		LatLng ll = new LatLng(e.getLatitude(), e.getLongitude());
		MarkerOptions opt = new MarkerOptions();
		opt.position(ll).title(e.getTitle()).snippet(e.getLocation());
		opt.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_RED));

		mMap.addMarker(opt);
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater)
	{
		if (StaticData.pref.contains(Const.USER_ID)) {
			inflater.inflate(R.menu.share_fav, menu);
			final ProgressDialog dia = parent
					.showProgressDia(R.string.alert_loading);
			new Thread(new Runnable() {
				@Override
				public void run()
				{
					final String id = '"' + e.getId() + '"';
					final String s = checkFavoriteEvents(e);
					parent.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							dia.dismiss();

							if (s.contains(id)){
								menu.findItem(R.id.menu_fav).setIcon(R.drawable.ic_fav_orange);
								e.setFav(true);
							}
							else if (!s.contains(id)){
								menu.findItem(R.id.menu_fav).setIcon(R.drawable.ic_fav);
								e.setFav(false);
							}
						}
					});
				}
			}).start();
		} else {
            inflater.inflate(R.menu.share, menu);
        }
		super.onCreateOptionsMenu(menu, inflater);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_share)
		{
			getLocalBitmapUri();
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("image/*");
			i.putExtra(Intent.EXTRA_STREAM, shareImageUri);
			i.putExtra(Intent.EXTRA_TEXT, e.getTitle() + " - " + "Find more local events and activities like this one by downloading the Adrenaline Life app now! #FindYourLife" + " - " + "www.onelink.to/life");
			startActivity(Intent.createChooser(i, getString(R.string.share)));
		}
		else //(item.getItemId() == R.id.menu_fav)
		{
			e.setFav(!e.isFav());
			if (e.isFav())
			{
				item.setIcon(R.drawable.ic_fav_orange);
				Toast.makeText(parent, R.string.msg_add_fav, Toast.LENGTH_SHORT).show();
			}
			else
			{
				item.setIcon(R.drawable.ic_fav);
				item.setTitle(R.string.add_to_fav);
				Toast.makeText(parent, R.string.msg_rem_fav, Toast.LENGTH_SHORT).show();
			}
			final ProgressDialog dia = parent
					.showProgressDia(R.string.alert_loading);
			new Thread(new Runnable() {
				@Override
				public void run()
				{
					String id = e.getId();
					WebHelper.addRemoveFavorite(id);
					parent.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							dia.dismiss();
							if (e.getId() == null) {
								Utils.showDialog(parent, StaticData.getErrorMessage());
							}
							else {
								Log.e("Fav is not null");
							}
						}
					});
				}
			}).start();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getId() == R.id.btnReg)
		{
			bookTicket();
		}
	}
	/**
	 * Book ticket.
	 */
	private void bookTicket()
	{
		if (e.isBooked())
			Utils.showDialog(parent, R.string.err_event_booked);
		else if (e.getAvailSpace() <= 0)
			Utils.showDialog(parent, R.string.err_no_space);
		else if (e.getEndDateTime() < System.currentTimeMillis())
			Utils.showDialog(parent, R.string.err_past_event);
		else if (!StaticData.pref.contains(Const.USER_ID))
			startActivityForResult(new Intent(parent, Login.class),
					Const.REQ_LOGIN);
		else
		{
			final ProgressDialog dia = parent
					.showProgressDia(R.string.alert_wait);
			new Thread(new Runnable() {
				@Override
				public void run()
				{
					final boolean book = WebHelper.isBooked(e);
					parent.runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							dia.dismiss();
							if (book)
								Utils.showDialog(parent,
										R.string.err_event_booked);
							else
								startActivity(new Intent(parent, BookTkt.class)
										.putExtra(Const.EXTRA_DATA, e));
						}
					});
				}
			}).start();
		}
	}

	public static String checkFavoriteEvents(Event z)
	{
		try
		{
			Log.e("EventDetail checkFavoriteEvents");
			ArrayList<NameValuePair> param = getUserParams();
			param.add(new BasicNameValuePair("page", "1"));
			param.add(new BasicNameValuePair("page_size", "30"));
			String res = executePostRequest(GET_FAV_EVENTS, param, true);
			return res;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Const.REQ_LOGIN && resultCode == Activity.RESULT_OK)
			bookTicket();
	}

	public Uri getLocalBitmapUri() {
		// Extract Bitmap from ImageView drawable
		Bitmap bmp;
		if (e.getImage() != "") {
			bmp = loader.loadImage(e.getImage(),
					new ImageLoader.ImageLoadedListener() {
						@Override
						public void imageLoaded(Bitmap bm)
						{
							if (bm != null)
								Toast.makeText(parent, "No BitMap", Toast.LENGTH_SHORT).show();
								imgShare.setImageBitmap(bmNoImg);
						}
					});
		} else {
			//Get Bitmap for Drawable File no_image.png
			bmp = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.no_imagebig);
		}
		// Store image to default external storage directory
		Uri bmpUri = null;
		try {
			File file =  new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
			file.getParentFile().mkdirs();
			FileOutputStream out = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
			bmpUri = Uri.fromFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}


		shareImageUri = bmpUri;
		return bmpUri;

	}

}
