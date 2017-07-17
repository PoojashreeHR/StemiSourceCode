package com.stemi.stemiapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.RegistrationActivity;

/**
 * Created by Pooja on 14-07-2017.
 */

public class PhysicalDetailsFragment extends Fragment implements View.OnClickListener {
    TextView textView;
    public PhysicalDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_physical_details, container, false);
        view.findViewById(R.id.bt_physical_next).setOnClickListener(this);
        textView =(TextView) view.findViewById(R.id.tv_smoke_yes);
        textView.setOnClickListener(this);


        // view.findViewById(R.id.userDetailButton).setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // ((RegistrationActivity) getActivity()).disableNavigationIcon();
        // ((RegistrationActivity) getActivity()).setToolbarTitle(R.string.MainFragmentTitle);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_physical_next:
                //Move to Fragment 03
                ((RegistrationActivity) getActivity()).showFragment(new HealthDetailFragment_1());
                break;
            case R.id.tv_smoke_yes:
                textView.setBackgroundColor(getResources().getColor(R.color.white));
                textView.setTextColor(getResources().getColor(R.color.appBackground));
                Toast.makeText(getActivity(), "Yes", Toast.LENGTH_SHORT).show();

        }
    }
}
