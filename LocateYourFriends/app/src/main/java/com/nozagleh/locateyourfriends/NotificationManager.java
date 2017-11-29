package com.nozagleh.locateyourfriends;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by arnarfreyr on 18.11.2017.
 */

public class NotificationManager {
    private static void baseNotification(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void notifyNoInternet(View view, String message) {
        baseNotification(view, message);
    }

    public static void notifyNoGPS(View view, String message) {
        baseNotification(view, message);
    }
}
