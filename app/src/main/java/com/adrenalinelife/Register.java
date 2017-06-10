package com.adrenalinelife;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.model.Status;
import com.adrenalinelife.utils.Commons;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.Log;
import com.adrenalinelife.utils.StaticData;
import com.adrenalinelife.utils.Utils;
import com.adrenalinelife.web.WebHelper;

/**
 * The Class Register is the Activity class that is launched when the user
 * clicks on Register button in Login screen and it allow user to register him
 * self as a new user of this app.
 */
public class Register extends CustomActivity
{

	/* (non-Javadoc)
	 * @see com.adrenalinelife.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_user);

		setTouchNClick(R.id.button_reg_cancel);
		setTouchNClick(R.id.button_reg_submit);

		getActionBar().setTitle(R.string.register);
	}

	/* (non-Javadoc)
	 * @see com.adrenalinelife.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getId() == R.id.button_reg_submit)
		{
			Log.e("doRegister");
			doRegister();
		}
		else
		{
			finish();
		}
	}

	/**
	 * Call the Register API and check user's Login details and based on the API
	 * response, take required action or show error message if any.
	 */

	/*
	$name, $user_login, $login_name, $email,  $pwd, $address, $city, $state, $zip, $country, $phone
	 */


	private void doRegister()
	{
		Log.e("Doing the Register Method");

		////////////////////////////////////////////////////////////////////////////////////////////////
		final String first_name = ((EditText) findViewById(R.id.reg_first_name)).getText()
			.toString().trim();
		final String last_name = ((EditText) findViewById(R.id.reg_last_name)).getText()
				.toString().trim();
		final String name = first_name + " " + last_name;

		final String login_name = ((EditText) findViewById(R.id.reg_username)).getText()
				.toString().trim();
		final String user_login = ((EditText) findViewById(R.id.reg_username)).getText()
				.toString().trim().toLowerCase();
		final String email = ((EditText) findViewById(R.id.reg_email)).getText()
				.toString().trim();
		final String pwd = ((EditText) findViewById(R.id.reg_pwd)).getText()
				.toString().trim();
		final String cpwd = ((EditText) findViewById(R.id.reg_con_pwd)).getText()
				.toString().trim();

/*
	$address, $city, $state, $zip, $country, $phone
	 */


		final String address = ((EditText) findViewById(R.id.reg_address)).getText()
				.toString().trim();
		final String city = ((EditText) findViewById(R.id.reg_city)).getText()
				.toString().trim();
		final String state = ((EditText) findViewById(R.id.reg_state)).getText()
				.toString().trim();
		final String zip = ((EditText) findViewById(R.id.reg_zip)).getText()
				.toString().trim();
		final String country = ((EditText) findViewById(R.id.reg_country)).getText()
				.toString().trim();
		final String phone = ((EditText) findViewById(R.id.reg_phone)).getText()
				.toString().trim();




		////////////////////////////////////////////////////////////////////////////////////////////////
		/*

		if (Commons.isEmpty(first_name) || Commons.isEmpty(first_name)
				|| Commons.isEmpty(first_name))
		{
			Utils.showDialog(THIS, R.string.err_field_empty);
			return;
		}
		if (Commons.isEmpty(last_name) || Commons.isEmpty(last_name)
				|| Commons.isEmpty(last_name))
		{
			Utils.showDialog(THIS, R.string.err_field_empty);
			return;
		}
		if (Commons.isEmpty(login_name) || Commons.isEmpty(login_name)
				|| Commons.isEmpty(login_name))
		{
			Utils.showDialog(THIS, R.string.err_field_empty);
			return;
		}
		if (!Utils.isValidEmail(email))
		{
			Utils.showDialog(THIS, R.string.err_email);
			return;
		}
		if (!(pwd).equals(cpwd))
		{
			Utils.showDialog(THIS, R.string.err_pwd);
			return;
		}
		*/

		////////////////////////////////////////////////////////////////////////////////////////////////
		final ProgressDialog dia = showProgressDia(R.string.alert_wait);
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				Log.e("Running WebHelper Do Reg", name);
				final Status st = WebHelper.doRegister();
				runOnUiThread(new Runnable() {
					@Override
					public void run()
					{
						dia.dismiss();
						if (!st.isSuccess())
							Utils.showDialog(THIS, st.getMessage());
						else
						{
							StaticData.pref.edit()
									.putString(Const.USER_ID, st.getData())
									.apply();
							setResult(RESULT_OK);
							finish();
						}
					}
				});
			}
		}).start();
	}

}
