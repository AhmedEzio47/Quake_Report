package com.devyat.quakereport;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    void fetchQuakesDataAndUpdateUI() {
        // Create a fake list of earthquake locations.
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        EarthquakeAdapter adapter = new EarthquakeAdapter(
                this, earthquakes);

        ListView earthquakeListView = findViewById(R.id.list);
        TextView emptyView = findViewById(R.id.empty_view);
        TextView errorView = findViewById(R.id.error_view);
        ProgressBar progressBar = findViewById(R.id.loading_spinner);

        Network network = new Network();
        network.fetchEarthQuakeData(this, new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                progressBar.setVisibility(View.GONE);
                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(result);
                    JSONArray features = jsonResponse.getJSONArray("features");
                    for (int i = 0; i < features.length(); i++) {
                        earthquakes.add(Earthquake.fromJSON(features.getJSONObject(i).getJSONObject("properties")));
                    }
                    if(earthquakes.isEmpty()){
                        emptyView.setVisibility(View.VISIBLE);
                        earthquakeListView.setEmptyView(emptyView);
                    }else{
                        earthquakeListView.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                progressBar.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                errorView.setText(error);
                earthquakeListView.setEmptyView(errorView);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}