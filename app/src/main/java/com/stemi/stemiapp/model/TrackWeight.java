package com.stemi.stemiapp.model;

/**
 * Created by praburaam on 11/09/17.
 */

public class TrackWeight {
    private String userId;
    private int monthIndex;
    private String year;
    private int weight;

    public int getMonthIndex() {
        return monthIndex;
    }

    public void setMonthIndex(int monthIndex) {
        this.monthIndex = monthIndex;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
