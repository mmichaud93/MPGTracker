package com.mpgtracker.tallmatt.mpgtracker.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private Car carDuplicate;
    private DataPointDatabaseHelper dataPointDatabaseHelper;
    private ArrayList<DataPoint> deletedPoints = new ArrayList<>();
    private boolean dirty = false;

    LinearLayout header;
    TextView requiredText;
    EditText makeEdit;
    EditText modelEdit;
    EditText yearEdit;
    EditText licenseEdit;
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
        carDuplicate = Car.duplicate(currentActiveCar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Car");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        header = (LinearLayout) layoutInflater.inflate(R.layout.adapter_edit_header, null, false);
        dataPointsList.addHeaderView(header);

        makeEdit = (EditText) header.findViewById(R.id.edit_car_make);
        makeEdit.setText(currentActiveCar.getMake());
        modelEdit = (EditText) header.findViewById(R.id.edit_car_model);
        modelEdit.setText(currentActiveCar.getModel());
        yearEdit = (EditText) header.findViewById(R.id.edit_car_year);
        yearEdit.setText(currentActiveCar.getYear());
        licenseEdit = (EditText) header.findViewById(R.id.edit_car_license);
        licenseEdit.setText(currentActiveCar.getLicense());
        nameEdit = (EditText) header.findViewById(R.id.edit_car_name);
        nameEdit.setText(currentActiveCar.getName());

        requiredText = (TextView) header.findViewById(R.id.edit_car_required);

        editDataPointAdapter = new EditDataPointAdapter(this, currentActiveCar.getDataPoints());
        dataPointsList.setAdapter(editDataPointAdapter);
        dataPointsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int fixedPosition = position - 1;
                FragmentManager fm = getSupportFragmentManager();
                NewDataPointDialogFragment newDataPointDialogFragment = NewDataPointDialogFragment.newInstance(currentActiveCar.getDataPoints().get(fixedPosition), new DialogListener() {
                    @Override
                    public void onDialogClose(Bundle args) {

                        if (args != null) {

                            DataPoint dataPoint = (DataPoint) args.get(NewDataPointDialogFragment.KEY_NEW_DATA_POINT);

                            currentActiveCar.updateDataPoint(dataPoint);
                            dataPointDatabaseHelper.updateDataPoint(dataPoint);
                        } else {

                            DataPoint removedDataPoint = currentActiveCar.getDataPoints().get(fixedPosition);
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

                currentActiveCar.setMake(makeEdit.getText().toString());
                currentActiveCar.setModel(modelEdit.getText().toString());
                currentActiveCar.setYear(yearEdit.getText().toString());
                currentActiveCar.setLicense(licenseEdit.getText().toString());
                currentActiveCar.setName(nameEdit.getText().toString());

                // check is dirty
                if (!Car.haveEqualValues(currentActiveCar, carDuplicate)) {

                    areYouSure(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                            for (DataPoint dataPoint : deletedPoints) {
                                dataPointDatabaseHelper.deleteDataPoint(dataPoint);
                            }

                            Intent data = new Intent();
                            data.putExtra(KEY_CURRENT_ACTIVE_CAR, currentActiveCar);

                            setResult(RESULT_SAVE, data);
                            finish();
                        }
                    });
                } else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void areYouSure(DialogInterface.OnClickListener positiveListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.save_changes);
        builder.setPositiveButton(getString(R.string.yes), positiveListener);
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                setResult(RESULT_DISCARD);
                finish();
            }
        });
        builder.setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
