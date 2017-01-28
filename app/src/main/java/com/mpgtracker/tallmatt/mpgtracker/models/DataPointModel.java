package com.mpgtracker.tallmatt.mpgtracker.models;

import com.mpgtracker.tallmatt.mpgtracker.utils.Utils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TallMatt on 1/24/2017.
 */

public class DataPointModel implements Serializable {

    public long id;
    public long carId;
    public float gallonsPutIn;
    public float milesTravelled;
    public float moneySpent;
    public long dateAdded;
    public LocationModel location;

    public DataPointModel(float gallonsPutIn, float milesTravelled, float moneySpent, long dateAdded, LocationModel location) {

        this(-1, -1, gallonsPutIn, milesTravelled, moneySpent, dateAdded, location);
    }

    public DataPointModel(long id, long carId, float gallonsPutIn, float milesTravelled, float moneySpent, long dateAdded, String location) {

        this(id, carId, gallonsPutIn, milesTravelled, moneySpent, dateAdded, LocationModel.stringToLocation(location));
    }

    public DataPointModel(long id, long carId, float gallonsPutIn, float milesTravelled, float moneySpent, long dateAdded, LocationModel location) {

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
