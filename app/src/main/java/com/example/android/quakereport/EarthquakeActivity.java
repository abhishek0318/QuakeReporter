/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private String sJSON;
    ArrayList<Earthquake> earthquakes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        earthquakes = new ArrayList<>();
        new DownloadJSON().execute();
    }

    private void getEarthquakes() throws JSONException {

        JSONObject jsonObject = new JSONObject(sJSON);
        JSONArray earthquakesArray = jsonObject.optJSONArray("features");
        for (int i = 0; i < earthquakesArray.length(); i++) {
            JSONObject earthquakeObject = (JSONObject) earthquakesArray.get(i);
            JSONObject properties = (JSONObject) earthquakeObject.get("properties");
            Float magnitude = Float.valueOf(properties.getString("mag"));
            magnitude = ((float) Math.round(magnitude * 10) / 10);
            String place = properties.getString("place");
            long epochTime = Long.parseLong(properties.getString("time"));
            JSONObject geometry = (JSONObject) earthquakeObject.get("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            double longitude = coordinates.getDouble(0);
            double latitude = coordinates.getDouble(1);
            earthquakes.add(new Earthquake(magnitude, place, epochTime, latitude, longitude));
        }
    }

    private void getJSON() {

    }

    private class DownloadJSON extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

//            String sURL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2017-01-01&minlatitude=8&maxlatitude=37&minlongitude=68&maxlongitude=97&limit=20";
            String sURL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2017-01-01&limit=20";
            String content = null;
            URLConnection connection = null;
            try {
                connection = new URL(sURL).openConnection();
                Scanner scanner = new Scanner(connection.getInputStream());
                scanner.useDelimiter("\\Z");
                content = scanner.next();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            if (content != null)
                sJSON = content;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            if (sJSON == null) {
                Toast.makeText(getApplicationContext(), "Error connecting to the internet!", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                getEarthquakes();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ListView earthquakeListView = (ListView) findViewById(R.id.list);
            EarthquakeAdapter adapter = new EarthquakeAdapter(getApplicationContext(), earthquakes);
            earthquakeListView.setAdapter(adapter);
            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Earthquake earthquake = earthquakes.get(i);
                    String place = earthquake.getPlace();
                    String[] parts = place.split(" of ");
                    String distance = parts[0] + " of";
                    String location = parts[1];
                    if (location.startsWith("the ")) {
                        location = location.substring(4);
                    }
                    String sUri = "geo:0,0?z=4&q=" + String.valueOf(earthquake.getLatitude()) + "," + String.valueOf(earthquake.getLongitude()) + "(" + location + ")";
                    Uri gmmIntentUri = Uri.parse(sUri);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });
        }
    }

}
