package com.stemi.stemiapp.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.stemi.stemiapp.model.apiModels.Hospital;
import com.stemi.stemiapp.model.apiModels.NearestHospitalResponse;
import com.stemi.stemiapp.rest.ApiClient;
import com.stemi.stemiapp.rest.ApiInterface;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

/**
 * Created by praburaam on 22/09/17.
 */

public class HospitalsLoader extends AsyncTaskLoader<NearestHospitalResponse> {
    private  ApiInterface apiInterface;
    private  double latitude;
    private  double longitude;
    private  String token;

    public HospitalsLoader(Context context, String token, double latitude, double longitude) {
        super(context);
        this.token = token;
        this.latitude = latitude;
        this.longitude = longitude;
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
    }

    @Override
    public NearestHospitalResponse loadInBackground() {
        Log.e("MapsActivity","loadInBackground()");
        Call<NearestHospitalResponse> apiCall = apiInterface.callNearestHsopitalsApi(token,latitude,longitude);
        NearestHospitalResponse hospital = null;
        try {
            hospital = apiCall.execute().body();
            Log.e("MapsActivity", new Gson().toJson(hospital));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hospital;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }


}
