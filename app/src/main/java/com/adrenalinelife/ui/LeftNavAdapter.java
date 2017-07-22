package com.adrenalinelife.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.adrenalinelife.R;
import com.adrenalinelife.model.Data;

import java.util.ArrayList;

/**
 * The Adapter class for the ListView displayed in the left navigation drawer.
 */
public class LeftNavAdapter extends BaseAdapter
{
	/** The items. */
	private ArrayList<Data> items;

	/** The context. */
	private Context context;

	/** The selected. */
	private int selected;

	/**
	 * Setup the current selected position of adapter.
	 * 
	 * @param position
	 *            the new selection
	 */
	public void setSelection(int position)
	{
		selected = position;
		notifyDataSetChanged();
	}
	/**
	 * Instantiates a new left navigation adapter.
	 * 
	 * @param context
	 *            the context of activity
	 * @param items
	 *            the array of items to be displayed on ListView
	 */
	public LeftNavAdapter(Context context, ArrayList<Data> items)
	{
		this.context = context;
		this.items = items;
	}
	@Override
	public int getCount()
	{
		return items.size();
	}

	@Override
	public Data getItem(int arg0)
	{
		return items.get(arg0);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView  , ViewGroup parent)
	{
		if (convertView == null)
			convertView = LayoutInflater.from(context).inflate(
					R.layout.left_nav_item, null);
		TextView lbl = (TextView) convertView;
		lbl.setText(getItem(position).getTitle1());

		if (position == selected)
		{
			lbl.setCompoundDrawablesWithIntrinsicBounds(getItem(position)
					.getImage2(), 0, 0, 0);
			lbl.setBackgroundColor(context.getResources().getColor(
					R.color.adrenaline_grey));
			lbl.setTextColor(Color.WHITE);
		}
		else
		{
			lbl.setCompoundDrawablesWithIntrinsicBounds(getItem(position)
					.getImage1(), 0, 0, 0);
			lbl.setBackgroundResource(0);
			lbl.setTextColor(context.getResources().getColor(
					R.color.main_color_gray_dk));
		}
		return convertView;
	}

}
