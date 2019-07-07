package com.example.digitalbreakthrough;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CameraEventReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "New Photo Clicked", Toast.LENGTH_LONG).show();

    }
}
