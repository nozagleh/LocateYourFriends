package com.nozagleh.locateyourfriends;

import android.content.Context;
import android.util.Log;

/**
 * Created by arnarfreyr on 19.11.2017.
 */

public class DataManager {
    // Log tag
    private static final String TAG = "DataManager";

    // Root domain URI
    private static final String ROOT_DOMAIN = "http://nozagleh.com/";

    // Service available flag
    private static boolean isServerUp = false;

    /**
     * Return the root domain.
     * @return String root domain
     */
    public static String getRootDomain() {
        return ROOT_DOMAIN;
    }

    /**
     * Return if the server is up.
     * @return boolean Is server up/service available
     */
    public static boolean getIsServerUp() {
        return isServerUp;
    }

    /**
     * Set the server up flag.
     * @param isUp boolean
     */
    public static void setIsServerUp(boolean isUp) {
        isServerUp = isUp;
    }

    public static boolean getData() {
        return false;
    }

    /**
     * Check if the domain is up.
     * @param context Context Application context
     * @return boolean Is the domain up and available
     */
    public static boolean isDomainUp(Context context) {
        return NetworkManager.pingDomain(ROOT_DOMAIN, context, new NetworkCallbacks() {
            @Override
            public boolean pingResponse(boolean isUp) {
                return isUp;
            }
        });
    }

    public static String getUserID() {
        return "ID";
    }
}
