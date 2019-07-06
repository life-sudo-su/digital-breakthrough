package com.example.digitalbreakthrough;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

public class LocationActivity extends FragmentActivity {

    private double LatitudeWorkerPlace = 55.040791;
    private double LontitudeWorkerPlace = 82.919684;

    private static final int REQUEST_LOCATION = 1;
    public LocationManager mLocationManager;
    public LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            Log.i("Message: ","Location changed, " + location.getAccuracy() + " , " + location.getLatitude()+ "," + location.getLongitude());
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

        //mLocationManager.req
        setContentView(R.layout.activity_maps);

    }

    public void checkLocation(Location location) {

        Log.d("test", "checkLocation: calling method success");

        float[] dist = new float[1];

        Location.distanceBetween(LatitudeWorkerPlace, LontitudeWorkerPlace, location.getLatitude(), location.getLongitude(), dist);

        if(dist[0]/1000 < 10){
            Log.d("test", "checkLocation: user is not far away");
        }
        else
            {
                Log.d("test", "checkLocation: user is far away");
                return;
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


}