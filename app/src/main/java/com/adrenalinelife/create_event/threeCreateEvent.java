package com.adrenalinelife.create_event;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
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
import java.io.IOException;
import java.util.Date;

public class threeCreateEvent extends CustomActivity {

    public Bundle mBundleIn;
    public Bundle mBundleOut;
    public Intent mIntentOut;
    public String mImageUri;

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
        Log.e("Start Time = ", mStartTimePicker);
        Log.e("End Time = ", mEndTimePicker);
        Log.e("Start Date = ", mStartDatePicker);
        Log.e("End Date = ", mEndDatePicker);

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

        /*

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
*/

        //Bundle up all the info for the final POST Request
        mBundleOut = new Bundle();
        mBundleOut.putString("Event_Name", mEventName);
        mBundleOut.putString("Description", mDescription);
        mBundleOut.putString("Event_Category", mEventCategory);
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
        mIntentOut = new Intent(threeCreateEvent.this, fourFinalize.class);
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


}

