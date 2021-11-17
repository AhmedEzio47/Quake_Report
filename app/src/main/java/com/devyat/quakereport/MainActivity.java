package com.devyat.quakereport;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devyat.quakereport.adapters.EarthquakeAdapter;
import com.devyat.quakereport.helpers.Network;
import com.devyat.quakereport.models.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&minlatitude=6";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a fake list of earthquake locations.
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = USGS_REQUEST_URL;
        EarthquakeAdapter adapter = new EarthquakeAdapter(
                this, earthquakes);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray features = jsonResponse.getJSONArray("features");
                            for (int i = 0; i < features.length(); i++) {
                                earthquakes.add(Earthquake.fromJSON(features.getJSONObject(i).getJSONObject("properties")));
                            }

                            // Find a reference to the {@link ListView} in the layout
                            ListView earthquakeListView = findViewById(R.id.list);
                            // Set the adapter on the {@link ListView}
                            // so the list can be populated in the user interface
                            earthquakeListView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}