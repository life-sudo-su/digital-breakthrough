package com.example.block_param;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServiceDetectCamera extends Service {
    public static final String SERVICE_ACTION = "ru.krylosovandrei.detecter.PLAYER" ;
    final String LOG_TAG = "myLogs";
    public ServiceDetectCamera() {
    }

    public void onCreate() {
        super.onCreate();
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo info : manager.getRunningAppProcesses()) {
            Log.e("TAG", "Running process: " + info.processName);
        }
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        someTask();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }
    void someTask() {

    }
}
