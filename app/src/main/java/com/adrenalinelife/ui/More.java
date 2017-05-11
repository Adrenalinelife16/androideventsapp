package com.adrenalinelife.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adrenalinelife.MoreDetail;
import com.adrenalinelife.R;
import com.adrenalinelife.Settings;
import com.adrenalinelife.custom.CustomFragment;
import com.adrenalinelife.utils.Const;

/**
 * The Class More is the Fragment class that is launched when the user clicks on
 * More option in Left navigation drawer and it simply shows a few options for
 * like Help, Privacy, Account, About etc. You can customize this to display
 * actual contents.
 */
public class More extends CustomFragment
{

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.more, null);

		setTouchNClick(v.findViewById(R.id.help));
		setTouchNClick(v.findViewById(R.id.privacy));
		setTouchNClick(v.findViewById(R.id.terms));
		setTouchNClick(v.findViewById(R.id.settings));
		return v;
	}

	/* (non-Javadoc)
	 * @see com.adrenalinelife.custom.CustomFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);

		int title;
		int text;
		if (v.getId() == R.id.help)
		{
			title = R.string.help_center;
			text = R.string.help_centre_text;
					startActivity(new Intent(parent, MoreDetail.class).putExtra(
					Const.EXTRA_DATA, text).putExtra(Const.EXTRA_DATA1, title));
		}
		else if (v.getId() == R.id.privacy)
		{
			title = R.string.privacy;
			text = R.string.privacy_text;
					startActivity(new Intent(parent, MoreDetail.class).putExtra(
					Const.EXTRA_DATA, text).putExtra(Const.EXTRA_DATA1, title));
		}
		else if (v.getId() == R.id.terms)
		{
			title = R.string.terms_condi;
			text = R.string.terms_text;
					startActivity(new Intent(parent, MoreDetail.class).putExtra(
					Const.EXTRA_DATA, text).putExtra(Const.EXTRA_DATA1, title));
		}
		else
		{
			Intent intent = new Intent(getActivity(), Settings.class);
			startActivity(intent);

		}
	}
	public void btnClick(View v)
	{

		Intent intent = new Intent(getActivity(), Settings.class);
		startActivity(intent);
	}
}
