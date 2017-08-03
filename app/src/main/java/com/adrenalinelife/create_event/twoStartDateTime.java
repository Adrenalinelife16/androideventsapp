package com.adrenalinelife.create_event;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.adrenalinelife.R;
import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.utils.Log;


import java.util.Calendar;
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

    public TextView mSetTime;
    public TextView mSetDate;

    //DatePickerDialoge Setup
    Calendar mCurrentDate;
    int day, month, year;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_date_time);
        setTouchNClick(R.id.two_date_time_button);

        getActionBar().setTitle("SET: Start Date and Time");
        mSetTime = (TextView) findViewById(R.id.setTime);
        mSetDate = (TextView) findViewById(R.id.setDate);
        //mSetTime.setText("Start Time");
        mSetDate.setText("Start Date");


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

        mDatePicker= (DatePicker) findViewById(R.id.date_picker2);

        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);
        month = month + 1;
        mSetTime.setText(day + "/" + month + "/" + year);

        mSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(twoStartDateTime.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        mSetDate.setText(dayOfMonth + "/" + month + "/" + year);

                    }
                }, year, month, day);
                dpd.show();
            }
        });


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
