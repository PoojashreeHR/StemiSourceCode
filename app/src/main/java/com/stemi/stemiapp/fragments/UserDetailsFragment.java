package com.stemi.stemiapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.RegistrationActivity;
import com.stemi.stemiapp.databases.UserDetailsTable;


import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 14-07-2017.
 */

public class UserDetailsFragment extends Fragment implements View.OnClickListener{


    @BindView(R.id.et_name) EditText etName;
    @BindView(R.id.et_age)  EditText etAge;
    @BindView(R.id.et_phone) EditText etPhone;
    @BindView(R.id.et_email) EditText etEmail;

    String emailPattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    String phonePatteren = "^[7-9][0-9]{9}$";

    //  EditText etEmail;
    UserDetailsTable dBforUserDetails;
    String gender;
    RadioGroup genderRadiogroup;
    String salutatioSelected = "";

    public UserDetailsFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        ButterKnife.bind(this,view);

        view.findViewById(R.id.userDetailButton).setOnClickListener(this);
        dBforUserDetails = new UserDetailsTable(getActivity());
        genderRadiogroup = (RadioGroup) view.findViewById(R.id.radioGroup1);
        genderRadiogroup.clearCheck();

       /* Attach CheckedChangeListener to radio group */
        genderRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                gender = ((RadioButton) group.findViewById(checkedId)).getText().toString();
                if(null!=gender && checkedId > -1){
                    salutatioSelected = gender;
                    Log.e(TAG, "onCheckedChanged: User is :"+ gender );
                }

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        saveUserDetails();
    }

    public void saveUserDetails(){
        if(RegistrationActivity.registeredUserDetails != null) {
            String gender1 = gender;
            RegistrationActivity.registeredUserDetails.setName(etName.getText().toString());
            RegistrationActivity.registeredUserDetails.setAge(etAge.getText().toString());
            RegistrationActivity.registeredUserDetails.setGender(gender1);
            RegistrationActivity.registeredUserDetails.setPhone(etPhone.getText().toString());
            RegistrationActivity.registeredUserDetails.setEmail(etEmail.getText().toString());
        }
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


        String firstName = etName.getText().toString();
        String userName = dBforUserDetails.isExist(firstName);
        if (TextUtils.isEmpty(firstName)) {
            etName.setError("Required");
            valid = false;
        } else if(userName != null)  {
                if (userName.equalsIgnoreCase(firstName)) {
                    Log.e(TAG, "validateAllFields: " + "True ");
                    etName.setError("Name already registered");
                    valid = false;
                } else {
                    Log.e(TAG, "validateAllFields: " + "False ");
                }
            }else {
            etName.setError(null);
        }

        String age = etAge.getText().toString();
        if (TextUtils.isEmpty(age)) {
            etAge.setError("Required");
            valid = false;
        } else if (Integer.parseInt(age) < 18 || Integer.parseInt(age) > 100) {
            etAge.setError("Enter Valid Age");
            valid = false;
        }  else {
            etAge.setError(null);
        }

        if(salutatioSelected.equals("")){
            Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        String mobileNumber = etPhone.getText().toString();
        if (TextUtils.isEmpty(mobileNumber)) {
            etPhone.setError("Required");
            valid = false;
        } else if (mobileNumber.length() < 10 || mobileNumber.length() > 10) {
            etPhone.setError("Enter Valid Mobile Number");
            valid = false;
        }else if(!etPhone.getText().toString().matches(phonePatteren)){
            etPhone.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            etPhone.setError(null);
        }

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required");
            valid = false;
        }if (etEmail.getText().toString().matches(emailPattern)) {
            etEmail.setError(null);
        } else{
            etEmail.setError("Enter Valid Email");
                valid = false;
            }
        return valid;
    }
}
