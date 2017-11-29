package com.nozagleh.locateyourfriends;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by arnarfreyr on 26.11.2017.
 */

public class LocalDataManager {
    public static final String SHARED_PREF_NAME = "LocalSharedPrefs";

    // SharedPreferences fields
    public static final String SP_FIELD_UID = "sp_uid";

    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, 0);

        return sp.getString(SP_FIELD_UID, null);
    }

    public static boolean hasUserId(Context context) {
        if(getUserId(context) == null) {
            return false;
        }

        return true;
    }
}
