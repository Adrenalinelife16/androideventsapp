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
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.StaticData;
import com.adrenalinelife.utils.Utils;
import com.adrenalinelife.web.WebHelper;

/**
 * Created by Chazz Romeo on 7/10/2017.
 */
public class AutoLogin extends CustomActivity
{

	public Bundle mBundle;

	public String mEmail;
	public String mPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		setTouchNClick(R.id.button_login_submit);
		setTouchNClick(R.id.button_reg);

		getActionBar().setTitle(R.string.login);

		mBundle = getIntent().getExtras();
		mEmail = mBundle.getString("email");
		mPwd = mBundle.getString("pwd");
		doLogin();
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
