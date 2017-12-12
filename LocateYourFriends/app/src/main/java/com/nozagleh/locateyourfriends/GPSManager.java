package com.nozagleh.locateyourfriends;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by arnarfreyr on 3.12.2017.
 */

public class GPSManager {

    private static final String TAG = "GPSManager";

    private static LocationManager lm;
    private static LocationListener ll;
    private static PackageManager pm;

    private static int hasPerm;

    private static Location currentLocation;

    public static void initLocationManager(Context context) {
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        ll = new CustomLocationListener();
        pm = context.getPackageManager();
    }

    public static void startLocationTracking(Context context) {

        // Get the permission
        hasPerm = pm.checkPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                context.getPackageName()
        );

        // Check if the needed permission for the GPS has been granted
        if (hasPerm == PackageManager.PERMISSION_GRANTED) {
            // Request location updates
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 5, ll);
        }

        currentLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public static void stopLocationTracking() {
        lm.removeUpdates(ll);
    }

    public static Location getLocation(Context context) {
        // Get the permission
        hasPerm = pm.checkPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                context.getPackageName()
        );

        // Check if the needed permission for the GPS has been granted
        if (hasPerm == PackageManager.PERMISSION_DENIED) {
            return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        return currentLocation;
    }

    public static void setLocation(Location location) {
        currentLocation = location;
    }
}

class CustomLocationListener implements LocationListener {

    private final String TAG = "CustomLocationListener";

    @Override
    public void onLocationChanged(Location location) {
        GPSManager.setLocation(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
        NotificationManager.notifyNoGPS("aas");
    }

    @Override
    public void onProviderDisabled(String s) {
        NotificationManager.notifyNoGPS("aas");
    }
}
