package com.adrenalinelife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.StaticData;


/**
 * Created by Josh on 1/23/2017.
 */

public class Settings extends CustomActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        if (StaticData.pref.contains(Const.USER_ID)) {
            setTouchNClick(R.id.Logout);

        } else {

            setTouchNClick(R.id.Login);

        }

        getActionBar().setTitle(R.string.settings);
    }

    public void onClick(View v) {
        super.onClick(v);

        if (v.getId() == R.id.Logout) {
            Intent intent = new Intent(THIS, Logout.class);
            startActivity(intent);
        }
        else{Intent intent = new Intent(THIS, Login.class);
            startActivity(intent);}
    }
}
