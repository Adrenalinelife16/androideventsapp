package com.adrenalinelife;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.model.Status;
import com.adrenalinelife.utils.Commons;
import com.adrenalinelife.utils.Const;
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
		setContentView(R.layout.register);

		setTouchNClick(R.id.btnCancel);
		setTouchNClick(R.id.btnReg);

		getActionBar().setTitle(R.string.register);
	}

	/* (non-Javadoc)
	 * @see com.adrenalinelife.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if (v.getId() == R.id.btnReg)
		{
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
		final String first_name = ((EditText) findViewById(R.id.FName)).getText()
			.toString().trim();
		final String last_name = ((EditText) findViewById(R.id.LName)).getText()
				.toString().trim();
		final String name = first_name + " " + last_name;

		final String login_name = ((EditText) findViewById(R.id.Username)).getText()
				.toString().trim();
		final String user_login = ((EditText) findViewById(R.id.Username)).getText()
				.toString().trim();
		final String email = ((EditText) findViewById(R.id.txtEmail)).getText()
				.toString().trim();
		final String pwd = ((EditText) findViewById(R.id.txtPwd)).getText()
				.toString().trim();
		final String cpwd = ((EditText) findViewById(R.id.ctxtPwd)).getText()
				.toString().trim();

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
		if (Commons.isEmpty(user_login) || Commons.isEmpty(user_login)
				|| Commons.isEmpty(user_login))
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
		final ProgressDialog dia = showProgressDia(R.string.alert_wait);
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				final Status st = WebHelper.doRegister(name, login_name, user_login, email, pwd);
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
