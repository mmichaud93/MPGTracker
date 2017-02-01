package com.mpgtracker.tallmatt.mpgtracker.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.mpgtracker.tallmatt.mpgtracker.models.Car;
import com.mpgtracker.tallmatt.mpgtracker.models.DataPoint;
import com.mpgtracker.tallmatt.mpgtracker.ui.chart.XAxisValueFormatter;
import com.mpgtracker.tallmatt.mpgtracker.ui.dialogfragments.DialogListener;
import com.mpgtracker.tallmatt.mpgtracker.ui.dialogfragments.NewCarDialogFragment;
import com.mpgtracker.tallmatt.mpgtracker.ui.dialogfragments.NewDataPointDialogFragment;
import com.mpgtracker.tallmatt.mpgtracker.utils.DataUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    Car currentActiveCar;
    private CarDatabaseHelper carDatabaseHelper;
    private DataPointDatabaseHelper dataPointDatabaseHelper;
    DecimalFormat decimalFormat = new DecimalFormat("##.###");

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

        if (true && BuildConfig.DEBUG) {
            carDatabaseHelper.resetTable();
            dataPointDatabaseHelper.resetTable();

            currentActiveCar = DataUtils.generateRandomCar();
            carDatabaseHelper.insertCar(currentActiveCar);
            currentActiveCar.setDataPoints(DataUtils.generateRandomTestData());
            for (DataPoint dataPoint : currentActiveCar.getDataPoints()) {
                dataPointDatabaseHelper.insertDataPoint(dataPoint);
            }

        }

        if (carDatabaseHelper.getNumberOfRows() == 0) {
            // no cars saved, prompt user to add one
            FragmentManager fm = getSupportFragmentManager();
            NewCarDialogFragment newCarDialogFragment = NewCarDialogFragment.newInstance(false, new DialogListener() {
                @Override
                public void onDialogClose(Bundle args) {
                    currentActiveCar = (Car) args.getSerializable(NewCarDialogFragment.KEY_NEW_CARMODEL);
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
                NewDataPointDialogFragment newDataPointDialogFragment = NewDataPointDialogFragment.newInstance(null, new DialogListener() {
                    @Override
                    public void onDialogClose(Bundle args) {

                        DataPoint dataPoint = (DataPoint) args.get(NewDataPointDialogFragment.KEY_NEW_DATA_POINT);

                        currentActiveCar.addDataPoint(dataPoint);
                        dataPointDatabaseHelper.insertDataPoint(dataPoint);

                        fillOutChart();
                    }
                });
                newDataPointDialogFragment.show(fm, "dialog_new_data_point");
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(currentActiveCar.getName());
            getSupportActionBar().setSubtitle(currentActiveCar.getYear() + " " + currentActiveCar.getMake() + " " +currentActiveCar.getModel());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_edit_car:
                Intent intent = new Intent(this, CarEditActivity.class);
                intent.putExtra(CarEditActivity.KEY_CURRENT_ACTIVE_CAR, currentActiveCar);
                startActivityForResult(intent, CarEditActivity.REQUEST, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CarEditActivity.REQUEST && resultCode == CarEditActivity.RESULT_SAVE)    {
            currentActiveCar = (Car) data.getSerializableExtra(CarEditActivity.KEY_CURRENT_ACTIVE_CAR);
            carDatabaseHelper.insertCar(currentActiveCar);
            initWithLoadedCar();
        }
    }

    private void fillOutDashboard() {
        mpgOverallText.setText(getResources().getString(R.string.avergae_mpg_text, decimalFormat.format(DataUtils.getAverageMPG(currentActiveCar.getDataPoints()))));
    }

    private void fillOutChart() {

        List<Entry> entries = new ArrayList<>();

        for (DataPoint data : currentActiveCar.getDataPoints()) {

            entries.add(new Entry(data.dateAdded, data.milesTravelled/data.gallonsPutIn));
        }
        Collections.sort(entries, new EntryXComparator());

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.mpg));
        dataSet.setColor(ContextCompat.getColor(this, R.color.colorAccent));
        dataSet.setLineWidth(2);
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSet.setCircleColorHole(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSet.setCircleRadius(2);
        dataSet.setValueTextSize(16);

        LineData lineData = new LineData(dataSet);
        dataChart.setData(lineData);
        dataChart.invalidate();
        dataChart.setDescription(null);
        dataChart.getXAxis().setValueFormatter(new XAxisValueFormatter());
    }
}
