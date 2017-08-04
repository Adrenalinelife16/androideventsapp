package com.adrenalinelife.create_event;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.adrenalinelife.R;
import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.utils.Commons;
import com.adrenalinelife.utils.Log;
import com.adrenalinelife.utils.Utils;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class editCreateEvent extends CustomActivity {

    public Bundle mBundleIn;
    public Bundle mBundleOut;
    public Intent mIntentOut;
    public String mImageUri;


    //Bundle Variables
    public Spinner sCategorySpinner;
    public TextView tEndDatePicker;
    public TextView tEndTimePicker;
    public TextView tStartDatePicker;
    public TextView tStartTimePicker;


    //Strings to Parameters
    public String mEventName;
    public String mCategory;
    public String mDescription;
    public String mEndDatePicker;
    public String mEndTimePicker;
    public String mStartDatePicker;
    public String mStartTimePicker;
    public String mLocation;
    public String mAddress;
    public String mCity;
    public String mZip;
    public String mState;
    public Bitmap mImage;
    public String mImageString;
    public String mBaseString;

    private static int PICK_EVENT_PHOTO = 1;

    //DatePickerDialoge Setup
    Calendar mCurrentDate;
    Calendar mCurrentDate2;
    int day, month, year, hour, min;
    //End Date too
    Calendar mCurrentDate3;
    Calendar mCurrentDate4;
    int day2, month2, year2, hour2, min2;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_create_event);

        getActionBar().setTitle(R.string.create_event);
        setTouchNClick(R.id.submitEventBtn);
        setTouchNClick(R.id.chooseImageBtn);
        setTouchNClick(R.id.submittedImageView);

        //Start Date////////////////////////////////////////////////////////////////////////////////
        tStartDatePicker = (TextView) findViewById(R.id.start_date);
        mCurrentDate = Calendar.getInstance();
        day = mCurrentDate.get(Calendar.DAY_OF_MONTH);
        month = mCurrentDate.get(Calendar.MONTH);
        year = mCurrentDate.get(Calendar.YEAR);
        tStartDatePicker.setText("Set Date");

        tStartDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(editCreateEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @TargetApi(Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int month2 = month + 1;
                        tStartDatePicker.setText(month2 + "/" + dayOfMonth + "/" + year);
                        //Format Date to Correct String
                        SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd");
                        year = year - 1900;
                        Date d = new Date(year, month, dayOfMonth);
                        String strDate = dateFormatter2.format(d);
                        mStartDatePicker = strDate;
                        Log.e("Start Date", mStartDatePicker);
                    }
                }, year, month, day);
                dpd.show();
            }
        });
        //Start Date////////////////////////////////////////////////////////////////////////////////

        //Start Time////////////////////////////////////////////////////////////////////////////////
        tStartTimePicker = (TextView) findViewById(R.id.start_time);
        mCurrentDate2 = Calendar.getInstance();
        hour = mCurrentDate2.get(Calendar.HOUR_OF_DAY);
        min = mCurrentDate2.get(Calendar.MINUTE);
        tStartTimePicker.setText("Set Time");

        tStartTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(editCreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @TargetApi(Build.VERSION_CODES.N)
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        //Format End Time and Date to Correct String
                        SimpleDateFormat dateF = new SimpleDateFormat("h:mm");
                        String f2 = hourOfDay + ":" + minute;
                        Date d2 = new Date();
                        try {
                            d2 = dateF.parse(f2);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat timeFormat2 = new SimpleDateFormat("h:mm a");
                        String n2 = timeFormat2.format(d2);

                        tStartTimePicker.setText(n2);
                        startTimeToString(hourOfDay, minute);
                        Log.e("Start Time", mStartTimePicker);
                    }
                }, hour, min, false);
                tpd.show();
            }
        });
        //Start Time////////////////////////////////////////////////////////////////////////////////

        //End Date//////////////////////////////////////////////////////////////////////////////////
        tEndDatePicker = (TextView) findViewById(R.id.end_date);
        mCurrentDate3 = Calendar.getInstance();
        day2 = mCurrentDate3.get(Calendar.DAY_OF_MONTH);
        month2 = mCurrentDate3.get(Calendar.MONTH);
        year2 = mCurrentDate3.get(Calendar.YEAR);
        tEndDatePicker.setText("Set Date");

        tEndDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd2 = new DatePickerDialog(editCreateEvent.this, new DatePickerDialog.OnDateSetListener() {
                    @TargetApi(Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int month2 = month + 1;
                        tEndDatePicker.setText(month2 + "/" + dayOfMonth + "/" + year);
                        //Format Date to Correct String
                        SimpleDateFormat dateFormatter2 = new SimpleDateFormat("yyyy-MM-dd");
                        year = year - 1900;
                        Date d = new Date(year, month, dayOfMonth);
                        String strDate = dateFormatter2.format(d);
                        mEndDatePicker = strDate;
                        Log.e("End Date", mEndDatePicker);
                    }
                }, year2, month2, day2);
                dpd2.show();
            }
        });
        //End Date//////////////////////////////////////////////////////////////////////////////////

        //End Time//////////////////////////////////////////////////////////////////////////////////
        tEndTimePicker = (TextView) findViewById(R.id.end_time);
        mCurrentDate4 = Calendar.getInstance();
        hour2 = mCurrentDate4.get(Calendar.HOUR_OF_DAY);
        min2 = mCurrentDate4.get(Calendar.MINUTE);
        tEndTimePicker.setText("Set Time");

        tEndTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(editCreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @TargetApi(Build.VERSION_CODES.N)
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        //Format End Time and Date to Correct String
                        SimpleDateFormat dateF = new SimpleDateFormat("h:mm");
                        String f2 = hourOfDay + ":" + minute;
                        Date d2 = new Date();
                        try {
                            d2 = dateF.parse(f2);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat timeFormat2 = new SimpleDateFormat("h:mm a");
                        String n2 = timeFormat2.format(d2);

                        tEndTimePicker.setText(n2);
                        endTimeToString(hourOfDay, minute);
                        Log.e("End Time", mEndTimePicker);
                    }
                }, hour2, min2, false);
                tpd.show();
            }
        });
        //End Time//////////////////////////////////////////////////////////////////////////////////


    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.submitEventBtn)
        {
            try {
                doCreateEvent();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (v.getId() == R.id.chooseImageBtn || v.getId() == R.id.submittedImageView)
        {
            chooseEventImage();
        }
        else
        {

        }
    }

    public void chooseEventImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_EVENT_PHOTO);

    }

    public void doCreateEvent() throws FileNotFoundException {

        //Event Name
        mEventName = ((EditText) findViewById(R.id.event_name)).getText()
                .toString().trim();
        //Category
        sCategorySpinner = (Spinner) findViewById(R.id.cate_spinner);
        mCategory = sCategorySpinner.getSelectedItem().toString();
        //Event Details
        mDescription = ((EditText) findViewById(R.id.event_details)).getText()
                .toString().trim();

        // Get Location Info
        mLocation = ((EditText) findViewById(R.id.locationName)).getText()
                .toString().trim();

        mAddress = ((EditText) findViewById(R.id.locationAddress)).getText()
                .toString().trim();

        mCity = ((EditText) findViewById(R.id.locationCity)).getText()
                .toString().trim();

        mState = ((Spinner)findViewById(R.id.locationState)).getSelectedItem().toString();

        mZip = ((EditText) findViewById(R.id.locationZip)).getText()
                .toString().trim();


        //Check if Text is empty////////////////////////////////////////////////////////////////////
        if (Commons.isEmpty(mDescription))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }

        if (Commons.isEmpty(mEventName))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }

        if (Commons.isEmpty(mLocation))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }
        if (Commons.isEmpty(mAddress))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }
        if (Commons.isEmpty(mCity))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }

        if (Commons.isEmpty(mState))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }

        if (mStartDatePicker == null)
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }

        if (mStartTimePicker == null)
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }

        if (mEndDatePicker == null)
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }

        if (mEndTimePicker == null)
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }

        if (mCategory.equals("Select a Category"))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////

        //Bundle up all the info for the final POST Request
        mBundleOut = new Bundle();
        mBundleOut.putString("Event_Name", mEventName);
        mBundleOut.putString("Description", mDescription);
        mBundleOut.putString("Event_Category", mCategory);
        mBundleOut.putString("Start_Time", mStartTimePicker);
        mBundleOut.putString("Start_Date", mStartDatePicker);
        mBundleOut.putString("End_Time", mEndTimePicker);
        mBundleOut.putString("End_Date", mEndDatePicker);
        mBundleOut.putString("Location", mLocation);
        mBundleOut.putString("Address", mAddress);
        mBundleOut.putString("City", mCity);
        mBundleOut.putString("State", mState);
        mBundleOut.putString("Zip", mZip);
        mBundleOut.putString("Image", mImageUri);

        //Build Intent, Send Intent to Next Page
        mIntentOut = new Intent(editCreateEvent.this, reviewCreateEvent.class);
        mIntentOut.putExtras(mBundleOut);
        Log.e("Bundle = ", mBundleOut);
        startActivity(mIntentOut);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_EVENT_PHOTO && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            mImageUri = selectedImage.toString();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mImageString = cursor.getString(columnIndex);
            cursor.close();

            //Convert Selected Image to Base64 String
            mBaseString = Utils.getBase64ImageString(mImageString);

            Log.e("column index cursor = ", mImageString);
            Log.e("Selected Image", selectedImage);
            Log.e("file to path", filePathColumn);


            ImageView imageView = (ImageView) findViewById(R.id.submittedImageView);
            imageView.setImageResource(0);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
                Log.e("bmp", bmp);
                saveImage(bmp);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            imageView.setImageBitmap(bmp);

        }
        if (resultCode == 1) {


        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();

        return image;
    }

    public Bitmap saveImage(Bitmap image)
    {
        mImage = image;

        return mImage;
    }


    public String startTimeToString(int hour, int min){

        if (min < 10 && hour < 10){
            String min2 = "0" + min;
            String hour2 = "0" + hour;
            mStartTimePicker = hour2 + ":" + min2 + ":" + "00";
        }
        else if (hour < 10 && min >= 10){
            String hour2 = "0" + hour;
            mStartTimePicker = hour2 + ":" + min + ":" + "00";
        }
        else if (hour >= 10 && min < 10){
            String min2 = "0" + min;
            mStartTimePicker = hour + ":" + min2 + ":" + "00";
        }
        else if (hour >= 10 && min >= 10){
            mStartTimePicker = hour + ":" + min + ":" + "00";
        }
        return mStartTimePicker;
    }

    public String endTimeToString(int hour, int min){

        if (min < 10 && hour < 10){
            String min2 = "0" + min;
            String hour2 = "0" + hour;
            mEndTimePicker = hour2 + ":" + min2 + ":" + "00";
        }
        else if (hour < 10 && min >= 10){
            String hour2 = "0" + hour;
            mEndTimePicker = hour2 + ":" + min + ":" + "00";
        }
        else if (hour >= 10 && min < 10){
            String min2 = "0" + min;
            mEndTimePicker = hour + ":" + min2 + ":" + "00";
        }
        else if (hour >= 10 && min >= 10){
            mEndTimePicker = hour + ":" + min + ":" + "00";
        }
        return mEndTimePicker;
    }
}

