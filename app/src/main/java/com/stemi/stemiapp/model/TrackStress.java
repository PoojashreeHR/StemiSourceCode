package com.stemi.stemiapp.model;

import java.util.Date;

/**
 * Created by praburaam on 04/09/17.
 */

public class TrackStress {
    private String userId;
    private Date dateTime;
    private boolean stressed;
    private boolean yoga;
    private boolean meditation;
    private boolean hobbies;

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

    public boolean isYoga() {
        return yoga;
    }

    public void setYoga(boolean yoga) {
        this.yoga = yoga;
    }

    public boolean isMeditation() {
        return meditation;
    }

    public void setMeditation(boolean meditation) {
        this.meditation = meditation;
    }

    public boolean isHobbies() {
        return hobbies;
    }

    public void setHobbies(boolean hobbies) {
        this.hobbies = hobbies;
    }
}
