package com.stemi.stemiapp.model.apiModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by praburaam on 22/09/17.
 */

public class NearestHospitalResponse implements Parcelable{
    @SerializedName("hospitals")
    private List<Hospital> hospitalList;
    @SerializedName("status")
    private String status;


    protected NearestHospitalResponse(Parcel in) {
        hospitalList = in.createTypedArrayList(Hospital.CREATOR);
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(hospitalList);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NearestHospitalResponse> CREATOR = new Creator<NearestHospitalResponse>() {
        @Override
        public NearestHospitalResponse createFromParcel(Parcel in) {
            return new NearestHospitalResponse(in);
        }

        @Override
        public NearestHospitalResponse[] newArray(int size) {
            return new NearestHospitalResponse[size];
        }
    };

    public List<Hospital> getHospitalList() {
        return hospitalList;
    }

    public void setHospitalList(List<Hospital> hospitalList) {
        this.hospitalList = hospitalList;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
