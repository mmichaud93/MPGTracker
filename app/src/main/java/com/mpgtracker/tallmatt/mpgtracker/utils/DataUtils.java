package com.mpgtracker.tallmatt.mpgtracker.utils;

import com.mpgtracker.tallmatt.mpgtracker.models.Car;
import com.mpgtracker.tallmatt.mpgtracker.models.DataPoint;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by TallMatt on 1/28/2017.
 */

public class DataUtils {

    public static float getAverage(List<Float> values) {

        if (values.size() == 0) return 0;

        float sum = 0;
        for (float value : values) sum += value;

        return sum / values.size();
    }

    public static float getAverageMPG(List<DataPoint> dataPoints) {

        return getAverageMPG(dataPoints, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public static float getAverageMPG(List<DataPoint> dataPoints, long minTimeBound, long maxTimeBound) {

        if (dataPoints.size() == 0 || minTimeBound > maxTimeBound) return 0;

        float sumMPG = 0;
        for (DataPoint dataPoint : dataPoints) {
            if (dataPoint.dateAdded >= minTimeBound && dataPoint.dateAdded <= maxTimeBound) {
                sumMPG += (dataPoint.milesTravelled / dataPoint.gallonsPutIn);
            }
        }

        return sumMPG / dataPoints.size();
    }

    public static Car generateRandomCar() {
        return new Car(-1, "Tesla", "Model S", "2015", "XXX-XXX", "The Ironicar");
    }

    public static List<DataPoint> generateRandomTestData() {

        Random random = new Random();
        Date date = new Date();
        date.setMonth(8);
        date.setYear(2016);


        ArrayList<DataPoint> dataPoints = new ArrayList<>();

        for (int i = 0; i < 8 + (int) (random.nextFloat() * 2); i++) {
            dataPoints.add(new DataPoint(
                    round(randomRange(10f, 12f), 2),
                    round(randomRange(350f, 400f), 1),
                    round(randomRange(2.32f, 2.42f), 1),
                    date.getTime(),
                    null
            ));

            date.setTime(date.getTime() + randomRange((long) (86164000 * 12), ((long) 86164000 * 16)));
        }

        return dataPoints;
    }

    public static float randomRange(float minInclusive, float maxInclusive) {
        Random random = new Random();

        return minInclusive + random.nextFloat() * (maxInclusive - minInclusive);
    }

    public static long randomRange(long minInclusive, long maxInclusive) {
        Random random = new Random();

        return minInclusive + ((long) (random.nextFloat() * (maxInclusive - minInclusive)));
    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
