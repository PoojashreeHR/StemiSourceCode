package com.stemi.stemiapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.MainActivity;
import com.stemi.stemiapp.activity.RegistrationActivity;
import com.stemi.stemiapp.customviews.BetterSpinner;


import java.util.ArrayList;

/**
 * Created by Pooja on 14-07-2017.
 */

public class UserDetailsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    Button user_next;
    BetterSpinner gender_spinner;
    ArrayList<String> genderText;
    String salutatioSelected = "";

    public UserDetailsFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        view.findViewById(R.id.userDetailButton).setOnClickListener(this);
        gender_spinner = (BetterSpinner) view.findViewById(R.id.gender_spinner);
        genderText = new ArrayList<>();
        gender_spinner.setOnItemSelectedListener(this);
        genderText.add("Male");
        genderText.add("Female");
       // genderText.add("Mrs");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.spinner_layout, genderText );
        gender_spinner.setAdapter(arrayAdapter);

       /* ArrayAdapter<String> quantityAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, genderText);
        quantityAdapter.setDropDownViewResource(R.layout.spinner_layout);
        gender_spinner.setAdapter(quantityAdapter);*/
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
            case R.id.userDetailButton:
                //Move to Fragment 03
                ((RegistrationActivity) getActivity()).showFragment(new PhysicalDetailsFragment());
                break;

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        salutatioSelected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        salutatioSelected = parent.getItemAtPosition(0).toString();
    }
}
