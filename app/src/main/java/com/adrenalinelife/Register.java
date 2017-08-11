package com.adrenalinelife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.model.Status;
import com.adrenalinelife.utils.Commons;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.Log;
import com.adrenalinelife.utils.Utils;
import com.adrenalinelife.web.WebHelper;
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
		mPwd2 = ((EditText) findViewById(R.id.reg_con_pwd)).getText()
				.toString().trim();
		////////////////////////////////////////////////////////////////////////////////////////////////
		if (Commons.isEmpty(mFirst))
		{
			Utils.showDialog(THIS, R.string.err_field_empty);
			return;
		}
		if (Commons.isEmpty(mLast))
		{
			Utils.showDialog(THIS, R.string.err_field_empty);
			return;
		}
		if (Commons.isEmpty(mUserName))
		{
			Utils.showDialog(THIS, R.string.err_field_empty);
			return;
		}
		if (!Utils.isValidEmail(mEmail))
		{
			Utils.showDialog(THIS, R.string.err_email);
			return;
		}
		if (!(mPwd).equals(mPwd2))
		{
			Utils.showDialog(THIS, R.string.err_pwd);
			return;
		}
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
							//Create Bundle for Login
							mBundle = new Bundle();
							mBundle.putString("email", mNiceName);
							mBundle.putString("pwd", mPwd);
							//Package Bundle into Intent, Send to auto login class
							mIntent = new Intent(Register.this, AutoLogin.class);
							mIntent.putExtras(mBundle);
							Log.e("Bundle = ", mIntent);
							startActivity(mIntent);
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
