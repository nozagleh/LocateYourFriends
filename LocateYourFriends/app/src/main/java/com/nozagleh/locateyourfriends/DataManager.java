package com.nozagleh.locateyourfriends;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.nfc.Tag;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by arnarfreyr on 19.11.2017.
 */

public class DataManager {
    // Log tag
    private static final String TAG = "DataManager";

    // Root domain URI
    private static final String ROOT_DOMAIN = "http://192.168.1.61:9000";

    private static final String PINGER = ROOT_DOMAIN + "/logs/pinger/";

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
     * @deprecated use checkDomain
     * @param context Context Application context
     */
    public static void isDomainUp(Context context) {
        NetworkManager.pingDomain(PINGER, context);
    }

    public static void getIsServerUp(Context context, Response.Listener listener) {
        NetworkManager.pingDomain(PINGER, context, listener);
    }

    /**
     * Check if the domain is up.
     * @param context Context Application context
     */
    public static void checkDomain(Context context) {
        NetworkManager.pingDomain(PINGER, context);
    }

    public static boolean registerNewUser(Context context) {
        return APIConnector.registerNewDevice(context, new DataCallback() {
            @Override
            public boolean booleanResponse(boolean success) {
                return success;
            }
        });
    }
}

class APIConnector {
    // Log tag
    private static final String TAG = "APIConnector";

    private static final String ADD_USER = DataManager.getRootDomain() + "/user/add";
    private static final String ADD_GROUP = DataManager.getRootDomain() + "/group/add";
    private static final String GET_USER_GROUPS = DataManager.getRootDomain() + "/group/get";
    private static final String ADD_USER_LOCATION = DataManager.getRootDomain() + "/location";

    public static boolean stringRequest( String url, final Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        if(!DataManager.getIsServerUp())
            return false;

        StringRequest registerUser = new StringRequest(Request.Method.GET, url, responseListener, errorListener);

        NetworkManager.getQueue().add(registerUser);

        return true;
    }

    public static boolean jsonRequest(final Context context, final String url, final Response.Listener<JSONObject> listener, final Response.ErrorListener errorListener) {
        /*if(!DataManager.getIsServerUp())
            return false;*/

        Response.Listener<String> checkListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if ( response.length() > 0 ) {
                    JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,url,null, listener, errorListener);

                    NetworkManager.getQueue().add(jsonReq);
                } else {
                    NotificationManager.notifyNoInternet(context.getString(R.string.notification_no_internet));
                }

            }
        };

        DataManager.getIsServerUp(context, checkListener);

        return  true;
    }

    public static boolean registerNewDevice(final Context context, final DataCallback callback) {
        String userHashId = String.valueOf((int) (Math.random() * 42 + 1));

        int hash = userHashId.hashCode();

        String url = ADD_USER + "/" + String.valueOf(hash) + "/";

        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()) {
                    callback.booleanResponse(true);
                    LocalDataManager.setUserId(context, response);
                } else {
                    callback.booleanResponse(false);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage());
                callback.booleanResponse(false);
            }
        };

        APIConnector.stringRequest(url, listener, errorListener);

        return true;
    }

    public static boolean sendLocationUpdate() {
        return false;
    }

    public static boolean getGroups(Context context, String uid) {
        String url = GET_USER_GROUPS + "/" + uid;

        APIConnector.jsonRequest(context, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int len = response.length();
                Gson gson = new GsonBuilder().create();

                List<Group> groupList = new ArrayList<>();
                for (int i = 0; i < len; i++) {
                    try {
                        Group group = gson.fromJson(response.getJSONObject(String.valueOf(i)).toString(), Group.class);
                        groupList.add(group);
                    } catch (JSONException error) {

                    }
                }
                LocalDataManager.setGroupList(groupList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });

        return false;
    }

    /*public void notifyListFragment() {
        GroupListFragment listFrag = (GroupListFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAG_LIST);

        if (listFrag != null && listFrag.isVisible()) {
            listFrag.dataChange();
        }
    }*/

    public static boolean addGroup(String uid, String groupName) {
        String url = ADD_GROUP + "/" + uid + "/" + groupName;

        APIConnector.stringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "failed");
            }
        });

        return true;
    }

    public static boolean getUsers() {
        return false;
    }

    public static void addUserLocation( String uid, Location location ) {
        String lat = String.valueOf(location.getLatitude());
        String lng = String.valueOf(location.getLongitude());

        String url = ADD_USER_LOCATION + "/" + uid + "/?lat=" + lat + "&lng=" + lng;

        APIConnector.stringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "location reported");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error reporting location");
            }
        });
    }
}

class UrlBuilder {

    public static String buildUrl(String path, Map<String, String> params) {
        int count = 0;
        String parameters = "/";
        for (Map.Entry<String, String> param : params.entrySet()) {
            parameters += addField(param.getKey(), param.getValue(), count);
            count++;
        }

        return path + parameters;
    }

    private static String addField(String field, String value, int fieldCount) {
        String path = "";

        if (fieldCount >= 1)
            path += "&";
        else
            path += "?";

        path += field + "=" + value;

        return path;
    }
}