package com.mpgtracker.tallmatt.mpgtracker.ui.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by TallMatt on 1/28/2017.
 */

public class XAxisValueFormatter implements IAxisValueFormatter {

    private DateFormat dateFormat;

    public XAxisValueFormatter() {
        dateFormat = new SimpleDateFormat("MMM dd", Locale.US);
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        Date date = new Date();
        date.setTime((long) value);
        return dateFormat.format(date);
    }
}
