package com.mpgtracker.tallmatt.mpgtracker.ui;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.mpgtracker.tallmatt.mpgtracker.BuildConfig;
import com.mpgtracker.tallmatt.mpgtracker.R;
import com.mpgtracker.tallmatt.mpgtracker.database.CarDatabaseHelper;
import com.mpgtracker.tallmatt.mpgtracker.database.DataPointDatabaseHelper;
import com.mpgtracker.tallmatt.mpgtracker.models.CarModel;
import com.mpgtracker.tallmatt.mpgtracker.models.DataPointModel;
import com.mpgtracker.tallmatt.mpgtracker.ui.chart.XAxisValueFormatter;
import com.mpgtracker.tallmatt.mpgtracker.ui.dialogfragments.DialogListener;
import com.mpgtracker.tallmatt.mpgtracker.ui.dialogfragments.NewCarDialogFragment;
import com.mpgtracker.tallmatt.mpgtracker.ui.dialogfragments.NewDataPointDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    CarModel currentActiveCar;
    private CarDatabaseHelper carDatabaseHelper;
    private DataPointDatabaseHelper dataPointDatabaseHelper;

    @BindView(R.id.main_car_data)
    LinearLayout carDataLayout;
    @BindView(R.id.main_overall_mpg)
    TextView mpgOverallText;
    @BindView(R.id.main_6_month_mpg)
    TextView mpg6MonthText;
    @BindView(R.id.main_overall_gpd)
    TextView gpdOverallText;
    @BindView(R.id.main_6_month_gpd)
    TextView gpd6MonthText;
    @BindView(R.id.main_overall_mpgpd)
    TextView mpgpdOverallText;
    @BindView(R.id.main_6_month_mpgpd)
    TextView mpgpd6MonthText;
    @BindView(R.id.main_chart)
    LineChart dataChart;
    @BindView(R.id.main_add_data_point)
    Button addDataPointButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // if there are no cars saved, prompt user to add one
        carDatabaseHelper = new CarDatabaseHelper(this);
        dataPointDatabaseHelper = new DataPointDatabaseHelper(this);

        if (BuildConfig.DEBUG) {
            //carDatabaseHelper.resetTable();
        }

        if (carDatabaseHelper.getNumberOfRows() == 0) {
            // no cars saved, prompt user to add one
            FragmentManager fm = getSupportFragmentManager();
            NewCarDialogFragment newCarDialogFragment = NewCarDialogFragment.newInstance(false, new DialogListener() {
                @Override
                public void onDialogClose(Bundle args) {
                    currentActiveCar = (CarModel) args.getSerializable(NewCarDialogFragment.KEY_NEW_CARMODEL);
                    carDatabaseHelper.insertCar(currentActiveCar);
                    initWithLoadedCar();
                }
            });
            newCarDialogFragment.show(fm, "dialog_new_car");
        } else {
            currentActiveCar = carDatabaseHelper.getAllCars()[0];

            initWithLoadedCar();
        }
    }

    private void initWithLoadedCar() {

        // load the data points
        currentActiveCar.setDataPoints(dataPointDatabaseHelper.getAllDataPoints(currentActiveCar.getId()));
        if (currentActiveCar.getDataPoints().size() > 0) {
            // fill out the texts views
            fillOutDashboard();
            fillOutChart();
        }

        addDataPointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // launch the dialog to input a new data point
                FragmentManager fm = getSupportFragmentManager();
                NewDataPointDialogFragment newDataPointDialogFragment = NewDataPointDialogFragment.newInstance(new DialogListener() {
                    @Override
                    public void onDialogClose(Bundle args) {

                        DataPointModel dataPointModel = (DataPointModel) args.get(NewDataPointDialogFragment.KEY_NEW_DATA_POINT);

                        currentActiveCar.addDataPoint(dataPointModel);
                        dataPointDatabaseHelper.insertDataPoint(dataPointModel);

                        fillOutChart();
                    }
                });
                newDataPointDialogFragment.show(fm, "dialog_new_data_point");
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(currentActiveCar.getName());
        }
    }

    private void fillOutDashboard() {

    }

    private void fillOutChart() {

        List<Entry> entries = new ArrayList<>();

        for (DataPointModel data : currentActiveCar.getDataPoints()) {

            entries.add(new Entry(data.dateAdded, data.milesTravelled/data.gallonsPutIn));
        }
        Collections.sort(entries, new EntryXComparator());

        LineDataSet dataSet = new LineDataSet(entries, "MPG");
        dataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSet.setLineWidth(2);
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.colorAccent));
        dataSet.setCircleColorHole(ContextCompat.getColor(this, R.color.colorAccent));
        dataSet.setCircleRadius(2);

        LineData lineData = new LineData(dataSet);
        dataChart.setData(lineData);
        dataChart.invalidate();

        dataChart.getXAxis().setValueFormatter(new XAxisValueFormatter());
    }
}
