package com.devyat.quakereport.helpers;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import androidx.preference.PreferenceManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devyat.quakereport.R;

public class Network {
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&minmagnitude=6";

    public void fetchEarthQuakeData(Context context, final VolleyCallback callback) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
            String minMagnitude = sharedPrefs.getString(
                    context.getString(R.string.settings_min_magnitude_key),
                    context.getString(R.string.settings_min_magnitude_default));
            Uri baseUri = Uri.parse(USGS_REQUEST_URL);
            String orderBy = sharedPrefs.getString(
                    context.getString(R.string.settings_order_by_key),
                    context.getString(R.string.settings_order_by_default)
            );
            Uri.Builder uriBuilder = baseUri.buildUpon();

            uriBuilder.appendQueryParameter("format", "geojson");
            uriBuilder.appendQueryParameter("limit", "10");
            uriBuilder.appendQueryParameter("minmag", minMagnitude);
            uriBuilder.appendQueryParameter("orderby", orderBy);

            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, uriBuilder.toString(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            callback.onSuccess(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    callback.onFailure(error.getMessage());
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } else {
            callback.onFailure("No internet connection");
        }

    }
}

