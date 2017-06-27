package com.adrenalinelife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.adrenalinelife.create_event.oneCreateEvent;
import com.adrenalinelife.create_event.twoStartDateTime;
import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.model.Status;
import com.adrenalinelife.utils.Commons;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.Log;
import com.adrenalinelife.utils.StaticData;
import com.adrenalinelife.utils.Utils;
import com.adrenalinelife.web.WebHelper;

import static com.adrenalinelife.web.WebHelper.doLogin;

/**
 * The Class Register is the Activity class that is launched when the user
 * clicks on Register button in Login screen and it allow user to register him
 * self as a new user of this app.
 */
public class Register extends CustomActivity {

	public String mFirst;
	public String mLast;
	public String mName;
	public String mUserName;
	public String mNiceName;
	public String mEmail;
	public String mPwd;
	public String mPwd2;

	public Bundle mBundle;
	public Intent mIntent;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_user);

		setTouchNClick(R.id.button_reg_cancel);
		setTouchNClick(R.id.button_reg_submit);

		getActionBar().setTitle(R.string.register);
	}
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
	private void doRegister()
	{
		Log.e("Doing the Register Method");

		////////////////////////////////////////////////////////////////////////////////////////////////
		mFirst = ((EditText) findViewById(R.id.reg_first_name)).getText()
			.toString().trim();
		mLast = ((EditText) findViewById(R.id.reg_last_name)).getText()
				.toString().trim();
		mName = mFirst + " " + mLast;

		mUserName = ((EditText) findViewById(R.id.reg_username)).getText()
				.toString().trim();
		mNiceName = ((EditText) findViewById(R.id.reg_username)).getText()
				.toString().trim().toLowerCase();
		mEmail = ((EditText) findViewById(R.id.reg_email)).getText()
				.toString().trim();
		mPwd = ((EditText) findViewById(R.id.reg_pwd)).getText()
				.toString().trim();
		//final String cpwd = ((EditText) findViewById(R.id.reg_con_pwd)).getText()
		//		.toString().trim();

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

		name, user_login, login_name, email, pwd
		*/
		////////////////////////////////////////////////////////////////////////////////////////////////
		final ProgressDialog dia = showProgressDia(R.string.alert_wait);
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				Log.e("Running WebHelper Do Reg", mName);
				final Status st = WebHelper.doRegister(mName, mUserName, mNiceName, mEmail, mPwd);
				runOnUiThread(new Runnable() {
					@Override
					public void run()
					{
						dia.dismiss();
						if (!st.isSuccess())
							Utils.showDialog(THIS, st.getMessage());
						else
						{

							mBundle = new Bundle();
							mBundle.putString("email", mNiceName);
							mBundle.putString("pwd", mPwd);

							mIntent = new Intent(Register.this, AutoLogin.class);
							mIntent.putExtras(mBundle);
							Log.e("Bundle = ", mIntent);
							startActivity(mIntent);


							//StaticData.pref.edit().putString(Const.USER_ID, st.getData()).apply();
							//StaticData.pref.edit()
							//		.putString(Const.USER_ID, st.getData())
							//		.putString(StaticData.User_iD, st.getData())
							//		.apply();
							//setResult(RESULT_OK);
							//finish();

						}
					}
				});
			}
		}).start();
	}


	private void autoLogin(){

		final ProgressDialog dia = showProgressDia(R.string.alert_login);
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				final Status st = WebHelper.doLogin(mNiceName, mPwd);
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
									.putString(StaticData.User_iD, st.getData())
									.apply();
							setResult(RESULT_OK);
							android.util.Log.v("User_ID", StaticData.User_iD);
							finish();
						}
					}
				});
			}
		}).start();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Const.REQ_LOGIN && resultCode == Activity.RESULT_OK)
		{
			setResult(RESULT_OK);
			finish();
		}
	}

}
