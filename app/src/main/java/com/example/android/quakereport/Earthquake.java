package com.example.android.quakereport;

public class Earthquake {
    private float magnitude;
    private String place;
    private long epochTime;
    private double latitude;
    private double longitude;

    public Earthquake(float magnitude, String place, long epochTime, double latitude, double longitude) {
        this.magnitude = magnitude;
        this.place = place;
        this.epochTime = epochTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public double getLatitude() {return latitude; }

    public double getLongitude() { return longitude;}

    public String getPlace() {
        return place;
    }

    public long getEpochTime() {
        return epochTime;
    }
}
