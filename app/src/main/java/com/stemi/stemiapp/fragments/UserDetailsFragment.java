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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.MainActivity;
import com.stemi.stemiapp.activity.RegistrationActivity;
import com.stemi.stemiapp.customviews.BetterSpinner;
import com.stemi.stemiapp.model.RegisteredUserDetails;


import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 14-07-2017.
 */

public class UserDetailsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{


    @BindView(R.id.et_name) EditText et_name;
    @BindView(R.id.et_age)  EditText et_age;
    @BindView(R.id.et_phone) EditText et_phone;
    @BindView(R.id.et_address) EditText et_address;


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
        ButterKnife.bind(this,view);
        view.findViewById(R.id.userDetailButton).setOnClickListener(this);

        gender_spinner = (BetterSpinner) view.findViewById(R.id.gender_spinner);
        genderText = new ArrayList<>();
        genderText.add("Male");
        genderText.add("Female");
       // genderText.add("Mrs");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_layout, genderText );
        gender_spinner.setAdapter(arrayAdapter);

        gender_spinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                salutatioSelected = gender_spinner.getText().toString();
                Log.e("TAG","Text is :"+gender_spinner.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        saveUserDetails();
       // ((RegistrationActivity) getActivity()).disableNavigationIcon();
       // ((RegistrationActivity) getActivity()).setToolbarTitle(R.string.MainFragmentTitle);
    }

    public void saveUserDetails(){
        RegistrationActivity.registeredUserDetails.setName(et_name.getText().toString());
        RegistrationActivity.registeredUserDetails.setAge(et_age.getText().toString());
        RegistrationActivity.registeredUserDetails.setGender(gender_spinner.getText().toString());
        RegistrationActivity.registeredUserDetails.setPhone(et_phone.getText().toString());
        RegistrationActivity.registeredUserDetails.setAddress(et_address.getText().toString());
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.userDetailButton:
                //Move to Fragment 03
                if (validateAllFields()) {
                    saveUserDetails();
                    ((RegistrationActivity) getActivity()).showFragment(new PhysicalDetailsFragment());
                    break;
                }

        }

    }
    private boolean validateAllFields() {
        boolean valid = true;

        Log.e(TAG, "validateAllFields: " + salutatioSelected);


        String firstName = et_name.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            et_name.setError("Required");
            valid = false;
        } else {
            et_name.setError(null);
        }

        String age = et_age.getText().toString();
        if (TextUtils.isEmpty(age)) {
            et_age.setError("Required");
            valid = false;
        } else {
            et_age.setError(null);
        }

        String gender = salutatioSelected;
        if (TextUtils.isEmpty(gender)) {
            gender_spinner.setError("Required");
            valid = false;
        } else {
            gender_spinner.setError(null);
    }

        String mobileNumber = et_phone.getText().toString();
        if (TextUtils.isEmpty(mobileNumber)) {
            et_phone.setError("Required");
            valid = false;
        } else if (mobileNumber.length() < 10 || mobileNumber.length() > 10) {
            et_phone.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            et_phone.setError(null);
        }

        String password = et_address.getText().toString();
        if (TextUtils.isEmpty(password)) {
            et_address.setError("Required");
            valid = false;
        } else {
            et_address.setError(null);
        }
        return valid;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        salutatioSelected = parent.getItemAtPosition(0).toString();
    }
}
