package com.stemi.stemiapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stemi.stemiapp.R;

/**
 * Created by Pooja on 17-07-2017.
 */

public class HealthDetailFragment_1 extends Fragment implements View.OnClickListener {
    public HealthDetailFragment_1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_health_detail_1, container, false);
        // view.findViewById(R.id.userDetailButton).setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {

    }
}
