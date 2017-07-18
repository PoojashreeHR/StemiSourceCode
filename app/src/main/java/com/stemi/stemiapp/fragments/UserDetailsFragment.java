package com.stemi.stemiapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pooja on 14-07-2017.
 */

public class UserDetailsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{


    @BindView(R.id.et_name) EditText et_name;
    @BindView(R.id.et_age)  EditText et_age;
    @BindView(R.id.et_phone) EditText et_phone;
    @BindView(R.id.et_address) EditText et_address;

    RegisteredUserDetails registeredUserDetails;
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

        registeredUserDetails = new RegisteredUserDetails();
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
                if (validateAllFields()) {
                    registeredUserDetails.setName(et_name.getText().toString());
                    registeredUserDetails.setAge(et_age.getText().toString());
                    registeredUserDetails.setGender(genderText.toString());
                    registeredUserDetails.setPhone(et_phone.getText().toString());
                    registeredUserDetails.setAddress(et_address.getText().toString());
                    ((RegistrationActivity) getActivity()).showFragment(new PhysicalDetailsFragment());
                    break;
                }

        }

    }
    private boolean validateAllFields() {
        boolean valid = true;

       /* String salutation = et_title.getText().toString();
        if (TextUtils.isEmpty(salutation)) {
            et_title.setError("Required");
            valid = false;
        } else if (salutation.length() > 3 || salutation.length() < 2) {
            et_title.setError("Eg: Mr or Mrs");
            valid = false;
        } else {
            et_title.setError(null);
        }*/
    //    CommonUtils.loge(TAG, "validateAllFields: " + salutatioSelected);


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

        String gender = gender_spinner.getText().toString();
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
        salutatioSelected = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        salutatioSelected = parent.getItemAtPosition(0).toString();
    }
}
