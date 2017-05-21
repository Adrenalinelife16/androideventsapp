package com.adrenalinelife.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adrenalinelife.FeedDetail;
import com.adrenalinelife.MapViewActivity;
import com.adrenalinelife.R;
import com.adrenalinelife.custom.CustomFragment;
import com.adrenalinelife.model.Feed;
import com.adrenalinelife.utils.Commons;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.ImageLoader;
import com.adrenalinelife.utils.ImageLoader.ImageLoadedListener;
import com.adrenalinelife.utils.ImageUtils;
import com.adrenalinelife.utils.StaticData;
import com.adrenalinelife.utils.Utils;
import com.adrenalinelife.web.WebHelper;

import java.util.ArrayList;


public class FeedList extends CustomFragment
{

	/** Swipe Refresh Layout **/
	private SwipeRefreshLayout SwipeRefresh;

	/** The feed list. */
	private ArrayList<Feed> fList;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		final View v = inflater.inflate(R.layout.programs, null);
		setFeedList(v);

		SwipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.EventsRefresh);
		SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {

				setFeedList(v);

				SwipeRefresh.setRefreshing(false);
			}


		});
		// Configure the refreshing colors
		SwipeRefresh.setColorSchemeResources(android.R.color.holo_red_light);

		return v;
	}


	private void setFeedList(View v)
	{

		int w = StaticData.getDIP(100);
		int h = StaticData.getDIP(110);
		bmNoImg = ImageUtils.getPlaceHolderImage(R.drawable.no_image, w, h);

		loader = new ImageLoader(w, h, ImageUtils.SCALE_FIT_CENTER);

		final ListView list = (ListView) v.findViewById(R.id.list);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				startActivity(new Intent(parent, FeedDetail.class).putExtra(
						Const.EXTRA_DATA, fList.get(arg2)));
			}
		});

		final ProgressDialog dia = parent
				.showProgressDia(R.string.alert_loading);
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				fList = WebHelper.getFeedList();
				parent.runOnUiThread(new Runnable() {
					@Override
					public void run()
					{
						dia.dismiss();

						if (fList == null)
						{
							Utils.showDialog(parent,
									StaticData.getErrorMessage());
						}
						else
						{
							if (fList.size() == 0)
								Toast.makeText(parent, R.string.msg_no_content,
										Toast.LENGTH_SHORT).show();
							list.setAdapter(new ProgramAdapter());
						}

					}
				});

			}
		}).start();
	}


	private class ProgramAdapter extends BaseAdapter
	{

		/** The param. */
		private LayoutParams param = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return fList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Feed getItem(int position)
		{
			return fList.get(position);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position)
		{
			return position;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
				convertView = getLayoutInflater(null).inflate(
						R.layout.feed_item, null);

			Feed d = getItem(position);
			TextView lbl = (TextView) convertView.findViewById(R.id.lbl1);
			lbl.setText(d.getTitle());
			if (d.getType() == Feed.FEED_IG)
				lbl.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_ig, 0, 0, 0);
			else if (d.getType() == Feed.FEED_TW)
				lbl.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tw, 0, 0, 0);
			else
				lbl.setCompoundDrawablesWithIntrinsicBounds(0, 0,
						R.drawable.ic_fb, 0);

			lbl = (TextView) convertView.findViewById(R.id.lbl2);
			lbl.setText(d.getMsg());

			lbl = (TextView) convertView.findViewById(R.id.lbl3);
			lbl.setText(Commons.millsToDateTime(d.getDate()));

			ImageView img;
			if (d.getType() == Feed.FEED_IG)
			{
				img = (ImageView) convertView.findViewById(R.id.img1);
				img.setVisibility(View.VISIBLE);
			}
			else
			{
				img = (ImageView) convertView.findViewById(R.id.img1);
				img.setVisibility(View.GONE);
			}
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

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.map, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_map)
			startActivity(new Intent(getActivity(), MapViewActivity.class));
		return super.onOptionsItemSelected(item);
	}

}
