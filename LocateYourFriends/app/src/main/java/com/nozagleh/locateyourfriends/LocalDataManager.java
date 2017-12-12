package com.nozagleh.locateyourfriends;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arnarfreyr on 26.11.2017.
 */

public class LocalDataManager {
    private static final String TAG = "LocalDataManager";

    public static final String SHARED_PREF_NAME = "LocalSharedPrefs";

    // SharedPreferences fields
    public static final String SP_FIELD_UID = "sp_uid";

    private static List<Group> groupList = new ArrayList<>();

    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, 0);

        return sp.getString(SP_FIELD_UID, "");
    }

    public static void setUserId(Context context, String uid) {
        Log.d(TAG, uid);
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, 0);

        sp.edit().putString(SP_FIELD_UID, uid).apply();
    }

    public static void removeUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, 0);

        sp.edit().remove(SP_FIELD_UID).apply();
    }

    public static boolean hasUserId(Context context) {
        return context.getSharedPreferences(SHARED_PREF_NAME, 0).contains(SP_FIELD_UID);
    }

    public static List<Group> getGroupList() {
        Log.d(TAG, "Group list" + groupList.toString());
        return groupList;
    }

    public static void setGroupList(List<Group> groupList) {
        Log.d(TAG, "setting list" + groupList.toString());
        LocalDataManager.groupList = groupList;
    }
}
