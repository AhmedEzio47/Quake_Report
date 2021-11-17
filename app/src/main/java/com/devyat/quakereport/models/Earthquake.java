package com.devyat.quakereport.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Earthquake {
    private String location;
    private double magnitude;
    private Date date;
    private String url;
    public Earthquake(String location, double magnitude, Date date, String url) {
        this.location = location;
        this.magnitude = magnitude;
        this.date = date;
        this.url = url;
    }

    public String getLocation() {
        return this.location;
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public Date getDate() {
        return this.date;
    }

    public String getUrl(){
        return this.url;
    }

    public static Earthquake fromJSON(JSONObject earthquakeObject) {
        try {
            return new Earthquake(earthquakeObject.getString("place"), earthquakeObject.getDouble("mag"), new Date(earthquakeObject.getLong("time")), earthquakeObject.getString("url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
