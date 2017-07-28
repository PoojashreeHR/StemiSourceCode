package com.stemi.stemiapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.TrackActivity;

/**
 * Created by Pooja on 26-07-2017.
 */

public class ExerciseFragment extends Fragment {

    public ExerciseFragment() {
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
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        ((TrackActivity) getActivity()).setActionBarTitle("Execise");

        return view;
    }
}
