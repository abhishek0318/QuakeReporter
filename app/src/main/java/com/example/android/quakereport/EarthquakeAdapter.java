package com.example.android.quakereport;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.android.quakereport.R.id.place;


public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    public EarthquakeAdapter(Context context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Earthquake earthquake = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        TextView magnitudeTv = (TextView) convertView.findViewById(R.id.magnitude);
        TextView distanceTv = (TextView) convertView.findViewById(R.id.distance);
        TextView placeTv = (TextView) convertView.findViewById(place);
        TextView dateTv = (TextView) convertView.findViewById(R.id.date);
        TextView timeTv = (TextView) convertView.findViewById(R.id.time);

        magnitudeTv.setText(String.valueOf(earthquake.getMagnitude()));
        if (earthquake.getMagnitude() > 6.0) {
            magnitudeTv.setBackgroundResource(R.drawable.red_circle);
            magnitudeTv.setTextColor(Color.WHITE);
        }
        else if (earthquake.getMagnitude() > 5.0) {
            magnitudeTv.setBackgroundResource(R.drawable.orange_circle);
            magnitudeTv.setTextColor(Color.WHITE);
        }
        else if (earthquake.getMagnitude() > 4.0) {
            magnitudeTv.setBackgroundResource(R.drawable.yellow_circle);
            magnitudeTv.setTextColor(Color.BLACK);
        }
        else {
            magnitudeTv.setBackgroundResource(R.drawable.green_circle);
            magnitudeTv.setTextColor(Color.WHITE);
        }

        String place = earthquake.getPlace();
        String[] parts = place.split(" of ");
        String distance = parts[0] + " of";
        distanceTv.setText(distance);

        String location = parts[1];
        if (location.startsWith("the ")) {
            location = location.substring(4);
        }
        placeTv.setText(location);

        long epochTime = earthquake.getEpochTime();
        Date dateObj = new Date(epochTime);

        SimpleDateFormat sdf = new SimpleDateFormat("h:mma" );
        String time = sdf.format(dateObj);
        timeTv.setText(time);

        sdf = new SimpleDateFormat("d MMM yyyy");
        String date = sdf.format(dateObj);
        dateTv.setText(date);

        return convertView;
    }
}
