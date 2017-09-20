package com.stemi.stemiapp.graphs;

import android.content.Context;

import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by praburaam on 07/08/17.
 */

public class YAxisValueFormatter extends DateAsXAxisLabelFormatter {
    private List<String> yAxisLabels;
    private SimpleDateFormat mSimpleDateFormat;

    public YAxisValueFormatter(Context context, List<String> yAxes) {
        super(context);
        this.yAxisLabels = yAxes;
        //mDateFormat = new SimpleDateFormat("dd/MM/yy");
    }

    @Override
    public String formatLabel(double value, boolean isValueX) {
        mSimpleDateFormat = new SimpleDateFormat("dd/MM");
        if (isValueX) {
            // format as date
            mCalendar.setTimeInMillis((long) value);
            return mSimpleDateFormat.format(mCalendar.getTimeInMillis());
        } else {
            return yAxisLabels.get((int) value);
        }
    }
}
