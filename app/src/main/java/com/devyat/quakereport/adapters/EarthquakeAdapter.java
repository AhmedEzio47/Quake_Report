package com.devyat.quakereport.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.devyat.quakereport.R;
import com.devyat.quakereport.helpers.DateFormatter;
import com.devyat.quakereport.models.Earthquake;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    public EarthquakeAdapter(@NonNull Context context, List<Earthquake> earthquakeList) {
        super(context, 0, earthquakeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.quake_item, parent, false);
        }
        TextView magnitudeTV = listItemView.findViewById(R.id.quake_magnitude);
        magnitudeTV.setText(String.format("%s", getItem(position).getMagnitude()));

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTV.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(getItem(position).getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        TextView grainLocationTV = listItemView.findViewById(R.id.quake_location);

        TextView fineLocationTV = listItemView.findViewById(R.id.quake_location_fine);
        String location = getItem(position).getLocation();

        String grainLocation = "", fineLocation = "";
        if(location.contains("of")){
            grainLocation = location.split("of")[1];
            fineLocation = location.split("of")[0] + "of";
        }else{
            grainLocation = location;
            fineLocationTV.setVisibility(View.GONE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_VERTICAL;
            grainLocationTV.setLayoutParams(params);
        }
        grainLocationTV.setText(grainLocation);
        fineLocationTV.setText(fineLocation);

        TextView dateTV = listItemView.findViewById(R.id.quake_date);
        dateTV.setText(DateFormatter.formatDate(getItem(position).getDate()));

        TextView timeTV = listItemView.findViewById(R.id.quake_time);
        timeTV.setText(DateFormatter.formatTime(getItem(position).getDate()));
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getItem(position).getUrl())));
            }
        });
        return listItemView;
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
