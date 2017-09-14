package com.stemi.stemiapp.model;

import java.util.Date;

/**
 * Created by praburaam on 04/09/17.
 */

public class TrackSmoking {
    private String userId;
    private Date dateTime;
    private boolean smoked;

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

    public boolean isSmoked() {
        return smoked;
    }

    public void setSmoked(boolean smoked) {
        this.smoked = smoked;
    }
}
