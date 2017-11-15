package com.adrenalinelife.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adrenalinelife.Login;
import com.adrenalinelife.Logout;
import com.adrenalinelife.MoreDetail;
import com.adrenalinelife.R;
import com.adrenalinelife.Register;
import com.adrenalinelife.Settings;
import com.adrenalinelife.custom.CustomFragment;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.StaticData;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import io.fabric.sdk.android.Fabric;

/**
 * The Class More is the Fragment class that is launched when the user clicks on
 * More option in Left navigation drawer and it simply shows a few options for
 * like Help, Privacy, Account, About etc. You can customize this to display
 * actual contents.
 */
public class More extends CustomFragment
{
	public TextView mLogin;
	public TextView mLogout;
	public TextView mRegister;

	public View vLogoutView;
	public View vLoginView;
	public View vRegView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.more, null);

		/** Fabric Initializing **/
		Fabric.with(getActivity(), new Answers());
		Fabric.with(getActivity(), new Crashlytics());
		final Fabric fabric = new Fabric.Builder(getActivity())
				.kits(new Crashlytics())
				.debuggable(true)
				.build();
		Fabric.with(fabric);

		if (!StaticData.pref.contains(Const.USER_ID)) { //We are not logged in

			//Logout + Bar = GONE
			mLogout = (TextView) v.findViewById(R.id.more_Logout);
			mLogout.setVisibility(View.GONE);
			vLogoutView = v.findViewById(R.id.logout_view);
			vLogoutView.setVisibility(View.GONE);
			//Buttons Setup
			setTouchNClick(v.findViewById(R.id.help));
			setTouchNClick(v.findViewById(R.id.privacy));
			setTouchNClick(v.findViewById(R.id.terms));
			setTouchNClick(v.findViewById(R.id.more_Login));
			setTouchNClick(v.findViewById(R.id.more_register));

		} else { //We are Logged In

			//Login + Bar = GONE
			mLogin = (TextView) v.findViewById(R.id.more_Login);
			mLogin.setVisibility(View.GONE);
			vLoginView = v.findViewById(R.id.login_view);
			vLoginView.setVisibility(View.GONE);
			//Register + Bar = GONE
			mRegister = (TextView) v.findViewById(R.id.more_register);
			mRegister.setVisibility(View.GONE);
			vRegView = v.findViewById(R.id.register_view);
			vRegView.setVisibility(View.GONE);
			//Buttons Setup
			setTouchNClick(v.findViewById(R.id.help));
			setTouchNClick(v.findViewById(R.id.privacy));
			setTouchNClick(v.findViewById(R.id.terms));
			setTouchNClick(v.findViewById(R.id.more_Logout));
		}

		return v;
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		int title;
		int text;
		if (v.getId() == R.id.help)
		{
			/** Fabric **/
			Answers.getInstance().logCustom(new CustomEvent("Settings_Pressed")
					.putCustomAttribute("Settings_Page", "Help"));

			title = R.string.help_center;
			text = R.string.help_centre_text;
					startActivity(new Intent(parent, MoreDetail.class).putExtra(
					Const.EXTRA_DATA, text).putExtra(Const.EXTRA_DATA1, title));
		}
		else if (v.getId() == R.id.privacy)
		{
			/** Fabric **/
			Answers.getInstance().logCustom(new CustomEvent("Settings_Pressed")
					.putCustomAttribute("Settings_Page", "Privacy"));

			title = R.string.privacy;
			text = R.string.privacy_text;
					startActivity(new Intent(parent, MoreDetail.class).putExtra(
					Const.EXTRA_DATA, text).putExtra(Const.EXTRA_DATA1, title));
		}
		else if (v.getId() == R.id.terms)
		{
			/** Fabric **/
			Answers.getInstance().logCustom(new CustomEvent("Settings_Pressed")
					.putCustomAttribute("Settings_Page", "Terms and Conditions"));

			title = R.string.terms_condi;
			text = R.string.terms_text;
					startActivity(new Intent(parent, MoreDetail.class).putExtra(
					Const.EXTRA_DATA, text).putExtra(Const.EXTRA_DATA1, title));
		}
		else if (v.getId() == R.id.more_Login)
		{

			/** Fabric **/
			Answers.getInstance().logCustom(new CustomEvent("Settings_Pressed")
					.putCustomAttribute("Settings_Page", "Login"));

            title = R.string.terms_condi;
            text = R.string.terms_text;
			startActivity(new Intent(parent, Login.class).putExtra(
					Const.EXTRA_DATA, text).putExtra(Const.EXTRA_DATA1, title));
		}
		else if (v.getId() == R.id.more_Logout)
		{
			/** Fabric **/
			Answers.getInstance().logCustom(new CustomEvent("Settings_Pressed")
					.putCustomAttribute("Settings_Page", "Logout"));

            title = R.string.terms_condi;
            text = R.string.terms_text;
			startActivity(new Intent(parent, Logout.class).putExtra(
					Const.EXTRA_DATA, text).putExtra(Const.EXTRA_DATA1, title));
		}
		else if (v.getId() == R.id.more_register)
		{
			/** Fabric **/
			Answers.getInstance().logCustom(new CustomEvent("Settings_Pressed")
					.putCustomAttribute("Settings_Page", "Register"));

            title = R.string.terms_condi;
            text = R.string.terms_text;
			startActivity(new Intent(parent, Register.class).putExtra(
					Const.EXTRA_DATA, text).putExtra(Const.EXTRA_DATA1, title));
		}
	}
	public void btnClick(View v)
	{
		Intent intent = new Intent(getActivity(), Settings.class);
		startActivity(intent);
	}
}
