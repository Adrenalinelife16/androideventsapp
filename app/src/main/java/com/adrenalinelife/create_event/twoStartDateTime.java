package com.adrenalinelife.create_event;

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.adrenalinelife.R;
import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.utils.Log;

import java.util.Date;

public class twoStartDateTime extends CustomActivity {

    //Bundles IN and OUT
    public Bundle mBundleIn;
    public Bundle mBundleOut;

    //Intent OUT
    public Intent mIntentOut;

    //Date and Time Pickers
    public DatePicker mDatePicker;
    public TimePicker mTimePicker;

    //Bundle Variables
    public String mEventName;
    public String mEventCategory;
    public String mEndDatePicker;
    public String mEndTimePicker;
    public String mStartDatePicker;
    public String mStartTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_date_time);
        setTouchNClick(R.id.two_date_time_button);

        getActionBar().setTitle("SET: Start Date and Time");

        //Grab Bundle IN from Previous Page
        //Set Variables for Bundle OUT
        mBundleIn = getIntent().getExtras();
        mEventName = mBundleIn.getString("Event_Name");
        mEventCategory = mBundleIn.getString("Event_Category");
        Log.e("Event Name = ", mEventName);
        Log.e("Event Category = ", mEventCategory);

    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.two_date_time_button)
        {
            oneCreateEvent();
        }
        else
        {

        }
    }
    @TargetApi(Build.VERSION_CODES.N)
    public void oneCreateEvent() {


        //Time Picker
        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker1);
        int hour = timePicker.getHour();
        int min = timePicker.getMinute();
        mStartTimePicker = hour + ":" + min;
        Log.e("Time Picker = ", mStartTimePicker);

        //Date Picker
        DatePicker datePicker= (DatePicker) findViewById(R.id.date_picker2);
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear() - 1900;
        Log.e("Day = ", day);
        Log.e("Month = ", month);
        Log.e("Year = ", year);

        //Format Date to Correct String
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(year, month, day);
        String strDate = dateFormatter.format(d);
        mStartDatePicker = strDate;
        Log.e("Date Picker = ", mStartDatePicker);

        //Bundle up all the info for the final POST Request
        mBundleOut = new Bundle();
        mBundleOut.putString("Event_Name", mEventName);
        mBundleOut.putString("Event_Category", mEventCategory);
        mBundleOut.putString("Start_Time", mStartTimePicker);
        mBundleOut.putString("Start_Date", mStartDatePicker);

        //Build Intent, Send Intent to Next Page
        mIntentOut = new Intent(twoStartDateTime.this, twoStartDateTime.class);
        mIntentOut.putExtras(mBundleOut);
        Log.e("Bundle = ", mBundleOut);
        startActivity(mIntentOut);

    }





}
