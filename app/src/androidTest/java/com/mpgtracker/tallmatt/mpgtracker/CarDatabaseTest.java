package com.mpgtracker.tallmatt.mpgtracker;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.mpgtracker.tallmatt.mpgtracker.database.CarDatabaseHelper;
import com.mpgtracker.tallmatt.mpgtracker.models.CarModel;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CarDatabaseTest {

    private static CarDatabaseHelper carDatabaseHelper;

    private static String testCarMake = "testMake";
    private static String testCarModel = "testModel";
    private static String testCarYear = "testYear";
    private static String testCarLicense = "testLicense";
    private static String testCarName = "testName";
    private static CarModel testCar;

    private String testUpdate = "_update";

    @BeforeClass
    public static void setUp() {
        carDatabaseHelper = new CarDatabaseHelper(InstrumentationRegistry.getTargetContext());
        testCar = new CarModel(-1, testCarMake, testCarModel, testCarYear, testCarLicense, testCarName);
    }

    @After
    public void tearDown() {
        carDatabaseHelper.resetTable();
    }

    @Test
    public void test_insert() throws Exception {

        long id = carDatabaseHelper.insertCar(testCar);

        assertEquals(1, carDatabaseHelper.getNumberOfRows());
        assertEquals(id, testCar.getId());
    }

    @Test
    public void test_update() throws Exception {

        carDatabaseHelper.insertCar(testCar);

        testCar.setMake(testCarMake + testUpdate);
        testCar.setModel(testCarModel + testUpdate);
        testCar.setYear(testCarYear + testUpdate);
        testCar.setLicense(testCarLicense + testUpdate);
        testCar.setName(testCarName + testUpdate);

        assertTrue(carDatabaseHelper.updateCar(testCar));

        CarModel[] models = carDatabaseHelper.getAllCars();

        assertEquals(testCarMake + testUpdate, models[0].getMake());
        assertEquals(testCarModel + testUpdate, models[0].getModel());
        assertEquals(testCarYear + testUpdate, models[0].getYear());
        assertEquals(testCarLicense + testUpdate, models[0].getLicense());
        assertEquals(testCarName + testUpdate, models[0].getName());
    }

    @Test
    public void test_getAllCars() throws Exception {

        carDatabaseHelper.insertCar(testCar);

        CarModel[] models = carDatabaseHelper.getAllCars();
        assertEquals(1, models.length);
    }

    @Test
    public void test_delete() throws Exception {

        carDatabaseHelper.insertCar(testCar);
        carDatabaseHelper.deleteCar(testCar);

        assertEquals(0, carDatabaseHelper.getNumberOfRows());
    }
}
