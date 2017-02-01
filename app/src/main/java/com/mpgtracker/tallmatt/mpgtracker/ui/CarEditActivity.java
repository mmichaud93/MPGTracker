package com.mpgtracker.tallmatt.mpgtracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mpgtracker.tallmatt.mpgtracker.R;
import com.mpgtracker.tallmatt.mpgtracker.database.DataPointDatabaseHelper;
import com.mpgtracker.tallmatt.mpgtracker.models.Car;
import com.mpgtracker.tallmatt.mpgtracker.models.DataPoint;
import com.mpgtracker.tallmatt.mpgtracker.ui.dialogfragments.DialogListener;
import com.mpgtracker.tallmatt.mpgtracker.ui.dialogfragments.NewDataPointDialogFragment;
import com.mpgtracker.tallmatt.mpgtracker.ui.lists.EditDataPointAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TallMatt on 1/29/2017.
 */

public class CarEditActivity extends AppCompatActivity {

    public static final String KEY_CURRENT_ACTIVE_CAR = "key_current_active_car";

    public static final int REQUEST = 1000;
    public static final int RESULT_SAVE = 1001;
    public static final int RESULT_DISCARD = 1002;

    private Car currentActiveCar;
    private DataPointDatabaseHelper dataPointDatabaseHelper;
    private ArrayList<DataPoint> deletedPoints = new ArrayList<>();

    @BindView(R.id.edit_car_required)
    TextView requiredText;
    @BindView(R.id.edit_car_make)
    EditText makeEdit;
    @BindView(R.id.edit_car_model)
    EditText modelEdit;
    @BindView(R.id.edit_car_year)
    EditText yearEdit;
    @BindView(R.id.edit_car_license)
    EditText licenseEdit;
    @BindView(R.id.edit_car_name)
    EditText nameEdit;
    @BindView(R.id.edit_data_points)
    ListView dataPointsList;

    EditDataPointAdapter editDataPointAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getIntent().hasExtra(KEY_CURRENT_ACTIVE_CAR)) {
            finish();
        }

        setContentView(R.layout.activity_car_edit);
        ButterKnife.bind(this);
        dataPointDatabaseHelper = new DataPointDatabaseHelper(this);

        currentActiveCar = (Car) getIntent().getSerializableExtra(KEY_CURRENT_ACTIVE_CAR);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Car");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        makeEdit.setText(currentActiveCar.getMake());
        modelEdit.setText(currentActiveCar.getModel());
        yearEdit.setText(currentActiveCar.getYear());
        licenseEdit.setText(currentActiveCar.getLicense());
        nameEdit.setText(currentActiveCar.getName());

        editDataPointAdapter = new EditDataPointAdapter(this, currentActiveCar.getDataPoints());
        dataPointsList.setAdapter(editDataPointAdapter);
        dataPointsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                FragmentManager fm = getSupportFragmentManager();
                NewDataPointDialogFragment newDataPointDialogFragment = NewDataPointDialogFragment.newInstance(currentActiveCar.getDataPoints().get(position), new DialogListener() {
                    @Override
                    public void onDialogClose(Bundle args) {

                        if (args != null) {

                            DataPoint dataPoint = (DataPoint) args.get(NewDataPointDialogFragment.KEY_NEW_DATA_POINT);

                            currentActiveCar.updateDataPoint(dataPoint);
                            dataPointDatabaseHelper.updateDataPoint(dataPoint);
                        } else {

                            DataPoint removedDataPoint = currentActiveCar.getDataPoints().get(position);
                            deletedPoints.add(removedDataPoint);
                            currentActiveCar.deleteDataPoint(removedDataPoint);

                            editDataPointAdapter.notifyDataSetChanged();
                        }
                    }
                });
                newDataPointDialogFragment.show(fm, "dialog_new_data_point");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                setResult(RESULT_DISCARD);
                finish();
                return true;
            case R.id.edit_menu_save_car:

                if (nameEdit.getText().toString().isEmpty()) {
                    requiredText.setVisibility(View.VISIBLE);
                    return false;
                }

                saveCar();

                Intent data =  new Intent();
                data.putExtra(KEY_CURRENT_ACTIVE_CAR, currentActiveCar);

                setResult(RESULT_SAVE, data);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveCar() {
        currentActiveCar.setMake(makeEdit.getText().toString());
        currentActiveCar.setModel(modelEdit.getText().toString());
        currentActiveCar.setYear(yearEdit.getText().toString());
        currentActiveCar.setLicense(licenseEdit.getText().toString());
        currentActiveCar.setName(nameEdit.getText().toString());

        for (DataPoint dataPoint : deletedPoints) {
            dataPointDatabaseHelper.deleteDataPoint(dataPoint);
        }
    }
}
