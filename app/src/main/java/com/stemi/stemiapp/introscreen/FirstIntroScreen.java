package com.stemi.stemiapp.introscreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;

/**
 * Created by Pooja on 01-09-2017.
 */

public class FirstIntroScreen extends Fragment {

    public FirstIntroScreen() {
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
        View view = inflater.inflate(R.layout.fragment_first_intro, container, false);
        ((TrackActivity) getActivity()).setActionBarTitle("Food");

        return view;
    }

}
