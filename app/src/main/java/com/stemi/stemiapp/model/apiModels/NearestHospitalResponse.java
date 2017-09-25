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

    protected NearestHospitalResponse(Parcel in) {
        hospitalList = in.createTypedArrayList(Hospital.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(hospitalList);
    }
}
