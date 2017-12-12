package com.nozagleh.locateyourfriends;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NetworkManager.initQueue(this);

        if(PermissionManager.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            GPSManager.initLocationManager(this);
            GPSManager.startLocationTracking(this);

            BackgroundManager.scheduleJob(this);
        }

        // Start the main activity
        startActivity(new Intent(SplashActivity.this,MainActivity.class));

        // Finish the splash activity
        finish();
    }
}
