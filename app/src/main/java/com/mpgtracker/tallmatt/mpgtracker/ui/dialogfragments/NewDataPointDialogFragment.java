package com.mpgtracker.tallmatt.mpgtracker.ui.dialogfragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.mpgtracker.tallmatt.mpgtracker.R;
import com.mpgtracker.tallmatt.mpgtracker.models.DataPoint;
import com.mpgtracker.tallmatt.mpgtracker.utils.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewDataPointDialogFragment extends DialogFragment {

    public static final String KEY_NEW_DATA_POINT = "key_data_point";

    private DialogListener dialogListener;
    private DataPoint currentDataPoint;

    @BindView(R.id.new_dp_gallons)
    EditText gallonsEdit;
    @BindView(R.id.new_dp_miles)
    EditText milesEdit;
    @BindView(R.id.new_dp_money)
    EditText moneyEdit;
    @BindView(R.id.new_dp_date)
    DatePicker datePicker;
    @BindView(R.id.new_dp_use_location)
    CheckBox locationCheck;
    @BindView(R.id.new_dp_required)
    TextView requiredText;
    @BindView(R.id.new_dp_cancel)
    Button cancelButton;
    @BindView(R.id.new_dp_delete)
    Button deleteButton;
    @BindView(R.id.new_dp_save)
    Button saveButton;

    public static NewDataPointDialogFragment newInstance(DataPoint currentDataPoint, DialogListener dialogListener) {
        NewDataPointDialogFragment newFragment = new NewDataPointDialogFragment(dialogListener);
        Bundle args = new Bundle();
        args.putSerializable(KEY_NEW_DATA_POINT, currentDataPoint);
        newFragment.setArguments(args);
        return newFragment;
    }

    public NewDataPointDialogFragment() {
        this(null);
    }

    @SuppressLint("ValidFragment")
    public NewDataPointDialogFragment(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_data_point, container);
        ButterKnife.bind(this, root);

        currentDataPoint = (DataPoint) getArguments().getSerializable(KEY_NEW_DATA_POINT);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewDataPointDialogFragment.this.dismiss();
            }
        });

        if (currentDataPoint != null) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogListener.onDialogClose(null);
                    NewDataPointDialogFragment.this.dismiss();
                }
            });
            deleteButton.setVisibility(View.VISIBLE);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkValidData()) {

                    requiredText.setVisibility(View.VISIBLE);
                    return;
                }

                if (dialogListener != null) {
                    Bundle args = new Bundle();

                    if (currentDataPoint != null) {
                        args.putSerializable(KEY_NEW_DATA_POINT, currentDataPoint);
                    } else {
                        args.putSerializable(KEY_NEW_DATA_POINT, new DataPoint(
                                Float.parseFloat(gallonsEdit.getText().toString()),
                                Float.parseFloat(milesEdit.getText().toString()),
                                Float.parseFloat(moneyEdit.getText().toString()),
                                Utils.dateFromDatePicker(datePicker),
                                null));
                    }

                    dialogListener.onDialogClose(args);
                }

                NewDataPointDialogFragment.this.dismiss();
            }
        });

        if (currentDataPoint != null) {
            gallonsEdit.setText(Float.toString(currentDataPoint.gallonsPutIn));
            milesEdit.setText(Float.toString(currentDataPoint.milesTravelled));
            moneyEdit.setText(Float.toString(currentDataPoint.moneySpent));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentDataPoint.dateAdded);
            datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
    }

    private boolean checkValidData() {

        return !(gallonsEdit.getText().toString().isEmpty() ||
                milesEdit.getText().toString().isEmpty() ||
                moneyEdit.getText().toString().isEmpty()) && !(Float.parseFloat(gallonsEdit.getText().toString()) == 0 || Float.parseFloat(milesEdit.getText().toString()) == 0);

    }
}
