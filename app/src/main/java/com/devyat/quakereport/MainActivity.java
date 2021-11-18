package com.devyat.quakereport;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.devyat.quakereport.adapters.EarthquakeAdapter;
import com.devyat.quakereport.helpers.Network;
import com.devyat.quakereport.helpers.VolleyCallback;
import com.devyat.quakereport.models.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchQuakesDataAndUpdateUI();
    }

    void fetchQuakesDataAndUpdateUI(){
        // Create a fake list of earthquake locations.
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        EarthquakeAdapter adapter = new EarthquakeAdapter(
                this, earthquakes);

        Network network = new Network();
        network.fetchEarthQuakeData(this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(result);
                    JSONArray features = jsonResponse.getJSONArray("features");
                    for (int i = 0; i < features.length(); i++) {
                        earthquakes.add(Earthquake.fromJSON(features.getJSONObject(i).getJSONObject("properties")));
                    }
                    ListView earthquakeListView = findViewById(R.id.list);
                    earthquakeListView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}