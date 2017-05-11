package com.adrenalinelife;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.model.Status;
import com.adrenalinelife.utils.Commons;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.StaticData;
import com.adrenalinelife.utils.Utils;
import com.adrenalinelife.web.WebHelper;


public class CreateEvent extends CustomActivity {
    //This is a blank comment for VCS testing.

    public void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        getActionBar().setTitle(R.string.create_event);



        setTouchNClick(R.id.submitEventBtn);

    }



    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.submitEventBtn)
        {
            doCreateEvent();
        }
        else
        {

        }
    }

    public void doCreateEvent()
    {

        // Get Event Name
        final String event_name = ((EditText) findViewById(R.id.eventName)).getText()
                .toString().trim();

        if (Commons.isEmpty(event_name) || Commons.isEmpty(event_name)
                || Commons.isEmpty(event_name))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }

        final String event_info = ((EditText) findViewById(R.id.eventDetails)).getText().toString().trim();

        // Get Event Dates

        final String start_date = ((EditText) findViewById(R.id.startDate)).getText()
                .toString().trim();
        final String start_time = ((EditText) findViewById(R.id.startTime)).getText()
                .toString().trim();
        final String end_date = ((EditText) findViewById(R.id.endDate)).getText()
                .toString().trim();
        final String end_time = ((EditText) findViewById(R.id.endTime)).getText()
                .toString().trim();
        if (Commons.isEmpty(start_date) || Commons.isEmpty(start_date)
                || Commons.isEmpty(start_date))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }
        if (Commons.isEmpty(start_time) || Commons.isEmpty(start_time)
                || Commons.isEmpty(start_time))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }
        if (Commons.isEmpty(end_date) || Commons.isEmpty(end_date)
                || Commons.isEmpty(end_date))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }
        if (Commons.isEmpty(end_time) || Commons.isEmpty(end_time)
                || Commons.isEmpty(end_time))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }


        // Get Location Info
        final String location_name = ((EditText) findViewById(R.id.locationName)).getText()
                .toString().trim();

        final String location_address = ((EditText) findViewById(R.id.locationAddress)).getText()
                .toString().trim();

        final String location_city = ((EditText) findViewById(R.id.locationCity)).getText()
                .toString().trim();

        final String location_state = ((Spinner)findViewById(R.id.locationState)).getSelectedItem().toString();

        final String location_zip = ((EditText) findViewById(R.id.locationZip)).getText()
                .toString().trim();

        if (Commons.isEmpty(location_name) || Commons.isEmpty(location_name)
                || Commons.isEmpty(location_name))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }
        if (Commons.isEmpty(location_address) || Commons.isEmpty(location_address)
                || Commons.isEmpty(location_address))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }
        if (Commons.isEmpty(location_city) || Commons.isEmpty(location_city)
                || Commons.isEmpty(location_city))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }

        if (Commons.isEmpty(location_zip) || Commons.isEmpty(location_zip)
                || Commons.isEmpty(location_zip))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }
        final String user = Const.USER_ID;
        final String category = "Test";

        final ProgressDialog dia = showProgressDia(R.string.alert_wait);
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                final Status st = WebHelper.doCreateEvent(location_name, location_address, location_city, location_zip, location_state, category, user, event_name, event_info, start_time, end_time, start_date, end_date);
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
