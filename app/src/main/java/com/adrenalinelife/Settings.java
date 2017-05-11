package com.adrenalinelife;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.adrenalinelife.custom.CustomActivity;



/**
 * Created by Josh on 1/23/2017.
 */

public class Settings extends CustomActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        setTouchNClick(R.id.Logout);

        getActionBar().setTitle(R.string.settings);
    }

    public void onClick(View v) {
        super.onClick(v);

        if (v.getId() == R.id.Logout) {
            Intent intent = new Intent(THIS, Logout.class);
            startActivity(intent);
        }
        else{}
    }
}
