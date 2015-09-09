package com.abara.stoptherain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abara.stoptherain.intro.MyIntro;
import com.abara.stoptherain.location.Tracker;
import com.abara.stoptherain.utils.PreferenceIds;
import com.abara.stoptherain.utils.VolleySingleTon;
import com.abara.stoptherain.utils.Weather;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String DEGREE_KEY = "degree_key";
    private static final String DETAILS_KEY = "details_key";
    private String data = "http://api.openweathermap.org/data/2.5/weather?lat=",tempData;
    private String main = "null";

    private Toolbar mAppbar;
    private Tracker mTracker;
    private Location location;
    private FloatingActionButton mFab;
    private Snackbar mSnack;

    private TextView mDegree, mDetails, mFun;
    private ImageView mFunImage;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(prefs.getBoolean(PreferenceIds.IS_FIRST__RUN,true)){
            startActivity(new Intent(this, MyIntro.class));
            finish();
        }

        initAll();
        mTracker = new Tracker(this);

        makeSnackBar("Hello, " + prefs.getString(PreferenceIds.USER_NAME_KEY,"")+"!");
    }

    private void initAll() {

        mAppbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mAppbar);
        mFab = (FloatingActionButton) findViewById(R.id.next_btn);

        mDegree = (TextView) findViewById(R.id.degree_txt);
        mDetails = (TextView) findViewById(R.id.details_txt);
        mFun = (TextView) findViewById(R.id.fun_txt);
        mFunImage = (ImageView) findViewById(R.id.fun_img);

        mDegree.setText(prefs.getInt(DEGREE_KEY, 0) + "\u00b0");
        mDetails.setText(prefs.getString(DETAILS_KEY, "data unavailable"));

        mFab.setImageResource(R.drawable.ic_arrow_forward_white_24dp);
        mFab.hide();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent stopRain = new Intent(MainActivity.this,StopRainActivity.class);
                startActivity(stopRain);

            }
        });

    }

    private void makeJSONReq() {

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, tempData, (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONArray weather = response.getJSONArray(Weather.WEATHER_OBJECT_KEY);
                            JSONObject weatherObj = (JSONObject) weather.get(0);
                            main = weatherObj.getString(Weather.WEATHER_OBJECT_NAME_MAIN);
                            String desc = weatherObj.getString(Weather.WEATHER_OBJECT_NAME_DESC);

                            String city = response.getString(Weather.CITY_NAME_KEY);

                            JSONObject mainObj = response.getJSONObject(Weather.MAIN_OBJECT_KEY);
                            String temp = mainObj.getString(Weather.MAIN_OBJECT_NAME_TEMP);

                            int deg = computeDegree(temp);
                            String details = desc + "\n" + city;

                            mDegree.setText(deg + "\u00b0");
                            mDetails.setText(details);

                            prefs.edit().putInt(DEGREE_KEY, deg).commit();
                            prefs.edit().putString(DETAILS_KEY, details).commit();

                            mTracker.stopTracker();

                            checkAndEnableStopTheRainButton();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        makeSnackBar("Error parsing data.");
                    }
                });

        VolleySingleTon.getInstance().addToRequestQueue(objectRequest);

    }

    private void checkAndEnableStopTheRainButton() {

        Fun temp = new Fun();

        if (main.toUpperCase().contentEquals("RAIN")) {
            //enable
            mFun.setText(temp.getFunText(true));
            mFunImage.setImageResource(temp.getFunImage(true));
            mFab.show();
        } else {
            //disable
            mFun.setText(temp.getFunText(false));
            mFunImage.setImageResource(temp.getFunImage(false));
            mFab.hide();
        }

    }

    private int computeDegree(String temp) {
        return (int) (Float.parseFloat(temp) - 273.15);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTracker.stopTracker();
    }


    @Override
    protected void onResume() {
        super.onResume();

        generateLocationAndUpdate();

    }

    private void makeSnackBar(String text) {

        mSnack = Snackbar.make(findViewById(R.id.frame_layout),text,Snackbar.LENGTH_LONG);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            View snack = mSnack.getView();
            snack.setPaddingRelative(0,0,0,getNavigationBarHeight());
        }
        mSnack.show();

    }

    private void generateLocationAndUpdate() {
        location = mTracker.getLocation();

        if (location != null) {
            tempData = data + location.getLatitude() + "&lon=" + location.getLongitude();
        }

        makeJSONReq();
    }

    private int getNavigationBarHeight(){
        int id = getResources().getIdentifier("navigation_bar_height","dimen","android");
        if(id > 0){
            return getResources().getDimensionPixelSize(id);
        }
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                mFab.hide();
                generateLocationAndUpdate();
                makeSnackBar("Loading...");
                return true;
            case R.id.action_about:
                startActivity(new Intent(this,AboutActivity.class));
                return true;
            case R.id.action_profile:
                startActivity(new Intent(this,MyProfile.class));
        }


        return super.onOptionsItemSelected(item);
    }
}
