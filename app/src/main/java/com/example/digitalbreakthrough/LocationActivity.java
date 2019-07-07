package com.example.digitalbreakthrough;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

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
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SECURE_SETTINGS}, REQUEST_LOCATION);
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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void checkLocation(Location location) {

        Log.d("test", "checkLocation: calling method success");

        float[] dist = new float[1];

        Location.distanceBetween(LatitudeWorkerPlace, LontitudeWorkerPlace, location.getLatitude(), location.getLongitude(), dist);
        float distance = dist[0];
        if (distance < 50) {
            //disable actions

            Log.d("test", "checkLocation: user is not far away");
            wifiManager.setWifiEnabled(false);
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            }
            setMobileDataEnabled(getApplicationContext(), false);
            //camera = Camera.open();

        } else {
            Log.d("test", "checkLocation: user is far away");
            wifiManager.setWifiEnabled(true);
            setMobileDataEnabled(getApplicationContext(), true);
            isCameraUsebyApp();
        }
    }

    private void getAndCheckLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);

        } else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 10, 10, mLocationListener);
                    /*mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                            mLocationListener);*/
            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {

                Log.d("location", "lan " + location.getLatitude() + " lon: " + location.getLongitude());
                checkLocation(location);
            } else {
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

    private void setMobileDataEnabled(Context context, boolean isEnabled) {
        /*NetworkInfo networkInfo = dataManager.getActiveNetworkInfo();
        if(networkInfo!= null && networkInfo.isConnected() && !isEnabled)
        {
            // toggle airplane mode
            Settings.System.putInt(
                    getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 1);

            // Post an intent to reload
            //Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            //intent.putExtra("state", !isEnabled);
            //sendBroadcast(intent);
        }
        else
            {
                Settings.System.putInt(this.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0);
            }*/
        /*int value = isEnabled ? 1 : 0;
        if (isEnabled){
            value = 1;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) { //if less than verson 4.2
            Settings.System.putInt(
                    getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, value);
        } else {
            Settings.Global.putInt(
                    getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, value);
        }*/
        // broadcast an intent to inform
        //Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        //intent.putExtra("state", !isEnabled);
        //sendBroadcast(intent);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        GoogleMap mMap = map;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);

        } else {
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng restrictedPosition = new LatLng(LatitudeWorkerPlace,LontitudeWorkerPlace);

            mMap.addMarker(new MarkerOptions().position(loc).title("Your Position"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

            CircleOptions co = new CircleOptions();
            co.center(restrictedPosition);
            co.radius(60);
            co.fillColor(Color.BLUE);
            co.strokeColor(0x30ff0000);
            co.strokeWidth(2.0f);

            mMap.addCircle(co);
        }
    }
}


