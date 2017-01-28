package com.mpgtracker.tallmatt.mpgtracker;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mpgtracker.tallmatt.mpgtracker.database.DataPointDatabaseHelper;
import com.mpgtracker.tallmatt.mpgtracker.models.DataPoint;
import com.mpgtracker.tallmatt.mpgtracker.models.LocationModel;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class DataPointDatabaseTest {

    private static DataPointDatabaseHelper dataPointDatabaseHelper;

    private static long testCarId = 1234;
    private static float testGallons = 0.1234f;
    private static float testMiles = 0.3421f;
    private static float testMoney = 5678.9871f;
    private static long testDate = new Date().getTime();
    private static LocationModel testLocation = new LocationModel(246.80f, 135.79f);
    private static DataPoint testDataPoint;

    private String testUpdate = "_update";
    private float testUpdate_float = 9999.99f;
    private float testUpdate_long = 55555;
    private long testUpdate_date = new Date().getTime() + 500000;
    private LocationModel testUpdate_location;

    @BeforeClass
    public static void setUp() {
        dataPointDatabaseHelper = new DataPointDatabaseHelper(InstrumentationRegistry.getTargetContext());
        testDataPoint = new DataPoint(-1, testCarId, testGallons, testMiles, testMoney, testDate, testLocation);
    }

    @After
    public void tearDown() {
        dataPointDatabaseHelper.resetTable();
    }

    @Test
    public void test_insert() throws Exception {

        long id = dataPointDatabaseHelper.insertDataPoint(testDataPoint);

        assertEquals(1, dataPointDatabaseHelper.getNumberOfRows());
        assertEquals(id, testDataPoint.id);
    }

    @Test
    public void test_update() throws Exception {

        dataPointDatabaseHelper.insertDataPoint(testDataPoint);

        testDataPoint.carId += testUpdate_long;
        testDataPoint.gallonsPutIn += testUpdate_float;
        testDataPoint.milesTravelled += testUpdate_float;
        testDataPoint.moneySpent += testUpdate_float;
        testDataPoint.dateAdded += testUpdate_date;
        testUpdate_location = new LocationModel(testDataPoint.location.lat + testUpdate_float, testDataPoint.location.lon + testUpdate_float);
        testDataPoint.location = testUpdate_location;

        assertTrue(dataPointDatabaseHelper.updateDataPoint(testDataPoint));

        ArrayList<DataPoint> models = dataPointDatabaseHelper.getAllDataPoints(testDataPoint.carId);

        assertEquals(testCarId + testUpdate_long, models.get(0).carId, 0);
        assertEquals(testGallons + testUpdate_float, models.get(0).gallonsPutIn, 0);
        assertEquals(testMiles + testUpdate_float, models.get(0).milesTravelled, 0);
        assertEquals(testMoney + testUpdate_float, models.get(0).moneySpent, 0);
        assertEquals(testDate + testUpdate_date, models.get(0).dateAdded);
        assertEquals(testUpdate_location.toString(), models.get(0).getLocationString());
    }

    @Test
    public void test_getAllDataPoints() throws Exception {

        dataPointDatabaseHelper.insertDataPoint(testDataPoint);

        ArrayList<DataPoint> models = dataPointDatabaseHelper.getAllDataPoints(testDataPoint.carId);
        assertEquals(1, models.size());
    }

    @Test
    public void test_delete() throws Exception {

        dataPointDatabaseHelper.insertDataPoint(testDataPoint);
        dataPointDatabaseHelper.deleteDataPoint(testDataPoint);

        assertEquals(0, dataPointDatabaseHelper.getNumberOfRows());
    }
}
