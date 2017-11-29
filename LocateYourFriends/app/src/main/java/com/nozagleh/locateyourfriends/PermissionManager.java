package com.nozagleh.locateyourfriends;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.util.Log;

/**
 * Created by arnarfreyr on 18.11.2017.
 */

public class PermissionManager {
    private static final String TAG = "PermissionManager";

    private static final int GPS_PERMISSION_REQUEST = 42;

    public static void askPermission(Activity currentActivity, String permission) {
        Log.d(TAG, "asking...");
        if (ContextCompat.checkSelfPermission(currentActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, permission)) {
                // TODO show rationale for asking this permission again
                Log.d(TAG, "showing rationale");
            } else {
                ActivityCompat.requestPermissions(currentActivity, new String[]{permission},GPS_PERMISSION_REQUEST);
            }
        }
    }

    public static boolean askPermissionResults( int requestCode, String[] permissions, int[] grantResults ) {
        switch (requestCode) {
            case GPS_PERMISSION_REQUEST: {
                return (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            }
        }
        return false;
    }

    public static boolean checkPermission(Activity currentActivity, String permission) {
        Integer permissionCheck = ContextCompat.checkSelfPermission(currentActivity, permission);
        switch (permissionCheck) {
            case PackageManager.PERMISSION_GRANTED: {
                return true;
            }
        }
        return false;
    }
}
