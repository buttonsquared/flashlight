package com.buttonsquared;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;
import android.hardware.Camera;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.hardware.Camera;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class CurrentMoodService extends Service {
    public boolean isLighOn = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStart(intent, startId);

        Log.i(CurrentMoodWidgetProvider.WIDGETTAG, "onStartCommand");


        Camera camera = Camera.open();
        final Camera.Parameters p = camera.getParameters();

        if (isLighOn) {

            Log.i("info", "torch is turn off!");

            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(p);
            camera.stopPreview();
            isLighOn = false;

        } else {

            Log.i("info", "torch is turn on!");

            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

            camera.setParameters(p);
            camera.startPreview();
            isLighOn = true;

        }

        stopSelf(startId);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
