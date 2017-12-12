package com.nozagleh.locateyourfriends;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by arnarfreyr on 18.11.2017.
 */

public class NetworkManager {
    private static final String TAG = "NetworkManager";

    public static RequestQueue queue;

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public static RequestQueue getQueue() {
        return queue;
    }

    public static boolean initQueue(Context context) {
        if (context == null)
            return false;

        queue = Volley.newRequestQueue(context);
        return true;
    }

    public static boolean pingDomain(String url, Context context) {
        if (hasInternetConnection(context)) {
            try {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ( response.length() > 0 ) {
                            Log.d(TAG, "up");
                            DataManager.setIsServerUp(true);
                        } else {
                            Log.d(TAG, "down");
                            DataManager.setIsServerUp(false);
                        }
                    }
                },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, error.toString());
                        }
                });

                NetworkManager.getQueue().add(stringRequest);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return false;
    }

    public static boolean pingDomain(String url, Context context, Response.Listener listener) {
        if (hasInternetConnection(context)) {
            try {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, error.toString());
                            }
                        });

                NetworkManager.getQueue().add(stringRequest);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return false;
    }
}
