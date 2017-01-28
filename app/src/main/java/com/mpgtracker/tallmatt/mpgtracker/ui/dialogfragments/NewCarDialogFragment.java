package com.mpgtracker.tallmatt.mpgtracker.ui.dialogfragments;

import android.annotation.SuppressLint;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mpgtracker.tallmatt.mpgtracker.R;
import com.mpgtracker.tallmatt.mpgtracker.models.Car;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewCarDialogFragment extends DialogFragment {

    private static final String KEY_CANCELLABLE = "key_cancellable";
    public static final String KEY_NEW_CARMODEL = "key_new_carmodel";

    @BindView(R.id.new_car_make)
    EditText makeEdit;
    @BindView(R.id.new_car_model)
    EditText modelEdit;
    @BindView(R.id.new_car_year)
    EditText yearEdit;
    @BindView(R.id.new_car_license)
    EditText licenseEdit;
    @BindView(R.id.new_car_name)
    EditText nameEdit;
    @BindView(R.id.new_car_required)
    TextView requiredText;
    @BindView(R.id.new_car_cancel)
    Button cancelButton;
    @BindView(R.id.new_car_save)
    Button saveButton;

    private DialogListener dialogListener;

    public static NewCarDialogFragment newInstance(boolean cancellable, DialogListener dialogListener) {
        NewCarDialogFragment newFragment = new NewCarDialogFragment(dialogListener);
        Bundle args = new Bundle();
        args.putBoolean(KEY_CANCELLABLE, cancellable);
        newFragment.setArguments(args);
        return newFragment;
    }

    public NewCarDialogFragment() {
        this(null);
    }

    @SuppressLint("ValidFragment")
    public NewCarDialogFragment(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_new_car, container);
        ButterKnife.bind(this, root);

        this.setCancelable(getArguments().getBoolean(KEY_CANCELLABLE, true));

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean cancellable = getArguments().getBoolean(KEY_CANCELLABLE);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewCarDialogFragment.this.dismiss();
            }
        });
        if (!cancellable) {
            cancelButton.setClickable(cancellable);
            cancelButton.setAlpha(0.5f);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save it
                if (nameEdit.getText().toString().isEmpty()) {
                    requiredText.setVisibility(View.VISIBLE);
                    return;
                }

                if (dialogListener != null) {
                    Bundle args = new Bundle();
                    args.putSerializable(KEY_NEW_CARMODEL, new Car(-1,
                            makeEdit.getText().toString(),
                            modelEdit.getText().toString(),
                            yearEdit.getText().toString(),
                            licenseEdit.getText().toString(),
                            nameEdit.getText().toString()
                    ));

                    dialogListener.onDialogClose(args);

                    dismiss();
                }
            }
        });
    }
}
