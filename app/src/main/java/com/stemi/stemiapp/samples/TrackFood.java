package com.stemi.stemiapp.samples;

import java.util.Date;

/**
 * Created by praburaam on 11/08/17.
 */

public class TrackFood {
    private String userId;
    private Date dateTime;
    private boolean withinLimit;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isWithinLimit() {
        return withinLimit;
    }

    public void setWithinLimit(boolean withinLimit) {
        this.withinLimit = withinLimit;
    }
}
