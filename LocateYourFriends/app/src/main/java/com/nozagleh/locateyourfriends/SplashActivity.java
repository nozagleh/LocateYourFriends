package com.nozagleh.locateyourfriends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NetworkManager.initQueue(this);
        DataManager.setIsServerUp(DataManager.isDomainUp(this));

        // Start the main activity
        startActivity(new Intent(SplashActivity.this,MainActivity.class));

        // Finish the splash activity
        finish();
    }
}
