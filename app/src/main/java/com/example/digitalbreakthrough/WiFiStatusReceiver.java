package com.example.digitalbreakthrough;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class WiFiStatusReceiver extends BroadcastReceiver {
    public static Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (mContext == null) mContext = context;
        final WifiManager wm = (WifiManager) mContext.getSystemService(mContext.WIFI_SERVICE);
        if (intent.getBooleanExtra(wm.EXTRA_SUPPLICANT_CONNECTED, true)) {
            Toast.makeText(context, "Wifi connected to network" , Toast.LENGTH_LONG).show();
        }

        if (!intent.getBooleanExtra(wm.EXTRA_SUPPLICANT_CONNECTED, true)) {
            Toast.makeText(context, "Wifi disconnected from network" , Toast.LENGTH_LONG).show();
        }
    }
}         