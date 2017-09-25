package com.stemi.stemiapp.model.apiModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by praburaam on 22/09/17.
 */

public class Hospital implements Parcelable{
    @SerializedName("hospital_id")
    private String hospitalId;
    @SerializedName("name")
    private String hospitalName;
    @SerializedName("address")
    private String hospitalAddress;
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lon")
    private double longitude;

    protected Hospital(Parcel in) {
        hospitalId = in.readString();
        hospitalName = in.readString();
        hospitalAddress = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        @Override
        public Hospital createFromParcel(Parcel in) {
            return new Hospital(in);
        }

        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(hospitalId);
        parcel.writeString(hospitalName);
        parcel.writeString(hospitalAddress);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
