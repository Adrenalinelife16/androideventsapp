package com.adrenalinelife.create_event;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adrenalinelife.Login;
import com.adrenalinelife.MainActivity;
import com.adrenalinelife.R;
import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.model.Status;
import com.adrenalinelife.ui.Events;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.Log;
import com.adrenalinelife.utils.Utils;
import com.adrenalinelife.web.WebHelper;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class reviewCreateEvent extends CustomActivity {



    //Bundle Variables
    public String mEventName;
    public String mDescription;
    public String mEventCategory;
    public String mEndDatePicker;
    public String mEndTimePicker;
    public String mStartDatePicker;
    public String mStartTimePicker;
    public String mLocation;
    public String mAddress;
    public String mCity;
    public String mZip;
    public String mState;
    public String mUser;
    public Bitmap mImage;
    public String mImageString;
    public String mBaseString;
    public Uri mImageUri;

    public Bundle mBundleIn;

    //Bundle Spots for Review
    //public TextView rEventName;
    public TextView rCategory;
    public TextView rStart;
    public TextView rEnd;
    public TextView rLocationName;
    public TextView rAddress;
    public TextView rCity;
    public TextView rState;
    public TextView rZip;
    public TextView rDetails;

    public String cHour;
    public String cMin;

    private static int PICK_EVENT_PHOTO = 1;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_create_event);

        setTouchNClick(R.id.submitEventButton);
        getActionBar().setTitle("Edit");

        //Grab Bundle IN from Previous Page
        mBundleIn = getIntent().getExtras();
        mEventName = mBundleIn.getString("Event_Name");
        mDescription = mBundleIn.getString("Description");
        mEventCategory = mBundleIn.getString("Event_Category");
        mStartTimePicker = mBundleIn.getString("Start_Time");
        mStartDatePicker = mBundleIn.getString("Start_Date");
        mEndTimePicker = mBundleIn.getString("End_Time");
        mEndDatePicker = mBundleIn.getString("End_Date");
        mLocation = mBundleIn.getString("Location");
        mAddress = mBundleIn.getString("Address");
        mCity = mBundleIn.getString("City");
        mState = mBundleIn.getString("State");
        mZip = mBundleIn.getString("Zip");
        mImageString = mBundleIn.getString("Image");

        //Set all Variables to their spot
        TextView rEventName = (TextView) findViewById(R.id.re_eventname);
        rEventName.setText(mEventName);

        rCategory = (TextView) findViewById(R.id.re_category);
        rCategory.setText(mEventCategory);

        //Format Start Time and Date to Correct String
        SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm:ss - yyyy-MM-dd");
        String f = mStartTimePicker + " - " + mStartDatePicker;
        Date d = new Date();
        try {
            d = dateFormatter.parse(f);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("EEEE h:mm a - MMM dd, yyyy");
        String n = timeFormat.format(d);
        rStart = (TextView) findViewById(R.id.re_start);
        rStart.setText(n);
        Log.e("Formatted Start Time and Date", n);


        //Format End Time and Date to Correct String
        SimpleDateFormat dateFormatter2 = new SimpleDateFormat("hh:mm:ss - yyyy-MM-dd");
        String f2 = mEndTimePicker + " - " + mEndDatePicker;
        Date d2 = new Date();
        try {
            d2 = dateFormatter2.parse(f2);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat2 = new SimpleDateFormat("EEEE h:mm a - MMM dd, yyyy");
        String n2 = timeFormat2.format(d2);
        rEnd = (TextView) findViewById(R.id.re_end);
        rEnd.setText(n2);
        Log.e("Formatted End Time and Date", n2);


        rLocationName = (TextView) findViewById(R.id.re_locationname);
        rLocationName.setText(mLocation);

        rAddress = (TextView) findViewById(R.id.re_address);
        rAddress.setText(mAddress);

        rCity = (TextView) findViewById(R.id.re_city);
        rCity.setText(mCity);

        rState = (TextView) findViewById(R.id.re_state);
        rState.setText(mState);

        rZip = (TextView) findViewById(R.id.re_zip);
        rZip.setText(mZip);

        rDetails = (TextView) findViewById(R.id.re_details);
        rDetails.setText(mDescription);

        if (mImageString != null){

            //Convert Image String to Uri
            mImageUri = Uri.parse(mImageString);

            ImageView imageView = (ImageView) findViewById(R.id.imageReview);
            imageView.setImageResource(0);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(mImageUri);
                Log.e("bmp", bmp);
                saveImage(bmp);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            imageView.setImageBitmap(bmp);

            //Convert Selected Image to Base64 String
            pickImage();
        }
    }

    //Helping setup image for ImageView
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();

        return image;
    }

    //Helping Setup image for ImageView
    public Bitmap saveImage(Bitmap image)
    {
        mImage = image;

        return mImage;
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.submitEventButton)
        {
            doCreateEvent();
        }
    }

    public void doCreateEvent() {


        // Get & Pass User ID
        mUser = Const.USER_ID;

        //mLocation, mAddress, mCity, mZip, mState, mEventCategory, mUser, mEventName, mDescription, tStartTimePicker, tEndTimePicker, tStartDatePicker, tEndDatePicker
        final ProgressDialog dia = showProgressDia(R.string.alert_wait);
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                Log.e("Running doCreateEvent");

                final Status st = WebHelper.doCreateEvent(mBaseString);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        dia.dismiss();
                        if (!st.isSuccess()) {
                            Utils.showEventCreatedDialog(reviewCreateEvent.this, "Event Creation Successful");
                            // Launch Events Fragment
                            //Intent i = new Intent(reviewCreateEvent.this, Events.class);
                            //startActivity(i);

                            /*
                            AlertDialog.Builder builder = new AlertDialog.Builder(reviewCreateEvent.this);
                            builder.setMessage( "Event Creation Successful")
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(reviewCreateEvent.this, Events.class);
                                            startActivity(intent);
                                        }
                                    });

                            AlertDialog action = builder.create();
                            action.show();
                            */
                        }

                        else
                        {
                            Utils.showEventCreatedDialog(reviewCreateEvent.this, "Event Creation Unsuccessful!");
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });
            }
        }).start();
    }
    //Select and Convert Image for Upload
    public void pickImage(){

        Uri.parse(mImageString);
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(mImageUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        mImageString = cursor.getString(columnIndex);
        cursor.close();
        mBaseString = Utils.getBase64ImageString(mImageString);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(reviewCreateEvent.this, "You just pressed back button",Toast.LENGTH_SHORT).show();
    }


}
