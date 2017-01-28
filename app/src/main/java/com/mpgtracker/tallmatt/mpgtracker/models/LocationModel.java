package com.mpgtracker.tallmatt.mpgtracker.models;

/**
 * Created by TallMatt on 1/24/2017.
 */

public class LocationModel {

    public float lat;
    public float lon;

    public LocationModel(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public static LocationModel stringToLocation(String locationString) {

        if (locationString.isEmpty()) {
            return null;
        }
        return new LocationModel(Float.parseFloat(locationString.substring(0, locationString.indexOf(':'))), Float.parseFloat(locationString.substring(locationString.indexOf(':')+1)));
    }

    public String toString() {
        return (lat + ":" + lon);
    }
}
