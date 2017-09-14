package com.stemi.stemiapp.graphs;

import com.jjoe64.graphview.series.DataPoint;

import java.util.Date;

/**
 * Created by praburaam on 28/08/17.
 */

public class FlaggedDataPoint extends DataPoint {
    private boolean flag;
    public FlaggedDataPoint(double x, double y, boolean flag) {
        super(x, y);
        this.flag = flag;
    }

    public FlaggedDataPoint(Date x, double y, boolean flag) {
        super(x, y);
        this.flag = flag;
    }

    public boolean getFlag(){
        return flag;
    }
}
