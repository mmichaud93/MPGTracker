package com.mpgtracker.tallmatt.mpgtracker.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TallMatt on 1/24/2017.
 */

public class Car implements Serializable {

    long id;

    String make;
    String model;
    String year;
    String license;
    String name;
    List<DataPoint> dataPoints;

    public Car(long id, String make, String model, String year, String license, String name) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.license = license;
        this.name = name;
        dataPoints = new ArrayList<>();
    }

    public void addDataPoint(DataPoint dataPoint) {

        dataPoint.carId = getId();
        dataPoints.add(dataPoint);
    }

    public void updateDataPoint(DataPoint dataPoint) {
        for (int i = 0; i < dataPoints.size(); i++) {
            if (getDataPoints().get(i).id == dataPoint.id) {
                getDataPoints().set(i, dataPoint);
                return;
            }
        }
    }

    public void deleteDataPoint(DataPoint dataPoint) {
        int r = 0;
        for (int i = 0; i < dataPoints.size(); i++) {
            if (getDataPoints().get(i).id == dataPoint.id) {
                r = i;
                break;
            }
        }
        getDataPoints().remove(r);
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

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        for (DataPoint dataPoint : dataPoints) {
            addDataPoint(dataPoint);
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static Car duplicate(Car car) {
        Car duplicate = new Car(
                car.getId(), car.make, car.model, car.year, car.license, car.name
        );
        duplicate.setDataPoints(car.getDataPoints());

        return duplicate;
    }

    public static boolean haveEqualValues(Car car1, Car car2) {
        if (car1 == null || car2 == null) {
            return false;
        }

        boolean sameDataPoints = false;
        if (car1.getDataPoints().size() != car2.getDataPoints().size()) {
            return false;
        }
        for (int i = 0; i < car1.getDataPoints().size(); i++) {
            DataPoint d1 = car1.getDataPoints().get(i);
            DataPoint d2 = car2.getDataPoints().get(i);
            if ((d1 == null && d2 != null) || (d1 != null && d2 == null)) {
                return false;
            }
            if (d1 == null && d2 == null) {
//                continue;
            }
            if (d1.gallonsPutIn != d2.gallonsPutIn ||
                    d1.milesTravelled != d2.milesTravelled ||
                    d1.moneySpent != d2.moneySpent ||
                    d1.id != d2.id ||
                    d1.carId != d2.carId ||
                    d1.dateAdded != d2.dateAdded) {
                return false;
            }
        }

        return car1.getId() == car2.getId() &&
                car1.getMake().equals(car2.getMake()) &&
                car1.getModel().equals(car2.getModel()) &&
                car1.getYear().equals(car2.getYear()) &&
                car1.getLicense().equals(car2.getLicense()) &&
                car1.getName().equals(car2.getName());

    }
}
