package com.stemi.stemiapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.RegistrationActivity;
import com.stemi.stemiapp.customviews.AnswerTemplateView;
import com.stemi.stemiapp.customviews.BetterSpinner;
import com.stemi.stemiapp.customviews.MaterialSpinner;
import com.stemi.stemiapp.model.RegisteredUserDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pooja on 14-07-2017.
 */

public class PhysicalDetailsFragment extends Fragment implements View.OnClickListener,AppConstants {
    @BindView(R.id.et_height) EditText etHeight;
    @BindView(R.id.et_weight) EditText etWeight;
    @BindView(R.id.et_waist) EditText etWaist;
    @BindView(R.id.et_address) EditText etAddress;
    @BindView(R.id.smoke_answerLayout) AnswerTemplateView smokeAnswer;
    @BindView(R.id.heart_attack) AnswerTemplateView heartAttack;

    AppSharedPreference appSharedPreference;
    //  BetterSpinner gender_spinner;
    ArrayList<String> genderText;
    String salutatioSelected = "";
  //  BetterSpinner spinner;
    RegisteredUserDetails registeredUserDetails;
    public PhysicalDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_physical_details, container, false);
        ButterKnife.bind(this, view);
        view.findViewById(R.id.bt_physical_next).setOnClickListener(this);

        appSharedPreference = new AppSharedPreference(getActivity());
       // spinner = (BetterSpinner) view.findViewById(R.id.spinner);
        genderText = new ArrayList<>();
        genderText.add("Feet and In");
        genderText.add("CM");
        // genderText.add("Mrs");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, genderText );
        /*spinner.setAdapter(arrayAdapter);
        spinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                salutatioSelected = spinner.getText().toString();
                Log.e("TAG","Text is :"+spinner.getText().toString());
            }
        });*/
        if (RegistrationActivity.registeredUserDetails != null) {
            etHeight.setText(RegistrationActivity.registeredUserDetails.getHeight());
            etWeight.setText(RegistrationActivity.registeredUserDetails.getWeight());
            etWaist.setText(RegistrationActivity.registeredUserDetails.getWaist());
            etAddress.setText(RegistrationActivity.registeredUserDetails.getAddress());
            if (RegistrationActivity.registeredUserDetails.getDo_you_smoke() != null) {
                smokeAnswer.setResponse(RegistrationActivity.registeredUserDetails.getDo_you_smoke());
            }
            if (RegistrationActivity.registeredUserDetails.getHeart_attack() != null) {
                heartAttack.setResponse(RegistrationActivity.registeredUserDetails.getHeart_attack());
            }

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        saveDetails();
    }



   public void saveDetails(){
       RegistrationActivity.registeredUserDetails.setHeight(etHeight.getText().toString());
       RegistrationActivity.registeredUserDetails.setWeight(etWeight.getText().toString());
       RegistrationActivity.registeredUserDetails.setWaist(etWaist.getText().toString());
       RegistrationActivity.registeredUserDetails.setAddress(etAddress.getText().toString());
       RegistrationActivity.registeredUserDetails.setDo_you_smoke(smokeAnswer.getResponse());
       RegistrationActivity.registeredUserDetails.setHeart_attack(heartAttack.getResponse());
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_physical_next:
                //Move to Fragment 03
                if(validateFields()) {
                    saveDetails();
                    appSharedPreference.addUserHeight(USER_HEIGHT,RegistrationActivity.registeredUserDetails.getHeight());
                    ((RegistrationActivity) getActivity()).showFragment(new HealthDetailFragment_1());
                    break;
                }
        }
    }

    private boolean validateFields() {
        boolean valid = true;

       /*    String gender = salutatioSelected;
        if (TextUtils.isEmpty(gender)) {
            spinner.setError("Required");
            valid = false;
        } else {
            spinner.setError(null);
    }*/
        String height = etHeight.getText().toString();
        if (TextUtils.isEmpty(height)) {
            etHeight.setError("Required");
            valid = false;
        }else if (Integer.parseInt(height) < 20 || Integer.parseInt(height) > 200) {
            etHeight.setError("Enter valid Height");
            valid = false;
        } else {
            etHeight.setError(null);
        }

        String weight = etWeight.getText().toString();
        if (TextUtils.isEmpty(weight)) {
            etWeight.setError("Required");
            valid = false;
        } else if (Integer.parseInt(weight) < 20 || Integer.parseInt(weight) > 200) {
            etWeight.setError("Enter valid weight");
            valid = false;
        } else {
            etWeight.setError(null);
        }

        String waist = etWaist.getText().toString();
        if (TextUtils.isEmpty(waist)) {
            etWaist.setError("Required");
            valid = false;
        } else if (Integer.parseInt(waist) < 20 || Integer.parseInt(waist) > 200) {
            etWaist.setError("Enter valid waist");
            valid = false;
        } else {
            etWaist.setError(null);
        }

        String address = etAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Required");
            valid = false;
        } else {
            etAddress.setError(null);
        }
        if(smokeAnswer.getResponse() == null){
            Toast.makeText(getActivity(), "You must select atleast one answer", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if(heartAttack.getResponse() == null){
            Toast.makeText(getActivity(), "You must select atleast one answer", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }
}
