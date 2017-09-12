package com.stemi.stemiapp.model;

/**
 * Created by Pooja on 28-08-2017.
 */

public class HeartSymptomsModel {
    private String SymptomNumb;
    private String Symptoms;
    private int symptomsImg;

    public String getSymptomNumb() {
        return SymptomNumb;
    }

    public void setSymptomNumb(String symptomNumb) {
        SymptomNumb = symptomNumb;
    }

    public String getSymptoms() {
        return Symptoms;
    }

    public void setSymptoms(String symptoms) {
        Symptoms = symptoms;
    }

    public int getSymptomsImg() {
        return symptomsImg;
    }

    public void setSymptomsImg(int symptomsImg) {
        this.symptomsImg = symptomsImg;
    }
}
