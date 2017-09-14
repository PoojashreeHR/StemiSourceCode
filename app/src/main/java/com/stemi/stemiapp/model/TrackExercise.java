package com.stemi.stemiapp.model;

import java.util.Date;

/**
 * Created by praburaam on 04/09/17.
 */

public class TrackExercise {
    private String userId;
    private Date dateTime;
    private boolean exercised;

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

    public boolean isExercised() {
        return exercised;
    }

    public void setExercised(boolean exercised) {
        this.exercised = exercised;
    }
}
