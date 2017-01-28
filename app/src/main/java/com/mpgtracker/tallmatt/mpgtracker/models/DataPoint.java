package com.mpgtracker.tallmatt.mpgtracker.models;

import java.io.Serializable;

/**
 * Created by TallMatt on 1/24/2017.
 */

public class DataPoint implements Serializable {

    public long id;
    public long carId;
    public float gallonsPutIn;
    public float milesTravelled;
    public float moneySpent;
    public long dateAdded;
    public LocationModel location;

    public DataPoint(float gallonsPutIn, float milesTravelled, float moneySpent, long dateAdded, LocationModel location) {

        this(-1, -1, gallonsPutIn, milesTravelled, moneySpent, dateAdded, location);
    }

    public DataPoint(long id, long carId, float gallonsPutIn, float milesTravelled, float moneySpent, long dateAdded, String location) {

        this(id, carId, gallonsPutIn, milesTravelled, moneySpent, dateAdded, LocationModel.stringToLocation(location));
    }

    public DataPoint(long id, long carId, float gallonsPutIn, float milesTravelled, float moneySpent, long dateAdded, LocationModel location) {

        this.id = id;
        this.carId = carId;
        this.gallonsPutIn = gallonsPutIn;
        this.milesTravelled = milesTravelled;
        this.moneySpent = moneySpent;
        this.dateAdded = dateAdded;
        this.location = location;
    }

    public String getLocationString() {
        return location.toString();
    }
}
