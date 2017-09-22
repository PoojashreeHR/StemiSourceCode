package com.stemi.stemiapp.model;

/**
 * Created by Pooja on 13-09-2017.
 */

public class MedicineInfo {
    private   String medicineName;
    private  int medicineColor;
    private  String medicineType;

    public String getMedicineType() {
        return medicineType;
    }

    public void setMedicineType(String medicineType) {
        this.medicineType = medicineType;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getMedicineColor() {
        return medicineColor;
    }

    public void setMedicineColor(int medicineColor) {
        this.medicineColor = medicineColor;
    }
}
