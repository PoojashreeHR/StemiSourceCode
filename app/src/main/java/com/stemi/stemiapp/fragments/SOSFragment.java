package com.stemi.stemiapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;


/**
 * Created by Pooja on 24-07-2017.
 */

public class SOSFragment extends Fragment {
    public SOSFragment() {
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
        View view = inflater.inflate(R.layout.fragment_sos, container, false);
      //  ((TrackActivity) getActivity()).setActionBarTitle("SOS");

        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if(getActivity() != null){
            if(menuVisible){
                ((TrackActivity) getActivity()).setActionBarTitle("SOS");

            }
        }
        super.setMenuVisibility(menuVisible);
    }
}
