package com.adrenalinelife.create_event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.adrenalinelife.R;
import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.utils.Log;

/**
 * Created by Martare on 6/17/17.
 */


public class oneCreateEvent extends CustomActivity {


    public Bundle mBundle;
    public Intent mIntent;
    public Button mNextButton;
    public EditText mEventName;
    public Spinner mEventCategory;
    public String sEventName;
    public String sEventCategory;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_category);

        //getActionBar().setTitle(R.string.create_event);

        setTouchNClick(R.id.one_category_button);

    }
    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.one_category_button)
        {
            oneCreateEvent();
        }
        else
        {

        }
    }

    public void oneCreateEvent() {

        //get Event Name
        sEventName = ((EditText) findViewById(R.id.eventName)).getText().toString().trim();

        //get Event Category
        sEventCategory = ((Spinner) findViewById(R.id.category_spinner)).getSelectedItem().toString();

        mBundle = new Bundle();
        mBundle.putString("Event_Name", sEventName);
        mBundle.putString("Event_Category", sEventCategory);

        mIntent = new Intent(oneCreateEvent.this, twoStartDateTime.class);
        mIntent.putExtras(mBundle);
        Log.e("Bundle = ", mBundle);
        startActivity(mIntent);



    }




}
