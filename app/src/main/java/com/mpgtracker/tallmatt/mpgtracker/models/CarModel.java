package com.mpgtracker.tallmatt.mpgtracker.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by TallMatt on 1/24/2017.
 */

public class CarModel implements Serializable {

    long id;

    String make;
    String model;
    String year;
    String license;
    String name;
    List<DataPointModel> dataPoints;

    public CarModel(long id, String make, String model, String year, String license, String name) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.license = license;
        this.name = name;
        dataPoints = new ArrayList<>();
    }

    public void addDataPoint(DataPointModel dataPointModel) {

        dataPointModel.carId = getId();
        dataPoints.add(dataPointModel);
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataPointModel> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPointModel> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
