package com.buttonsquared;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.hardware.Camera;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RemoteViews;

public class CurrentMoodService extends Service {
    private boolean isLighOn = false;
    private Camera camera;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStart(intent, startId);

        Log.i(CurrentMoodWidgetProvider.WIDGETTAG, "onStartCommand");

        Context context = this;
        PackageManager pm = context.getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Log.e("err", "Device has no camera!");
            stopSelf(startId);
        } else {

            if (camera == null) {
                camera = Camera.open();
            }
            final Camera.Parameters p = camera.getParameters();

            int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
            AppWidgetManager appWidgetMan = AppWidgetManager.getInstance(this);
            RemoteViews views = new RemoteViews(this.getPackageName(),R.layout.widgetlayout);

            if (isLighOn) {
                Log.i("info", "torch is turn off!");
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.stopPreview();

                views.setImageViewResource(R.id.widgetBtn, R.drawable.panda_off);
                appWidgetMan.updateAppWidget(widgetId, views);

                isLighOn = false;
                stopSelf(startId);
            } else {
                Log.i("info", "torch is turn on!");
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                camera.startPreview();
                views.setImageViewResource(R.id.widgetBtn, R.drawable.panda_on);
                appWidgetMan.updateAppWidget(widgetId, views);
                isLighOn = true;
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
