package com.adrenalinelife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.utils.StaticData;
import com.adrenalinelife.utils.Const;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Chazz Romeo on 7/10/2017.
 */

public class Logout extends CustomActivity {


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout);

        /** Fabric Initializing **/
        Fabric.with(this, new Answers());
        Fabric.with(this, new Crashlytics());
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);

        setTouchNClick(R.id.btnlogout);

        getActionBar().setTitle(R.string.logout);
    }

    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.btnlogout) {
            doLogout();
        }
    }

    private void doLogout()
    {


        StaticData.clearCache();
        StaticData.pref.edit()
                .putString(StaticData.User_iD, null)
                .putString(Const.USER_ID, null)
                .putString(Const.EXTRA_DATA, null)
                .putString(Const.EXTRA_DATA1, null)
                .apply();

        SharedPreferences preferences = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        setResult(RESULT_OK);

        Toast.makeText(THIS,
                R.string.logout_success,
                Toast.LENGTH_SHORT).show();

        /** Fabric **/
        Answers.getInstance().logCustom(new CustomEvent("User_Logged_Out"));

        Intent intent = new Intent(THIS, MainActivity.class);
        startActivity(intent);

    }

}




