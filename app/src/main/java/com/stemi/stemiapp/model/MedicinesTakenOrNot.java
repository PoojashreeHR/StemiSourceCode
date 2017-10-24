package com.stemi.stemiapp.model;

/**
 * Created by Pooja on 13-10-2017.
 */

public class MedicinesTakenOrNot {

    private String date;
    private String medName;
    private  Boolean TakenorNot;
    private String medTime;
    private String type;
    private String duration;
    private int medColor;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getMedColor() {
        return medColor;
    }

    public void setMedColor(int medColor) {
        this.medColor = medColor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMedTime() {
        return medTime;
    }

    public void setMedTime(String medTime) {
        this.medTime = medTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public Boolean getTakenorNot() {
        return TakenorNot;
    }

    public void setTakenorNot(Boolean takenorNot) {
        TakenorNot = takenorNot;
    }
}
