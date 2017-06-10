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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import static com.adrenalinelife.utils.Const.EXTRA_DATA;
import static com.adrenalinelife.web.WebAccess.GET_FAV_EVENTS;
import static com.adrenalinelife.web.WebAccess.executePostRequest;
import static com.adrenalinelife.web.WebAccess.getUserParams;


public class FavEvents extends PagingFragment
{

    /** Search View **/
    ListView filterResults;
    SearchView searchView;

    /** Swipe Refresh Layout **/
    private SwipeRefreshLayout SwipeRefresh;

    /** The Events list. */
    private final ArrayList<Event> pList = new ArrayList<>();

    public String stringOfFav;


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
        searchView = (SearchView) v.findViewById(R.id.searchEvents);
        searchView.setVisibility(View.GONE);


        performCheck();
        setProgramList(v);
        filterResults = (ListView) v.findViewById(R.id.list);

        SwipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.EventsRefresh);
        SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                performCheck();
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
                al = WebHelper.grabFavEvents(page, Const.PAGE_SIZE_30);
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
                            onFinishLoading(al.size());
                            performFavFilter();
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

                if (newText.toString().toLowerCase().startsWith("f")){

                    Log.e(q);
                    Event item;

                    int s = fList.size();
                    String size = String.valueOf(s);
                    String size2 = String.valueOf(stringOfFav);
                    Log.e("Size of pList, ", size);
                    Log.e("Size of FavList ", size2);

                    for (int i = 0; i < fList.size(); i++) {

                        item = fList.get(i);
                        String itemFav = '"' + item.getId() + '"';

                        Log.e("itemFav ", itemFav);
                        Log.e("stringOfFav ", stringOfFav);

                        if (stringOfFav.contains(itemFav)) {
                            item = fList.get(i);
                            tempList.add(item);
                            Log.e("Item = ", item);
                            //i++;
                        }
                        String ss = String.valueOf(i);
                        Log.e("i = ", ss);
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

    public void performFavFilter()
    {
        final SearchAdapter favA = new SearchAdapter(getActivity(), pList);
        filterResults.setAdapter(favA);
        String favFilter = "f" + stringOfFav;
        favA.getFilter().filter(favFilter);
        adapter.notifyDataSetChanged();
        favA.notifyDataSetChanged();

    }

    public static String checkFavoriteEvents2()
    {
        try
        {
            Log.e("Events checkFavoriteEvents2");
            ArrayList<NameValuePair> param = getUserParams();
            param.add(new BasicNameValuePair("page", "1"));
            param.add(new BasicNameValuePair("page_size", "30"));
            String res = executePostRequest(GET_FAV_EVENTS, param, true);
            android.util.Log.e("String Res ", res);
            return res;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String saveFavStringEvents(String res)
    {
        this.stringOfFav = res;
        Log.e("String of Fav ", stringOfFav);
        return stringOfFav;
    }

    public String performCheck()
    {
        try
        {
            final ProgressDialog dia = parent
                    .showProgressDia(R.string.alert_loading);
            new Thread(new Runnable() {
                @Override
                public void run()
                {
                    final String s = checkFavoriteEvents2();
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dia.dismiss();
                            Log.e("Performing Check Now");

                            if (s == null){
                                Log.e("s is NULL", s);

                            }
                            else if (s != null){
                                Log.e("s is NOT null", s);
                                stringOfFav = s;
                                saveFavStringEvents(s);
                            }
                        }
                    });
                }
            }).start();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return true;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}

