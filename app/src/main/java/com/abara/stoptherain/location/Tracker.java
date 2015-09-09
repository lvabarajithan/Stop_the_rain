package com.abara.stoptherain.location;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

/**
 * Created by Abb on 8/25/2015.
 */
public class Tracker implements LocationListener {

    private Context mContext;
    private boolean bool;

    private static final long DISTANCE_UPDATE = 1; //meters
    private static final long TIME_BET_UPDATE = 1000 * 5; //5 sec

    private LocationManager mLocationManager;
    private Location mLocation;
    private double latitude,longitude;

    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;

    public Tracker(Context context){
        this.mContext = context;
        //getLocation();
    }

    public Location getLocation(){

        try{

            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isNetworkEnabled && !isGPSEnabled){
                if(!showSettings()){

                }
            }else{

                if(isGPSEnabled){
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,TIME_BET_UPDATE,DISTANCE_UPDATE,this);

                    if(mLocationManager!=null){
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        //updateLocation();
                    }

                }

                if(isNetworkEnabled){
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,TIME_BET_UPDATE,DISTANCE_UPDATE,this);

                    if(mLocationManager!=null){
                        mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        //updateLocation();
                    }

                }

            }

        }catch(Exception e){

        }

        return mLocation;

    }

    /*private void updateLocation() {

        if(mLocation != null){
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
        }

    }*/

    private boolean showSettings() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Location Access");
        builder.setMessage("I need your current location to stop the rain!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mContext.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                bool = true;
            }
        });
        builder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                bool = false;
            }
        });
        builder.show();

        return bool;

    }

    public void stopTracker(){
        if(mLocationManager!=null){
            mLocationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //empty shit
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
