package com.stemi.stemiapp.graphs;

import android.content.Context;

import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;

import java.text.SimpleDateFormat;

/**
 * Created by praburaam on 21/09/17.
 */

public class XAxisDateFormatter extends DateAsXAxisLabelFormatter {
    private SimpleDateFormat mSimpleDateFormat;

    public XAxisDateFormatter(Context context) {
        super(context);
    }

    @Override
    public String formatLabel(double value, boolean isValueX) {
        mSimpleDateFormat = new SimpleDateFormat("dd/MM");
        if (isValueX) {
            // format as date
            mCalendar.setTimeInMillis((long) value);
            return mSimpleDateFormat.format(mCalendar.getTimeInMillis());
        }
        else {
            return super.formatLabel(value, isValueX);
        }
    }
}
