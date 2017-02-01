package com.mpgtracker.tallmatt.mpgtracker.ui.lists;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mpgtracker.tallmatt.mpgtracker.R;
import com.mpgtracker.tallmatt.mpgtracker.models.DataPoint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditDataPointAdapter extends ArrayAdapter<DataPoint> {



    public EditDataPointAdapter(Context context, List<DataPoint> dataPoints) {
        super(context, R.layout.adapter_edit_data_point, dataPoints);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_edit_data_point, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DataPoint dataPoint = getItem(position);
        if (dataPoint == null) {
            return convertView;
        }

        SpannableString spannableStringGallons = new SpannableString(getContext().getString(R.string.edit_gallons, Float.toString(dataPoint.gallonsPutIn)));
        spannableStringGallons.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.gallonsText.setText(spannableStringGallons);

        SpannableString spannableStringMiles = new SpannableString(getContext().getString(R.string.edit_miles, Float.toString(dataPoint.milesTravelled)));
        spannableStringMiles.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.milesText.setText(spannableStringMiles);

        SpannableString spannableStringDollars = new SpannableString(getContext().getString(R.string.edit_dollars, Float.toString(dataPoint.moneySpent)));
        spannableStringDollars.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.dollarsText.setText(spannableStringDollars);

        Date date = new Date();
        date.setTime(dataPoint.dateAdded);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        SpannableString spannableStringDate = new SpannableString(getContext().getString(R.string.edit_date, simpleDateFormat.format(date)));
        spannableStringDate.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.dateText.setText(spannableStringDate);

        return convertView;
    }

    public static class ViewHolder {

        @BindView(R.id.adapter_edit_gallons)
        TextView gallonsText;
        @BindView(R.id.adapter_edit_miles)
        TextView milesText;
        @BindView(R.id.adapter_edit_dollars)
        TextView dollarsText;
        @BindView(R.id.adapter_edit_date)
        TextView dateText;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
