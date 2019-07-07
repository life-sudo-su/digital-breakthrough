package com.example.digitalbreakthrough;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class LocationActivity extends FragmentActivity {

    public Camera camera;
    public ConnectivityManager dataManager;
    //university campus
    private double LatitudeWorkerPlace = 54.986650;
    private double LontitudeWorkerPlace = 82.862930;

    //square of fame
    //private double LatitudeWorkerPlace = 54.986998;
    //private double LontitudeWorkerPlace = 82.875720;

    private WifiManager wifiManager;

    private static final int REQUEST_LOCATION = 1;
    public LocationManager mLocationManager;
    public LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            //Log.i("Message: ","Location changed, " + location.getAccuracy() + " , " + location.getLatitude()+ "," + location.getLongitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        dataManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildMessage();
        } else {
            getAndCheckLocation();

        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAndCheckLocation();
                handler.postDelayed(this, 5000);
            }
        }, 5000);

        setContentView(R.layout.activity_maps);

    }

    public void checkLocation(Location location) {

        Log.d("test", "checkLocation: calling method success");

        float[] dist = new float[1];

        Location.distanceBetween(LatitudeWorkerPlace, LontitudeWorkerPlace, location.getLatitude(), location.getLongitude(), dist);
        float distance = dist[0];
        if(distance < 100){
            //disable actions

            Log.d("test", "checkLocation: user is not far away");
            wifiManager.setWifiEnabled(false);
            setMobileDataEnabled(false);
            camera = Camera.open();

        }
        else
            {
                Log.d("test", "checkLocation: user is far away");
                wifiManager.setWifiEnabled(true);
                setMobileDataEnabled(true);
                isCameraUsebyApp();
            }
    }

    private void getAndCheckLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);

            }
            else
                {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000 * 10, 10, mLocationListener);
                    /*mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                            mLocationListener);*/
                    Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if(location != null)
                    {

                        Log.d("location","lan " + location.getLatitude() + " lon: " + location.getLongitude());
                        checkLocation(location);
                    }
                    else
                        {
                            Log.d("getAndCheckLocation", "getAndCheckLocation: Unable to track your location");
                        }

                }


    }

    private void buildMessage() {
        Log.d("buildMessage", "buildMessage: the service requires access to fine and coarse location");
    }

    public void isCameraUsebyApp() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
    }

    private void setMobileDataEnabled(boolean isEnabled) {
        NetworkInfo networkInfo = dataManager.getActiveNetworkInfo();
        if(networkInfo!= null && networkInfo.isConnected() && !isEnabled)
        {
            Settings.System.putInt(this.getContentResolver(),Settings.Global.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);
        }
        else
            {
                Settings.System.putInt(this.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);
            }

    }

}
