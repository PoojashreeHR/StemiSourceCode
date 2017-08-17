package com.stemi.stemiapp.samples;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * Created by praburaam on 19/07/17.
 */

public class XAxisValueFormatter implements IAxisValueFormatter {

    private List<String> datesList;

    public XAxisValueFormatter(List<String> dates){
        this.datesList = dates;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        String stringValue;
        if (datesList.size() >= 0 && value >= 0) {
            if (value < datesList.size()) {
                stringValue = datesList.get((int) value);
            } else {
                stringValue = "";
            }
        }else {
            stringValue = "";
        }
        return stringValue;
    }
}
