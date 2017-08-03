package com.adrenalinelife.ui;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
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
import com.adrenalinelife.custom.CustomFragment;
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

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static com.adrenalinelife.utils.Const.EXTRA_DATA;


public class CategoryEvents extends PagingFragment
{

    /** Search View **/
    static ListView filterResults;


    /** Swipe Refresh Layout **/
    private SwipeRefreshLayout SwipeRefresh;

    /** The Events list. */
    private static final ArrayList<Event> pList = new ArrayList<>();

    public static String filterKey;

    public View mDayFilterScroll;
    public LinearLayout mExtActionBar;
    public SearchView searchView;

    public Bundle mBundle;
    public Intent mIntentOut;
    public String mFilter;

    public FragmentManager mFragmentManager;
    public ActionBar mBar;
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        mBundle = new Bundle();
        mBundle = getArguments();
        mFilter = mBundle.getString("Filter");
        final View v = inflater.inflate(R.layout.events, null);
        setHasOptionsMenu(true);

        searchView = (SearchView) v.findViewById(R.id.searchEvents);
        searchView.setVisibility(View.GONE);
        mDayFilterScroll = v.findViewById(R.id.dayFilterScroll);
        mDayFilterScroll.setVisibility(View.GONE);
        mExtActionBar = (LinearLayout) v.findViewById(R.id.extActionBar);
        mExtActionBar.setVisibility(View.GONE);

        //performDiscoverSetup();
        setProgramList(v);
        filterResults = (ListView) v.findViewById(R.id.list);

        SwipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.EventsRefresh);
        SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                setProgramList(v);
                SwipeRefresh.setRefreshing(false);
            }
        });
        SwipeRefresh.setColorSchemeResources(android.R.color.holo_red_light);

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
        int w = StaticData.getDIP(60);
        int h = StaticData.getDIP(60);
        bmNoImg = ImageUtils.getPlaceHolderImage(R.drawable.no_image, w, h);
        loader = new ImageLoader(w, h, ImageUtils.SCALE_FIT_CENTER);

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
                            //adapter.notifyDataSetChanged();
                            //onFinishLoading(al.size());
                            performDiscoverFilter();
                        }
                    }
                });
            }
        }).start();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public class ProgramAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Event getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            /*
            if (convertView == null)
                convertView = getLayoutInflater(null).inflate(
                        R.layout.event_item, null);

            Event d = getItem(position);
            TextView lbl = (TextView) convertView.findViewById(R.id.lbl1);
            lbl.setText(Commons.millsToDate(d.getStartDateTime()));

            lbl = (TextView) convertView.findViewById(R.id.lbl2);
            lbl.setText(Commons.millsToTime(d.getStartDateTime()));

            lbl = (TextView) convertView.findViewById(R.id.lbl3);
            lbl.setText(d.getTitle());

            lbl = (TextView) convertView.findViewById(R.id.lbl4);
            lbl.setText(d.getLocation());

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

                */
            return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public class SearchAdapter extends BaseAdapter implements Filterable
    {
        Context mContext;
        LayoutInflater inflater;
        public List<Event> eventNamesList;
        public ArrayList<Event> fList = new ArrayList<>();

        public SearchAdapter(Context context, List<Event> eventNamesList) {
            mContext = context;
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
                        R.layout.event_item, null);

            Event d = getItem(position);
            TextView lbl = (TextView) convertView.findViewById(R.id.lbl1);
            lbl.setText(Commons.millsToDate(d.getStartDateTime()));

            lbl = (TextView) convertView.findViewById(R.id.lbl2);
            lbl.setText(Commons.millsToTime(d.getStartDateTime()));

            lbl = (TextView) convertView.findViewById(R.id.lbl3);
            lbl.setText(d.getTitle());

            lbl = (TextView) convertView.findViewById(R.id.lbl4);
            lbl.setText(d.getLocation());

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
                if (newText.toString().toLowerCase().startsWith("x")) {
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
                }
                //CATEGORY FILTER
                if (mFilter.toLowerCase() != null){
                    Log.e(newText);
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
                }

                return filterResults;
            }
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence newText, FilterResults results) {
                if (results.count > 0) {
                    fList.clear();
                    fList.addAll((ArrayList<Event>) results.values);

                    pList.clear();
                    pList.addAll((ArrayList<Event>) results.values);

                    final View v = inflater.inflate(R.layout.events, null);
                    setSearchList(v, fList);
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
    public void performDiscoverFilter()
    {
        final SearchAdapter favA = new SearchAdapter(getActivity(), pList);
        filterResults.setAdapter(favA);
        favA.getFilter().filter(mFilter);
        adapter.notifyDataSetChanged();
        favA.notifyDataSetChanged();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}

