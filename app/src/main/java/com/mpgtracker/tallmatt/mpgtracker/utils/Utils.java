package com.mpgtracker.tallmatt.mpgtracker.utils;

import android.util.Log;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by TallMatt on 1/25/2017.
 */

public class Utils {

    public static long dateFromDatePicker(DatePicker datePicker) {

        if (datePicker == null) {
            return -1;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        return calendar.getTimeInMillis();
    }
}
