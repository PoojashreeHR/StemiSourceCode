package com.stemi.stemiapp.model;

/**
 * Created by Pooja on 17-07-2017.
 */

public class HealthAnswers {

    private String values;
    private Boolean isClicked;
    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public Boolean getClicked() {
        return isClicked;
    }

    public void setClicked(Boolean clicked) {
        isClicked = clicked;
    }

    public HealthAnswers() {
    }

    public HealthAnswers(String values, Boolean isClicked) {
        this.values = values;
        this.isClicked = isClicked;
    }
}
