package com.adrenalinelife;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.model.Status;
import com.adrenalinelife.utils.Commons;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.StaticData;
import com.adrenalinelife.utils.Utils;
import com.adrenalinelife.web.WebHelper;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import io.fabric.sdk.android.Fabric;

/**
 * The Class Login is the Activity class that is launched when the app require
 * user to Login to perform next operations like in this current we ask user to
 * Login before he/she is going to Book Event ticket and once the user is logged
 * in, it will never ask to loin again for the lifetime of this app.
 */
public class Login extends CustomActivity
{
	public String mEmail;
	public String mPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		/** Fabric Initializing **/
		Fabric.with(this, new Answers());
		Fabric.with(this, new Crashlytics());
		final Fabric fabric = new Fabric.Builder(this)
				.kits(new Crashlytics())
				.debuggable(true)
				.build();
		Fabric.with(fabric);

		setTouchNClick(R.id.button_login_submit);
		setTouchNClick(R.id.button_reg);

		getActionBar().setTitle(R.string.login);
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getId() == R.id.button_reg)
		{
			startActivityForResult(new Intent(THIS, Register.class),
					Const.REQ_LOGIN);
		}
		else
		{
			mEmail = ((EditText) findViewById(R.id.txtEmail)).getText()
					.toString();
			mPwd = ((EditText) findViewById(R.id.txtPwd)).getText()
					.toString();
			doLogin();
		}
	}

	/**
	 * Call the Login API and check user's Login details and based on the API
	 * response, take required action or show error message if any.
	 */
	private void doLogin()
	{
		if (mEmail.length() < 3)
		{
			Utils.showDialog(THIS, R.string.err_field_empty);
			return;
		}

		if (mEmail.contains("@"))
		{
			Utils.showDialog(THIS, "Please Login with your Username!");
			return;
		}

		if (Commons.isEmpty(mPwd))
		{
			Utils.showDialog(THIS, R.string.err_field_empty);
			return;
		}

		final ProgressDialog dia = showProgressDia(R.string.alert_login);
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				final Status st = WebHelper.doLogin(mEmail, mPwd);
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
							Log.v("User_ID", StaticData.User_iD);

							/** Fabric **/
							Answers.getInstance().logCustom(new CustomEvent("User_Logged_In"));

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
