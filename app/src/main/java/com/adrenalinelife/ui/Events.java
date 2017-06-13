package com.adrenalinelife.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.adrenalinelife.CreateEvent;
import com.adrenalinelife.EventDetailActivity;
import com.adrenalinelife.Login;
import com.adrenalinelife.R;
import com.adrenalinelife.custom.PagingFragment;
import com.adrenalinelife.model.Event;
import com.adrenalinelife.utils.Commons;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.ImageLoader;
import com.adrenalinelife.utils.ImageLoader.ImageLoadedListener;
import com.adrenalinelife.utils.ImageUtils;
import com.adrenalinelife.utils.Log;
import com.adrenalinelife.utils.StaticData;
import com.adrenalinelife.utils.Utils;
import com.adrenalinelife.web.WebHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static com.adrenalinelife.utils.Const.EXTRA_DATA;


public class Events extends PagingFragment implements SearchView.OnQueryTextListener
{

	/** Search View **/
	SearchView searchView;
	ListView filterResults;

	/** Filter by Day **/
    Button mFilterMonday;
	Button mFilterTuesday;
	Button mFilterWednesday;
	Button mFilterThursday;
	Button mFilterFriday;
	Button mFilterSaturday;
	Button mFilterSunday;

	/** Swipe Refresh Layout **/
	private SwipeRefreshLayout SwipeRefresh;


	/** The Events list. */
	private final ArrayList<Event> pList = new ArrayList<>();
	private GoogleApiClient mGoogleApiClient;

	private final int REQUEST_PERMISSION_LOCATION = 0;

	////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		final View v = inflater.inflate(R.layout.events, null);
		setHasOptionsMenu(true);

		///////////////////////////////////////////////// - Initiating Views
		mFilterMonday = (Button) v.findViewById(R.id.btn_mon);
		mFilterTuesday = (Button) v.findViewById(R.id.btn_tues);
		mFilterWednesday = (Button) v.findViewById(R.id.btn_wed);
		mFilterThursday = (Button) v.findViewById(R.id.btn_thurs);
		mFilterFriday = (Button) v.findViewById(R.id.btn_fri);
		mFilterSaturday = (Button) v.findViewById(R.id.btn_sat);
		mFilterSunday = (Button) v.findViewById(R.id.btn_sun);

		filterResults = (ListView) v.findViewById(R.id.list);

/////////////////////////////////////////////////  - Calling Initial onCreate Methods
		setFilterTextWhite();
		setProgramList(v);
///////////////////////////////////////////////// - setOnClickListeners for each day in the filter tab

		mFilterMonday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setFilterTextWhite();
				mFilterMonday.setTextColor(getResources().getColor(R.color.adrenaline_red));
				final SearchAdapter monA = new SearchAdapter(getActivity(), pList);
				filterResults.setAdapter(monA);
				monA.getFilter().filter("dMon");
				monA.notifyDataSetChanged();
			}
		});

		mFilterTuesday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setFilterTextWhite();
				mFilterTuesday.setTextColor(getResources().getColor(R.color.adrenaline_red));
				final SearchAdapter monA = new SearchAdapter(getActivity(), pList);
				filterResults.setAdapter(monA);
				monA.getFilter().filter("dTue");
				monA.notifyDataSetChanged();
			}
		});

		mFilterWednesday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setFilterTextWhite();
				mFilterWednesday.setTextColor(getResources().getColor(R.color.adrenaline_red));
				final SearchAdapter monA = new SearchAdapter(getActivity(), pList);
				filterResults.setAdapter(monA);
				monA.getFilter().filter("dWed");
				monA.notifyDataSetChanged();
			}
		});

		mFilterThursday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setFilterTextWhite();
				mFilterThursday.setTextColor(getResources().getColor(R.color.adrenaline_red));
				final SearchAdapter monA = new SearchAdapter(getActivity(), pList);
				filterResults.setAdapter(monA);
				monA.getFilter().filter("dThu");
				monA.notifyDataSetChanged();
			}
		});

		mFilterFriday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setFilterTextWhite();
				mFilterFriday.setTextColor(getResources().getColor(R.color.adrenaline_red));
				final SearchAdapter monA = new SearchAdapter(getActivity(), pList);
				filterResults.setAdapter(monA);
				monA.getFilter().filter("dFri");
				monA.notifyDataSetChanged();
			}
		});
		mFilterSaturday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setFilterTextWhite();
				mFilterSaturday.setTextColor(getResources().getColor(R.color.adrenaline_red));
				final SearchAdapter monA = new SearchAdapter(getActivity(), pList);
				filterResults.setAdapter(monA);
				monA.getFilter().filter("dSat");
				monA.notifyDataSetChanged();
			}
		});

		mFilterSunday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setFilterTextWhite();
				mFilterSunday.setTextColor(getResources().getColor(R.color.adrenaline_red));
				final SearchAdapter monA = new SearchAdapter(getActivity(), pList);
				filterResults.setAdapter(monA);
				monA.getFilter().filter("dSun");
				monA.notifyDataSetChanged();
			}
		});
		//////////////////////////////////////////// - Swipe to Refresh

		SwipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.EventsRefresh);
		SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {

				setFilterTextWhite();
				setProgramList(v);
				SwipeRefresh.setRefreshing(false);
			}
		});
		//Configure the refreshing colors
		SwipeRefresh.setColorSchemeResources(android.R.color.holo_red_light);
		//Toast.makeText(activity, String.valueOf(hasFocus),Toast.LENGTH_SHORT).show();

        //////////////////////////////////////////// - SearchView Setup

		/***Search View***/
		searchView = (SearchView) v.findViewById(R.id.searchEvents);
		searchView.setQueryHint("Search Events");
		searchView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				searchView.setIconified(false);
			}
		});
		searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

			}
		});
		searchView.setOnCloseListener(new SearchView.OnCloseListener() {
			@Override
			public boolean onClose() {
				//Hide Keyboard//
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
				return false;
			}
		});
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
		{
			@Override
			public boolean onQueryTextSubmit(String query) {
                //Hide Keyboard//
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
				return true;
			}
			@Override
			public boolean onQueryTextChange(String newText) {

                final SearchAdapter sA = new SearchAdapter(getActivity(), pList);
                filterResults.setAdapter(sA);

				if (newText.length() >= 0) {
                    String search = "s" + newText;

					sA.getFilter().filter(search);
					Log.e(search);
				}
				return true;
			}
		});
		////////////////////////////////////////////END SEARCHVIEW SETUP

		return v;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////


	private void setProgramList(View v)
	{
		initPagingList((ListView) v.findViewById(R.id.list),
				new ProgramAdapter());
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3)
			{
				startActivity(new Intent(getActivity(),
						EventDetailActivity.class).putExtra(EXTRA_DATA,
						pList.get(arg2)));
			}
		});
		int w = StaticData.width;
		int h = (int) (StaticData.width / 2.25); //2.25
		bmNoImg = ImageUtils.getPlaceHolderImage(R.drawable.no_imagebig, w, h);
        loader = new ImageLoader(StaticData.width, StaticData.height,
                ImageUtils.SCALE_FIT_WIDTH);

		loadEventList();
	}
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void setSearchList(View v, final ArrayList newList)
    {
        final ArrayList<Event> n = newList;
        initPagingList((ListView) v.findViewById(R.id.list),
                new SearchAdapter(getActivity(), newList));
        filterResults.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3)
            {
                startActivity(new Intent(getActivity(),
                        EventDetailActivity.class).putExtra(EXTRA_DATA,
                        n.get(arg2)));
            }
        });
        int w = StaticData.width;
        int h = (int) (StaticData.width / 2.25); //2.25
        bmNoImg = ImageUtils.getPlaceHolderImage(R.drawable.no_imagebig, w, h);
    }
	////////////////////////////////////////////////////////////////////////////////////////////////
	private void loadEventList()
    {
		reset();
		pList.clear();
		adapter.notifyDataSetChanged();
		footer.setVisibility(View.GONE);
		loadNextPage();
	}
	////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void loadNextPage()
	{
		Log.e("loadNextPage");
		onStartLoading();
		final ProgressDialog dia;
		if (page == 0)
			dia = parent.showProgressDia(R.string.alert_loading);
		else
			dia = null;
		new Thread(new Runnable() {
			ArrayList<Event> al = new ArrayList<Event>();
			@Override
			public void run() {
				al = WebHelper.getEvents(page, Const.PAGE_SIZE_30);
				parent.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (dia != null)
							dia.dismiss();

						if (page == 0 && al == null) {
							Utils.showDialog(parent,
									StaticData.getErrorMessage());
						} else if (page == 0 && al.size() == 0)
							Toast.makeText(parent, R.string.msg_no_content,
									Toast.LENGTH_SHORT).show();
						if (al == null || al.size() == 0) {
							onFinishLoading(0);
						} else {
							pList.addAll(al);
							int i = pList.size();
							String s = String.valueOf(i);
							adapter.notifyDataSetChanged();
							//onFinishLoading(al.size());
						}
					}
				});
			}
		}).start();
	}
    @Override
    public boolean onQueryTextSubmit(String s) {return false;}
    @Override
    public boolean onQueryTextChange(String s) {return false;}
    ////////////////////////////////////////////////////////////////////////////////////////////////
	private class ProgramAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return pList.size();
		}

		@Override
		public Event getItem(int position)
		{
			return pList.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
				convertView = getLayoutInflater(null).inflate(
						R.layout.program_item, null);

			Event d = getItem(position);
			TextView lbl = (TextView) convertView.findViewById(R.id.lbl1);
			lbl.setText(d.getTitle());

			lbl = (TextView) convertView.findViewById(R.id.lbl2);
			lbl.setText(Html.fromHtml(d.getDesc()));

			lbl = (TextView) convertView.findViewById(R.id.lbl3);

			lbl.setText(Commons.millsToDateTime(d.getStartDateTime()));

			ImageView img = (ImageView) convertView.findViewById(R.id.img1);

            Bitmap bm = loader.loadImage(d.getImage(),
                    new ImageLoadedListener() {
                        @Override
                        public void imageLoaded(Bitmap bm)
                        {
                            if (bm != null)
                                notifyDataSetChanged();
                        }
                    });
            if (bm == null)
                img.setImageBitmap(bmNoImg);
            else
                img.setImageBitmap(bm);

			return convertView;
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////
	public class SearchAdapter extends BaseAdapter implements Filterable
	{
		Context mContext;
		LayoutInflater inflater;
        //int mLayout;
		public List<Event> eventNamesList;
		public ArrayList<Event> fList = new ArrayList<>();

		public SearchAdapter(Context context, List<Event> eventNamesList) {
			mContext = context;
            //this.mLayout = layout;
			this.eventNamesList = eventNamesList;
			inflater = LayoutInflater.from(mContext);
			this.fList.clear();
			this.fList.addAll(eventNamesList);
		}

		@Override
		public int getCount()
		{
			return fList.size();
		}

		@Override
		public Event getItem(int position)
		{
			return fList.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
				convertView = getLayoutInflater(null).inflate(
						R.layout.program_item, null);

			Event d = getItem(position);
			TextView lbl = (TextView) convertView.findViewById(R.id.lbl1);
			lbl.setText(d.getTitle());

			lbl = (TextView) convertView.findViewById(R.id.lbl2);
			lbl.setText(Html.fromHtml(d.getDesc()));

			lbl = (TextView) convertView.findViewById(R.id.lbl3);

			lbl.setText(Commons.millsToDateTime(d.getStartDateTime()));

			ImageView img = (ImageView) convertView.findViewById(R.id.img1);
			Bitmap bm = loader.loadImage(d.getImage(),
					new ImageLoadedListener() {

						@Override
						public void imageLoaded(Bitmap bm)
						{
							if (bm != null)
								notifyDataSetChanged();
						}
					});
			if (bm == null)
				img.setImageBitmap(bmNoImg);
			else
				img.setImageBitmap(bm);

			return convertView;
		}
		Filter myFilter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence newText) {

				FilterResults filterResults = new FilterResults();
				ArrayList<Event> tempList = new ArrayList<>();
                String q = newText.toString().substring(1);

				//SEARCH FILTER
                if (newText.toString().toLowerCase().startsWith("s")){
                    Log.e(q);
                    Event item;
                    for (int i = 0; i < fList.size(); i++) {

                        String eName;
						String eDesc;
                        eName = fList.get(i).getTitle().toLowerCase();
                        eDesc = fList.get(i).getDesc().toLowerCase();

                        if (eName.contains(q.toLowerCase()) || eDesc.contains(q.toLowerCase())) {
                            item = fList.get(i);
                            tempList.add(item);
                            Log.e(item);
                        }
                        filterResults.values = tempList;
                        filterResults.count = tempList.size();
                    }
					//BY DAY FILTER
                } if (newText.toString().toLowerCase().startsWith("d")){
                    Log.e(q);
                    Event item;
                    for (int i = 0; i < fList.size(); i++) {

                        long eDate;
                        eDate = fList.get(i).getStartDateTime();
						Log.e("eDate = ", eDate);
                        String eDay;
                        eDay = Commons.toDAY(eDate);
						Log.e("eDay = ", eDay);

                        if (eDay.equals(q)) {
                            item = fList.get(i);
							Log.e("Day Item = ", item);
                            tempList.add(item);
                        }
                        filterResults.values = tempList;
                        filterResults.count = tempList.size();
                    }
					//REFRESH FILTER
                } if (newText.toString().toLowerCase().startsWith("r")){

					Event item;
					for (int i = 0; i < fList.size(); i++) {

						item = fList.get(i);
						tempList.add(item);

						filterResults.values = tempList;
						filterResults.count = tempList.size();
					}
				}
                return filterResults;
            }
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence newText, FilterResults results) {
				if (results.count > 0) {
                    Log.e(newText);
					fList.clear();
					fList.addAll((ArrayList<Event>) results.values);
                    final View v = inflater.inflate(R.layout.events, null);
                    setSearchList(v, fList);
                    Log.e(fList);
					notifyDataSetChanged();
				} else {
                    Toast.makeText(getActivity(), "No Results Found!",Toast.LENGTH_SHORT).show();
                    fList.clear();
					notifyDataSetInvalidated();
				}
			}
		};
		@Override
		public Filter getFilter() {
			return myFilter;
		}
	}


	////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.add_refresh, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}

	/* ONCE THE FEATURE IS READY, USE THIS METHOD INSTEAD OF THE CURRENT
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_fav && !StaticData.pref.contains(Const.USER_ID))
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.err_login)
					.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent intent = new Intent(getActivity(), Login.class);
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
		} if (item.getItemId() == R.id.menu_fav && StaticData.pref.contains(Const.USER_ID)){
			Intent intent = new Intent(getActivity(), CreateEvent.class);
			startActivity(intent);
	}
		return true;
	}
*/


	// DELETE THIS METHOD AFTER CREATE EVENTS IS FINISHED
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_fav)
		{
			final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.err_beta)
					.setPositiveButton(R.string.back, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							builder.setCancelable(true);

						}
					});
			AlertDialog action = builder.create();
			action.show();
	}

		else if (item.getItemId() == R.id.refresh) {

			final SearchAdapter refreshA = new SearchAdapter(getActivity(), pList);
			filterResults.setAdapter(refreshA);
			setFilterTextWhite();
			refreshA.getFilter().filter("r");
			refreshA.notifyDataSetChanged();
		}

		return true;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////

	public void setFilterTextWhite(){
		mFilterMonday.setTextColor(getResources().getColor(R.color.white));
		mFilterTuesday.setTextColor(getResources().getColor(R.color.white));
		mFilterWednesday.setTextColor(getResources().getColor(R.color.white));
		mFilterThursday.setTextColor(getResources().getColor(R.color.white));
		mFilterFriday.setTextColor(getResources().getColor(R.color.white));
		mFilterSaturday.setTextColor(getResources().getColor(R.color.white));
		mFilterSunday.setTextColor(getResources().getColor(R.color.white));
	}
}
