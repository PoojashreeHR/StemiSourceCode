package com.stemi.stemiapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.samples.ChartSampleActivity;

/**
 * Created by Pooja on 26-07-2017.
 */

public class StatusFragment extends Fragment {
    Button button;
    public StatusFragment() {
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
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        button = (Button) view.findViewById(R.id.bt_Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChartSampleActivity.class));
            }
        });
       // ((TrackActivity) getActivity()).setActionBarTitle("Status");

        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if(getActivity() != null){
            if(menuVisible){
                ((TrackActivity) getActivity()).setActionBarTitle("Status");

            }
        }
        super.setMenuVisibility(menuVisible);
    }
}
