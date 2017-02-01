package com.mpgtracker.tallmatt.mpgtracker.utils;

import android.widget.DatePicker;

import java.util.Calendar;

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
