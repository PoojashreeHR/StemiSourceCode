package com.stemi.stemiapp.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 14-07-2017.
 */

public class PhysicalDetailsFragment extends Fragment implements View.OnClickListener,AppConstants {
    @BindView(R.id.et_inFt) EditText etHeightFt;
    @BindView(R.id.et_inch)EditText etHeightInch;
    @BindView(R.id.et_inCm)EditText et_HeightCm;

    @BindView(R.id.et_waistInches) EditText etWaistInches;
    @BindView(R.id.et_waistCm)EditText etWaistCm;

    @BindView(R.id.et_weight) EditText etWeight;
    @BindView(R.id.et_address) EditText etAddress;
    @BindView(R.id.smoke_answerLayout) AnswerTemplateView smokeAnswer;
    @BindView(R.id.heart_attack) AnswerTemplateView heartAttack;

    AppSharedPreference appSharedPreference;
    //  BetterSpinner gender_spinner;
    ArrayList<String> genderText;
    String HeightInFeet = null;
    String HeightIncm = null;
    String Waist;
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

        etHeightFt.addTextChangedListener(new GenericTextWatcher(etHeightFt));
        etHeightInch.addTextChangedListener(new GenericTextWatcher(etHeightFt));
        et_HeightCm.addTextChangedListener(new GenericTextWatcher(et_HeightCm));

        etWaistCm.addTextChangedListener(new GenericTextWatcher(etWaistCm));
        etWaistInches.addTextChangedListener(new GenericTextWatcher(etWaistInches));

        if (RegistrationActivity.registeredUserDetails != null) {
            //etHeight.setText(RegistrationActivity.registeredUserDetails.getHeight());
            etWeight.setText(RegistrationActivity.registeredUserDetails.getWeight());
           // etWaist.setText(RegistrationActivity.registeredUserDetails.getWaist());
            etAddress.setText(RegistrationActivity.registeredUserDetails.getAddress());
            if (RegistrationActivity.registeredUserDetails.getDo_you_smoke() != null) {
                smokeAnswer.setResponse(RegistrationActivity.registeredUserDetails.getDo_you_smoke());
            }
            if (RegistrationActivity.registeredUserDetails.getHeart_attack() != null) {
                heartAttack.setResponse(RegistrationActivity.registeredUserDetails.getHeart_attack());
            }

        }
        smokeAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager =
                        (InputMethodManager) getActivity().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        smokeAnswer.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //saveDetails();
    }



   public void saveDetails(){

        if(et_HeightCm.getText().toString().isEmpty()){
            String[] parts = HeightInFeet.split("-");
            double part1 = Double.parseDouble(parts[0]); // 004
            double part2 = Double.parseDouble(parts[1]); // 034556

            part2 +=( part1 )* 12;
            double s = Math.round(part2 * 2.54);
            Log.e(TAG, "saveDetails: "+ s );
            HeightIncm = String.valueOf(s);
        }else {
            HeightIncm = et_HeightCm.getText().toString();
        }

        if(etWaistInches.getText().toString().isEmpty()){
            Waist= ""+etWaistCm.getText().toString()+" Cm";
        }else {
            Waist = ""+etWaistInches.getText().toString() + " Inch";
        }

       RegistrationActivity.registeredUserDetails.setHeight(HeightIncm);
       RegistrationActivity.registeredUserDetails.setWeight(etWeight.getText().toString());
       RegistrationActivity.registeredUserDetails.setWaist(Waist);
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
        String address = etAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Required");
            valid = false;
        } else {
            etAddress.setError(null);
        }

        String weight = etWeight.getText().toString();
        if (TextUtils.isEmpty(weight)) {
            etWeight.setError("Required");
            valid = false;
        } else if (Integer.parseInt(weight) < 20 || Integer.parseInt(weight) > 200) {
            etWeight.setError("Enter valid data");
            valid = false;
        } else {
            etWeight.setError(null);
        }


        String heightFt = etHeightFt.getText().toString();
        String heightIn = etHeightInch.getText().toString();
        String heightCm = et_HeightCm.getText().toString();

        if(etHeightFt.isEnabled() && et_HeightCm.isEnabled()){
            Toast.makeText(getActivity(), "Please Enter Height", Toast.LENGTH_SHORT).show();
            valid = false;
        }else if(!etHeightFt.isEnabled() &&  TextUtils.isEmpty(heightCm)){
            et_HeightCm.setError("Required");
            valid = false;
        }else if(etHeightFt.isEnabled()) {
            if (TextUtils.isEmpty(heightFt) && (!TextUtils.isEmpty(heightIn) || TextUtils.isEmpty(heightIn))) {
                etHeightFt.setError("Required");
                valid = false;
            } else if (!TextUtils.isEmpty(heightFt) && TextUtils.isEmpty(heightIn)) {
                HeightInFeet = heightFt + "-00";
                etHeightFt.setError(null);
            } else if (!TextUtils.isEmpty(heightFt) && !TextUtils.isEmpty(heightIn)) {
                HeightInFeet = heightFt + "-" + heightIn;
                etHeightFt.setError(null);
            }
        }else {
            et_HeightCm.setError(null);
            etHeightFt.setError(null);
        }

        String waistIn = etWaistInches.getText().toString();
        String waistCm = etWaistCm.getText().toString();

        if(etWaistInches.isEnabled() && etWaistCm.isEnabled()){
            Toast.makeText(getActivity(), "Please enter Waist", Toast.LENGTH_SHORT).show();
            valid = false;
        }else if(etWaistInches.isEnabled() && TextUtils.isEmpty(waistIn)){
            etWaistInches.setError("Reqired");
            valid = false;
        }else if(et_HeightCm.isEnabled() && TextUtils.isEmpty(waistCm)){
            etWaistCm.setError("Reqired");
            valid = false;
        }else {
            etWaistCm.setError(null);
            etWaistInches.setError(null);
        }
      /*  String height = etHeight.getText().toString();
        if (TextUtils.isEmpty(height)) {
            etHeight.setError("Required");
            valid = false;
        }else if (Integer.parseInt(height) < 20 || Integer.parseInt(height) > 200) {
            etHeight.setError("Enter valid Height");
            valid = false;
        } else {
            etHeight.setError(null);
        }*/


     /*   String waist = etWaist.getText().toString();
        if (TextUtils.isEmpty(waist)) {
            etWaist.setError("Required");
            valid = false;
        } else if (Integer.parseInt(waist) < 20 || Integer.parseInt(waist) > 200) {
            etWaist.setError("Enter valid waist");
            valid = false;
        } else {
            etWaist.setError(null);
        }*/

        if(smokeAnswer.getResponse() == null){
            Toast.makeText(getActivity(), "You must select atleast one answer", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if(heartAttack.getResponse() == null){
            Toast.makeText(getActivity(), "You must select atleast one answer", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }



    private class GenericTextWatcher implements TextWatcher {

        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.et_inFt:
                    if(editable.length() > 0 || etHeightInch.length() > 0){
                        et_HeightCm.setEnabled(false);
                        et_HeightCm.setAlpha(0.6f);
                    }else if(etHeightFt.getText().toString().isEmpty() && etHeightInch.getText().toString().isEmpty()) {
                        et_HeightCm.setEnabled(true);
                        et_HeightCm.setAlpha(1f);
                    }
                    break;

                case R.id.et_inCm:
                    if(editable.length() > 0){
                        etHeightFt.setEnabled(false);
                        etHeightFt.setAlpha(0.6f);

                        etHeightInch.setEnabled(false);
                        etHeightInch.setAlpha(0.6f);
                    }else {
                        etHeightFt.setEnabled(true);
                        etHeightFt.setAlpha(1f);

                        etHeightInch.setEnabled(true);
                        etHeightInch.setAlpha(1f);
                    }
                    break;
                case R.id.et_waistCm:
                    if(editable.length()>0){
                        etWaistInches.setEnabled(false);
                        etWaistInches.setAlpha(0.5f);
                    }else {
                        etWaistInches.setEnabled(true);
                        etWaistInches.setAlpha(1f);
                    }
                    break;
                case R.id.et_waistInches:
                    if(editable.length()>0){
                        etWaistCm.setEnabled(false);
                        etWaistCm.setAlpha(0.5f);
                    }else {
                        etWaistCm.setEnabled(true);
                        etWaistCm.setAlpha(1f);
                    }
            }
        }
    }
}
