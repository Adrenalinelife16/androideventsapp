package com.adrenalinelife.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.adrenalinelife.EventDetailActivity;
import com.adrenalinelife.R;
import com.adrenalinelife.custom.PagingFragment;
import com.adrenalinelife.custom.PicassoTransform;
import com.adrenalinelife.model.Event;
import com.adrenalinelife.utils.Commons;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.ImageLoader;
import com.adrenalinelife.utils.ImageUtils;
import com.adrenalinelife.utils.Log;
import com.adrenalinelife.utils.StaticData;
import com.adrenalinelife.utils.Utils;
import com.adrenalinelife.web.WebHelper;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.SearchEvent;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.squareup.picasso.Picasso;

import com.crashlytics.android.answers.Answers;
import io.fabric.sdk.android.Fabric;

import java.util.ArrayList;
import java.util.List;

import static com.adrenalinelife.utils.Const.EXTRA_DATA;

public class Events extends PagingFragment
{
	/** Search View **/
	ListView filterResults;

	/** Filter by Day **/
    Button mFilterMonday;
	Button mFilterTuesday;
	Button mFilterWednesday;
	Button mFilterThursday;
	Button mFilterFriday;
	Button mFilterSaturday;
	Button mFilterSunday;
	Button mFilterAll;
	Button mDiscoverEvents;

	public LinearLayout vTabs;
	/** Swipe Refresh Layout **/
	private SwipeRefreshLayout SwipeRefresh;
	/** The Events list. */
	private final ArrayList<Event> pList = new ArrayList<>();

	////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		final View v = inflater.inflate(R.layout.events, null, false);
		setHasOptionsMenu(true);
		setRetainInstance(true);

		/** Fabric Initializing **/
		Fabric.with(getActivity(), new Answers());
		Fabric.with(getActivity(), new Crashlytics());
		final Fabric fabric = new Fabric.Builder(getActivity())
				.kits(new Crashlytics())
				.debuggable(true)
				.build();
		Fabric.with(fabric);

		///////////////////////////////////////////////// - Initiating Views

		mFilterMonday = (Button) v.findViewById(R.id.btn_mon);
		mFilterTuesday = (Button) v.findViewById(R.id.btn_tues);
		mFilterWednesday = (Button) v.findViewById(R.id.btn_wed);
		mFilterThursday = (Button) v.findViewById(R.id.btn_thurs);
		mFilterFriday = (Button) v.findViewById(R.id.btn_fri);
		mFilterSaturday = (Button) v.findViewById(R.id.btn_sat);
		mFilterSunday = (Button) v.findViewById(R.id.btn_sun);
		mFilterAll = (Button) v.findViewById(R.id.button_all_filter);
		mDiscoverEvents = (Button) v.findViewById(R.id.discover_btn);
		filterResults = (ListView) v.findViewById(R.id.list);
		vTabs = (LinearLayout) v.findViewById(R.id.vTabsF);
		vTabs.setVisibility(View.GONE);

		/////////////////////////////////////  - Calling Initial onCreate Methods
		setFilterTextWhite();
		mFilterAll.setTextColor(getResources().getColor(R.color.adrenaline_red));
		setProgramList(v);
		////////////////////////////////////// - setOnClickListeners for each day in the filter tab

		mDiscoverEvents.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Take User to Discover Events Page
				setFilterTextWhite();
				mDiscoverEvents.setTextColor(getResources().getColor(R.color.adrenaline_red));

				/** Fabric "Discover Events Page" **/
				Answers.getInstance().logCustom(new CustomEvent("Discover Events Page")
								.putCustomAttribute("Activity", "Events Page"));


				DiscoverEvents f = new DiscoverEvents();
				android.support.v4.app.FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.add(R.id.content_frame, f).addToBackStack("Discover Events").commit();


			}
		});

		mFilterAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				setFilterTextWhite();
				mFilterAll.setTextColor(getResources().getColor(R.color.adrenaline_red));
				softEventRefresh();
			}
		});

		mFilterMonday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				/** Fabric **/
				Answers.getInstance().logCustom(new CustomEvent("Events_Filter")
						.putCustomAttribute("Day", "Monday"));

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

				/** Fabric **/
				Answers.getInstance().logCustom(new CustomEvent("Events_Filter")
						.putCustomAttribute("Day", "Tuesday"));

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

				/** Fabric **/
				Answers.getInstance().logCustom(new CustomEvent("Events_Filter")
						.putCustomAttribute("Day", "Wednesday"));

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

				/** Fabric **/
				Answers.getInstance().logCustom(new CustomEvent("Events_Filter")
						.putCustomAttribute("Day", "Thursday"));

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

				/** Fabric **/
				Answers.getInstance().logCustom(new CustomEvent("Events_Filter")
						.putCustomAttribute("Day", "Friday"));

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

				/** Fabric **/
				Answers.getInstance().logCustom(new CustomEvent("Events_Filter")
						.putCustomAttribute("Day", "Saturday"));

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

				/** Fabric **/
				Answers.getInstance().logCustom(new CustomEvent("Events_Filter")
						.putCustomAttribute("Day", "Sunday"));

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
                mFilterAll.setTextColor(getResources().getColor(R.color.adrenaline_red));
				setProgramList(v);
				SwipeRefresh.setRefreshing(false);
			}
		});
		//Configure the refreshing colors
		SwipeRefresh.setColorSchemeResources(android.R.color.holo_red_light);
		//Toast.makeText(activity, String.valueOf(hasFocus),Toast.LENGTH_SHORT).show();
		return v;
	}
	// End of onCreate Method
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
		Log.e("setProgramList get Rounded Corner");
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
							adapter.notifyDataSetChanged();
							//onFinishLoading(al.size());
						}
					}
				});
			}
		}).start();
	}
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

		@SuppressLint("RestrictedApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
				convertView = getLayoutInflater(null).inflate(
						R.layout.program_item, null);

			Event d = getItem(position);
			TextView lbl = (TextView) convertView.findViewById(R.id.lbl1);
			lbl.setText(d.getTitle());

			lbl = (TextView) convertView.findViewById(R.id.lbl3);

			// 12:00AM to All Day
			if (Commons.millsToDateTime(d.getStartDateTime()).contains("Today - 12:00 AM")){
				lbl.setText("Today - " + Commons.mToDate(d.getStartDateTime()));
			} else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Monday 12:00 AM")){
			lbl.setText("Monday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Tuesday 12:00 AM")){
				lbl.setText("Tuesday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Wednesday 12:00 AM")){
				lbl.setText("Wednesday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Thursday 12:00 AM")){
				lbl.setText("Thursday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Friday 12:00 AM")){
				lbl.setText("Friday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Saturday 12:00 AM")){
				lbl.setText("Saturday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Sunday 12:00 AM")){
				lbl.setText("Sunday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else {
				lbl.setText(Commons.millsToDateTime(d.getStartDateTime()));
			}

			ImageView img = (ImageView) convertView.findViewById(R.id.img1);

			//Log.e("ProgramAdapter GetView");
			ImageView shadow = (ImageView) convertView.findViewById(R.id.shadow);
			Bitmap sh = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.shadow_medium);
			shadow.setImageBitmap(ImageUtils.getRoundedCornerBitmapLess(sh));

			Picasso.with(getContext()).load(d.getImage()).transform(new PicassoTransform(40,0)).placeholder(R.drawable.no_imagebig).into(img);

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

		@SuppressLint("RestrictedApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
				convertView = getLayoutInflater(null).inflate(
						R.layout.program_item, null);

			Event d = getItem(position);
			TextView lbl = (TextView) convertView.findViewById(R.id.lbl1);
			lbl.setText(d.getTitle());

			lbl = (TextView) convertView.findViewById(R.id.lbl3);

			// 12:00AM to All Day
			if (Commons.millsToDateTime(d.getStartDateTime()).contains("Today - 12:00 AM")){
				lbl.setText("Today - " + Commons.mToDate(d.getStartDateTime()));
			} else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Monday 12:00 AM")){
				lbl.setText("Monday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Tuesday 12:00 AM")){
				lbl.setText("Tuesday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Wednesday 12:00 AM")){
				lbl.setText("Wednesday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Thursday 12:00 AM")){
				lbl.setText("Thursday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Friday 12:00 AM")){
				lbl.setText("Friday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Saturday 12:00 AM")){
				lbl.setText("Saturday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else if (Commons.millsToDateTime(d.getStartDateTime()).contains("Sunday 12:00 AM")){
				lbl.setText("Sunday - " + Commons.mToDate(d.getStartDateTime()));
			}
			else {
				lbl.setText(Commons.millsToDateTime(d.getStartDateTime()));
			}

			ImageView img = (ImageView) convertView.findViewById(R.id.img1);
			//Log.e("ProgramAdapter GetView");
			ImageView shadow = (ImageView) convertView.findViewById(R.id.shadow);
			Bitmap sh = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.shadow_medium);
			shadow.setImageBitmap(ImageUtils.getRoundedCornerBitmapLess(sh));

			Picasso.with(getContext()).load(d.getImage()).transform(new PicassoTransform(40,0)).placeholder(R.drawable.no_imagebig).into(img);

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
					StyleableToast.makeText(getContext(), "No Results Found", Toast.LENGTH_LONG, R.style.ToastGrey).show();
                    fList.clear();
					notifyDataSetInvalidated();
				}}};
		@Override
		public Filter getFilter() {
			return myFilter;
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////


	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		menu.clear();
		//Setup search button in action bar
		final MenuItem item = menu.add("");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		final SearchView searchView = new SearchView(getActivity());
		item.setActionView(searchView);

		//add "add" button to action bar
		inflater.inflate(R.menu.add, menu);

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
		{
			@Override
			public boolean onQueryTextSubmit(String query) {

				/** Fabric **/
				Answers.getInstance().logSearch(new SearchEvent());
				Answers.getInstance().logSearch(new SearchEvent()
						.putQuery(query));

				//Hide Keyboard//
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
				return true;
			}
			@Override
			public boolean onQueryTextChange(String newText) {

				final SearchAdapter sA = new SearchAdapter(getActivity(), pList);
				filterResults.setAdapter(sA);

				if (newText.length() >= 1) {
					String search = "s" + newText;

					sA.getFilter().filter(search);
					Log.e(search);
				} if (newText == "") {
					sA.getFilter().filter("s");
				}
				return true;
			}
		});
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
		mFilterAll.setTextColor(getResources().getColor(R.color.white));
		mDiscoverEvents.setTextColor(getResources().getColor(R.color.white));
	}

	public void softEventRefresh(){

		final SearchAdapter refreshA = new SearchAdapter(getActivity(), pList);
		filterResults.setAdapter(refreshA);
		setFilterTextWhite();
		refreshA.getFilter().filter("r");
		refreshA.notifyDataSetChanged();
		mFilterAll.setTextColor(getResources().getColor(R.color.red));

	}
}
