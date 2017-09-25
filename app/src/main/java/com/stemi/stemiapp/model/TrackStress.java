package com.stemi.stemiapp.model;

import java.util.Date;

/**
 * Created by praburaam on 04/09/17.
 */

public class TrackStress {
    private String userId;
    private Date dateTime;
    private boolean stressed;
    private double meditationHrs;
    private double yogaHrs;

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

    public boolean isStressed() {
        return stressed;
    }

    public void setStressed(boolean stressed) {
        this.stressed = stressed;
    }

    public double getMeditationHrs() {
        return meditationHrs;
    }

    public void setMeditationHrs(double meditationHrs) {
        this.meditationHrs = meditationHrs;
    }

    public double getYogaHrs() {
        return yogaHrs;
    }

    public void setYogaHrs(double yogaHrs) {
        this.yogaHrs = yogaHrs;
    }
}
