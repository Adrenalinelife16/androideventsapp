package com.adrenalinelife.create_event;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.adrenalinelife.R;
import com.adrenalinelife.custom.CustomActivity;
import com.adrenalinelife.model.Status;
import com.adrenalinelife.utils.Commons;
import com.adrenalinelife.utils.Const;
import com.adrenalinelife.utils.Log;
import com.adrenalinelife.utils.StaticData;
import com.adrenalinelife.utils.Utils;
import com.adrenalinelife.web.WebHelper;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class threeCreateEvent extends CustomActivity {


    public Bundle mBundleIn;

    public Intent mIntentIn;

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

    private static int PICK_EVENT_PHOTO = 1;






    public void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.three_create_event);

        getActionBar().setTitle(R.string.create_event);
        setTouchNClick(R.id.submitEventBtn);
        setTouchNClick(R.id.chooseImageBtn);
        setTouchNClick(R.id.submittedImageView);

        //Grab Bundle IN from Previous Page
        //Set Variables for Bundle OUT
        mBundleIn = getIntent().getExtras();
        mEventName = mBundleIn.getString("Event_Name");
        mDescription = mBundleIn.getString("Description");
        mEventCategory = mBundleIn.getString("Event_Category");
        mStartTimePicker = mBundleIn.getString("Start_Time");
        mStartDatePicker = mBundleIn.getString("Start_Date");
        mEndTimePicker = mBundleIn.getString("End_Time");
        mEndDatePicker = mBundleIn.getString("End_Date");

    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.submitEventBtn)
        {
            doCreateEvent();
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

    public void doCreateEvent()
    {

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



        if (Commons.isEmpty(mLocation) || Commons.isEmpty(mLocation)
                || Commons.isEmpty(mLocation))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }
        if (Commons.isEmpty(mAddress) || Commons.isEmpty(mAddress)
                || Commons.isEmpty(mAddress))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }
        if (Commons.isEmpty(mCity) || Commons.isEmpty(mCity)
                || Commons.isEmpty(mCity))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }

        if (Commons.isEmpty(mState) || Commons.isEmpty(mState)
                || Commons.isEmpty(mState))
        {
            Utils.showDialog(THIS, R.string.err_field_empty);
            return;
        }

        //Get/Pass User ID
        mUser = Const.USER_ID;

        final ProgressDialog dia = showProgressDia(R.string.alert_wait);
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                Log.e("Running doCreateEvent");
                final Status st = WebHelper.doCreateEvent(mLocation, mAddress, mCity, mZip, mState, mEventCategory, mUser, mEventName, mDescription, mStartTimePicker, mEndTimePicker, mStartDatePicker, mEndDatePicker);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_EVENT_PHOTO && resultCode == RESULT_OK && null != data) {



            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.submittedImageView);
            imageView.setImageResource(0);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            imageView.setImageBitmap(bmp);

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


}

