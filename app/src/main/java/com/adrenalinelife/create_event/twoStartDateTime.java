package com.adrenalinelife.create_event;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.adrenalinelife.R;
import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.utils.Commons;
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
    public NumberPicker mNumberPicker;

    //Bundle Variables
    public String mEventName;
    public String mDescription;
    public String mEventCategory;
    public String mStartDatePicker;
    public String mStartTimePicker;

    public String hour2;
    public String min2;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.M)
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
        mDescription = mBundleIn.getString("Description");
        mEventCategory = mBundleIn.getString("Event_Category");
        Log.e("Event Name = ", mEventName);
        Log.e("Event Category = ", mEventCategory);

        mTimePicker = (TimePicker) findViewById(R.id.time_picker1);
        mTimePicker.setHour(12);

        //Could be causing issue here!!/////////////////////////////////////////////////////////
        //mNumberPicker = (NumberPicker) findViewById(R.id.time_picker1);
       // mTimePicker.setDisplayedValues(new String[]{"0", "15", "30", "45"});
        //mTimePicker.setMinute(00);

        mDatePicker= (DatePicker) findViewById(R.id.date_picker2);

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
        int hour = mTimePicker.getHour();
        int min = mTimePicker.getMinute();
        timeToString(hour, min);
        Log.e("Time Picker = ", mStartTimePicker);

        //Date Picker
        int day = mDatePicker.getDayOfMonth();
        int month = mDatePicker.getMonth();
        int year = mDatePicker.getYear() - 1900;
        Log.e("Day = ", day);
        Log.e("Month = ", month);
        Log.e("Year = ", year);

        //Format Date to Correct String
        SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date(year, month, day);
        String strDate = dateFormatter2.format(d);
        mStartDatePicker = strDate;
        Log.e("Date Picker = ", mStartDatePicker);

        //Bundle up all the info for the final POST Request
        mBundleOut = new Bundle();
        mBundleOut.putString("Event_Name", mEventName);
        mBundleOut.putString("Description", mDescription);
        mBundleOut.putString("Event_Category", mEventCategory);
        mBundleOut.putString("Start_Time", mStartTimePicker);
        mBundleOut.putString("Start_Date", mStartDatePicker);

        //Build Intent, Send Intent to Next Page
        mIntentOut = new Intent(twoStartDateTime.this, twoEndDateTime.class);
        mIntentOut.putExtras(mBundleOut);
        Log.e("Bundle = ", mBundleOut);
        startActivity(mIntentOut);

    }

    public String timeToString(int hour, int min){

        if (min < 10 && hour < 10){
            min2 = "0" + min;
            hour2 = "0" + hour;
            mStartTimePicker = hour2 + ":" + min2 + ":" + "00";
        }
        else if (hour < 10 && min >= 10){
            hour2 = "0" + hour;
            mStartTimePicker = hour2 + ":" + min + ":" + "00";
        }
        else if (hour >= 10 && min < 10){
            min2 = "0" + min;
            mStartTimePicker = hour + ":" + min2 + ":" + "00";
        }
        else if (hour >= 10 && min >= 10){
            mStartTimePicker = hour + ":" + min + ":" + "00";
        }
        return mStartTimePicker;
    }
}
