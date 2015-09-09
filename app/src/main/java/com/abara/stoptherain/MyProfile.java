package com.abara.stoptherain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.abara.stoptherain.utils.PreferenceIds;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Abb on 8/29/2015.
 */
public class MyProfile extends AppCompatActivity {

    private AppCompatButton browseBtn;
    private AppCompatEditText userName;

    private BootstrapCircleThumbnail mImage;
    private TextView mUser, mDesc;
    private int count = 0;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        browseBtn = (AppCompatButton) findViewById(R.id.browse_pic);
        userName = (AppCompatEditText) findViewById(R.id.user_name_txtbox);

        mImage = (BootstrapCircleThumbnail) findViewById(R.id.user_image);
        mUser = (TextView) findViewById(R.id.user_name_text);
        mDesc = (TextView) findViewById(R.id.user_rain_details);

        setImageAndUserName();

        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                choosePicture();

            }
        });

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUser.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                prefs.edit().putString(PreferenceIds.USER_NAME_KEY, s.toString()).commit();
            }
        });

    }

    private void choosePicture() {

        Intent picker = new Intent(Intent.ACTION_PICK);
        picker.setType("image/*");
        startActivityForResult(picker, 99);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 99:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri selectedImage = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap image = BitmapFactory.decodeStream(imageStream);
                        saveIamgeToLocalStore(image);
                        mImage.setImage(image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }

    }

    private void loadImageFromLocal(String imageURI) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.parse(Environment.getExternalStorageDirectory().toString() + imageURI);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
        } else {
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));
        }

        try {
            File file = new File(Environment.getExternalStorageDirectory() + imageURI);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            mImage.setImage(bitmap);
        } catch (Exception e) {
            mImage.setImage(R.drawable.ic_no_image);
        }
    }

    private void saveIamgeToLocalStore(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + PreferenceIds.USER_IMAGE_LOCATION_PATH);
        myDir.mkdirs();
        String fname = PreferenceIds.USER_IMAGE_FILE_NAME;
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImageAndUserName() {

        mUser.setText(prefs.getString(PreferenceIds.USER_NAME_KEY, "unknown user"));
        count = prefs.getInt(PreferenceIds.USER_STOPPED_COUNT_KEY, 0);
        mDesc.setText("I have just stopped the rain " + count + (count == 1 ? " time." : " times."));
        loadImageFromLocal(PreferenceIds.USER_IMAGE_LOCATION_PATH + "/" + PreferenceIds.USER_IMAGE_FILE_NAME);

    }
}
