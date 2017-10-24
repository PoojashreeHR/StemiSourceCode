package com.stemi.stemiapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pooja on 13-10-2017.
 */

public class MedicinesTakenOrNot implements Parcelable{

    private String date;
    private String medName;
    private  Boolean TakenorNot;
    private String medTime;
    private String type;
    private String duration;
    private int medColor;

    public MedicinesTakenOrNot(){}

    protected MedicinesTakenOrNot(Parcel in) {
        date = in.readString();
        medName = in.readString();
        medTime = in.readString();
        type = in.readString();
        duration = in.readString();
        medColor = in.readInt();
    }

    public static final Creator<MedicinesTakenOrNot> CREATOR = new Creator<MedicinesTakenOrNot>() {
        @Override
        public MedicinesTakenOrNot createFromParcel(Parcel in) {
            return new MedicinesTakenOrNot(in);
        }

        @Override
        public MedicinesTakenOrNot[] newArray(int size) {
            return new MedicinesTakenOrNot[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(medName);
        parcel.writeString(medTime);
        parcel.writeString(type);
        parcel.writeString(duration);
        parcel.writeInt(medColor);
    }
}
