package com.stemi.stemiapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.model.apiModels.SignUpResponseModel;
import com.stemi.stemiapp.model.apiModels.StatusMessageResponse;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.rest.ApiClient;
import com.stemi.stemiapp.rest.ApiInterface;
import com.stemi.stemiapp.utils.AppConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 24-07-2017.
 */

public class LearnFragment extends Fragment implements AppConstants{
    @BindView(R.id.tvTips)
    TextView tvTips;
    ApiInterface apiInterface;
    AppSharedPreference appSharedPreference;
    public LearnFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_learn, container, false);
        ButterKnife.bind(this,view);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        appSharedPreference = new AppSharedPreference(getActivity());
        callGetTipOfTheDay();
        return view;
    }

    public void callGetTipOfTheDay(){

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(new Date());
        Log.e(TAG, "onCreateView: "+ formattedDate );

        String token = appSharedPreference.getUserToken(USER_TOKEN);
        Call<StatusMessageResponse> call = apiInterface.callTipOfTheDAy(formattedDate, token);
        call.enqueue(new Callback<StatusMessageResponse>() {
            @Override
            public void onResponse(Call<StatusMessageResponse> call, Response<StatusMessageResponse> response) {
                if(response.isSuccessful()){
                    tvTips.setText(response.body().getMessage());
                }else {
                    Log.e(TAG, "onResponse: SOMETHING WRONG" );
                }
            }

            @Override
            public void onFailure(Call<StatusMessageResponse> call, Throwable t) {

            }
        });
    }
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if(getActivity() != null){
            if(menuVisible){
                ((TrackActivity) getActivity()).setActionBarTitle("Learn");

            }
        }
        super.setMenuVisibility(menuVisible);
    }
}
