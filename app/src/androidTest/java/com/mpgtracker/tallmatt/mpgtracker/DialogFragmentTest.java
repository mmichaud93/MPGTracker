package com.mpgtracker.tallmatt.mpgtracker;

import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;

import com.mpgtracker.tallmatt.mpgtracker.models.Car;
import com.mpgtracker.tallmatt.mpgtracker.models.DataPoint;
import com.mpgtracker.tallmatt.mpgtracker.ui.MainActivity;
import com.mpgtracker.tallmatt.mpgtracker.ui.dialogfragments.DialogListener;
import com.mpgtracker.tallmatt.mpgtracker.ui.dialogfragments.NewCarDialogFragment;
import com.mpgtracker.tallmatt.mpgtracker.ui.dialogfragments.NewDataPointDialogFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by TallMatt on 1/27/2017.
 */

@RunWith(AndroidJUnit4.class)
public class DialogFragmentTest {

    private String testCarMake = "testMake";
    private String testCarModel = "testModel";
    private String testCarYear = "testYear";
    private String testCarLicense = "testLicense";
    private String testCarName = "testName";

    private float testGallons = 0.1234f;
    private float testMiles = 0.3421f;
    private float testMoney = 5678.9871f;
    private long testDate;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void test_carDialogFragment() throws Exception {

        FragmentManager fm = activityTestRule.getActivity().getSupportFragmentManager();
        NewCarDialogFragment carDialogFragment = NewCarDialogFragment.newInstance(false, new DialogListener() {
            @Override
            public void onDialogClose(Bundle args) {
                Car car = (Car) args.get(NewCarDialogFragment.KEY_NEW_CARMODEL);

                assertTrue(car != null);

                assertEquals(testCarMake, car.getMake());
                assertEquals(testCarModel, car.getModel());
                assertEquals(testCarYear, car.getYear());
                assertEquals(testCarLicense, car.getLicense());
                assertEquals(testCarName, car.getName());
            }
        });
        carDialogFragment.show(fm, "dialog_new_car");

        onView(withId(R.id.new_car_make)).perform(typeText(testCarMake));
        onView(withId(R.id.new_car_model)).perform(typeText(testCarModel));
        onView(withId(R.id.new_car_year)).perform(typeText(testCarYear));
        onView(withId(R.id.new_car_license)).perform(typeText(testCarLicense));
        onView(withId(R.id.new_car_name)).perform(typeText(testCarName), closeSoftKeyboard());

        onView(withId(R.id.new_car_save)).perform(click());
    }

    @Test
    public void test_dataPointDialogFragment() throws Exception {

        FragmentManager fm = activityTestRule.getActivity().getSupportFragmentManager();
        NewDataPointDialogFragment dataPointDialogFragment = NewDataPointDialogFragment.newInstance(null, new DialogListener() {
            @Override
            public void onDialogClose(Bundle args) {
                DataPoint dataPoint = (DataPoint) args.get(NewDataPointDialogFragment.KEY_NEW_DATA_POINT);

                assertTrue(dataPoint != null);

                assertEquals(testGallons, dataPoint.gallonsPutIn, 0);
                assertEquals(testMiles, dataPoint.milesTravelled, 0);
                assertEquals(testMoney, dataPoint.moneySpent, 0);
                assertEquals(testDate, dataPoint.dateAdded, 250);
            }
        });
        dataPointDialogFragment.show(fm, "dialog_new_data_point");

        onView(withId(R.id.new_dp_gallons)).perform(typeText(Float.toString(testGallons)));
        onView(withId(R.id.new_dp_miles)).perform(typeText(Float.toString(testMiles)));
        onView(withId(R.id.new_dp_money)).perform(typeText(Float.toString(testMoney)), closeSoftKeyboard());

        testDate = new Date().getTime();

        onView(withId(R.id.new_dp_save)).perform(click());
    }
}
