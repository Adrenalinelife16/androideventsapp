package com.adrenalinelife.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

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

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */

	////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		final View v = inflater.inflate(R.layout.events, null);
		setHasOptionsMenu(true);

		setProgramList(v);
        filterResults = (ListView) v.findViewById(R.id.list);

        mFilterMonday = (Button) v.findViewById(R.id.button_filter_monday);
        mFilterMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SearchAdapter fA = new SearchAdapter(getActivity(), pList);
                filterResults.setAdapter(fA);
                fA.getFilter().filter("dMon");
                Log.e("date button");

            }
        });

		SwipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.EventsRefresh);
		SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {

				setProgramList(v);
				SwipeRefresh.setRefreshing(false);
			}
		});
		//Configure the refreshing colors
		SwipeRefresh.setColorSchemeResources(android.R.color.holo_red_light);
		//Toast.makeText(activity, String.valueOf(hasFocus),Toast.LENGTH_SHORT).show();

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

		// loader = new ImageLoader(w,h,ImageUtils.SCALE_ASPECT_WIDTH);
		//loader = new ImageLoader(StaticData.width, StaticData.height,
		//		ImageUtils.SCALE_FIT_WIDTH);

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
			@Override
			public void run()
			{
				final ArrayList<Event> al;
				if (getArg() == null)
					al = WebHelper.getEvents(page, Const.PAGE_SIZE_30);
				else
					al = WebHelper.getFavoriteEvents(page, Const.PAGE_SIZE_30);
				parent.runOnUiThread(new Runnable() {
					@Override
					public void run()
					{
						if (dia != null)
							dia.dismiss();

						if (page == 0 && al == null)
						{
							Utils.showDialog(parent,
									StaticData.getErrorMessage());
						}
						else if (page == 0 && al.size() == 0)
							Toast.makeText(parent, R.string.msg_no_content,
									Toast.LENGTH_SHORT).show();

						if (al == null || al.size() == 0)
						{
							onFinishLoading(0);
						}
						else
						{
							pList.addAll(al);

							adapter.notifyDataSetChanged();

							onFinishLoading(al.size());
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

                if (newText.toString().toLowerCase().startsWith("s")){
                    Log.e(q);
                    Event item;
                    for (int i = 0; i < fList.size(); i++) {

                        String eName;
						String eDesc;
                        eName = fList.get(i).getTitle().toLowerCase();
                        eDesc = pList.get(i).getDesc().toLowerCase();

                        if (eName.contains(q.toLowerCase())|| eDesc.contains(q.toLowerCase())) {
                            item = fList.get(i);
                            tempList.add(item);
                            Log.e(item);
                            i++;
                        }
                        filterResults.values = tempList;
                        filterResults.count = tempList.size();
                    }
                } if (newText.toString().toLowerCase().startsWith("d")){
                    Log.e(q);
                    Event item;
                    for (int i = 0; i < fList.size(); i++) {
                        long eDate;
                        eDate = fList.get(i).getStartDateTime();
                        String eDay;
                        eDay = Commons.toDAY(eDate);
                        Log.e("eDay = ", eDay);
                        Log.e("q = ", q);

                        if (eDay.equals(q)) {
                            item = fList.get(i);
                            tempList.add(item);
                            Log.e("Item = ", item);
                            i++;
                        }
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
		if (StaticData.pref.contains(Const.USER_ID)) {
			inflater.inflate(R.menu.add, menu);
		}

		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_fav)
		{
			Intent intent = new Intent(getActivity(), CreateEvent.class);
			startActivity(intent);
		}
		return true;
	}
	////////////////////////////////////////////////////////////////////////////////////////////////
}
