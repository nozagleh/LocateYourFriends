package com.nozagleh.locateyourfriends;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by arnarfreyr on 18.11.2017.
 */

public class NotificationManager {
    private static View currentView;

    public static void setView(View view) {
        currentView = view;
    }

    private static void baseNotification(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private static void baseNotification(String message) {
        Snackbar snackbar = Snackbar.make(currentView, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void notifyNoInternet(View view, String message) {
        baseNotification(view, message);
    }

    public static void notifyNoInternet(String message) {
        baseNotification(message);
    }

    public static void notifyNoGPS(View view, String message) {
        baseNotification(view, message);
    }

    public static void notifyNoGPS(String message) {
        baseNotification(message);
    }
}
