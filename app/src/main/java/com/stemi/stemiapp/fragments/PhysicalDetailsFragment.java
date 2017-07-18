package com.stemi.stemiapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.RegistrationActivity;
import com.stemi.stemiapp.customviews.AnswerTemplateView;
import com.stemi.stemiapp.model.RegisteredUserDetails;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pooja on 14-07-2017.
 */

public class PhysicalDetailsFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.et_height) EditText et_height;
    @BindView(R.id.et_weight) EditText et_weight;
    @BindView(R.id.et_waist) EditText et_waist;
    @BindView(R.id.smoke_answerLayout) AnswerTemplateView smoke_answer;
    @BindView(R.id.heart_attack) AnswerTemplateView heart_attack;

    RegisteredUserDetails registeredUserDetails;
    public PhysicalDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_physical_details, container, false);
        ButterKnife.bind(this,view);
        view.findViewById(R.id.bt_physical_next).setOnClickListener(this);
        // view.findViewById(R.id.userDetailButton).setOnClickListener(this);

        if(RegistrationActivity.registeredUserDetails != null){
            et_height.setText(RegistrationActivity.registeredUserDetails.getWeight());
            et_weight.setText(RegistrationActivity.registeredUserDetails.getWeight());
            et_waist.setText(RegistrationActivity.registeredUserDetails.getWaist());

            if(RegistrationActivity.registeredUserDetails.getDo_you_smoke() != null) {
                smoke_answer.setResponse(RegistrationActivity.registeredUserDetails.getDo_you_smoke());
            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        saveDetails();
        // ((RegistrationActivity) getActivity()).disableNavigationIcon();
        // ((RegistrationActivity) getActivity()).setToolbarTitle(R.string.MainFragmentTitle);
    }



   public void saveDetails(){
       RegistrationActivity.registeredUserDetails.setHeight(et_height.getText().toString());
       RegistrationActivity.registeredUserDetails.setWeight(et_weight.getText().toString());
       RegistrationActivity.registeredUserDetails.setWaist(et_waist.getText().toString());
       RegistrationActivity.registeredUserDetails.setDo_you_smoke(smoke_answer.getResponse());
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_physical_next:
                //Move to Fragment 03
                if(validateFields()) {
                    saveDetails();
                    ((RegistrationActivity) getActivity()).showFragment(new HealthDetailFragment_1());
                    break;
                }
        }
    }

    private boolean validateFields() {
        boolean valid = true;

        String height = et_height.getText().toString();
        if (TextUtils.isEmpty(height)) {
            et_height.setError("Required");
            valid = false;
        } else {
            et_height.setError(null);
        }

        String weight = et_weight.getText().toString();
        if (TextUtils.isEmpty(weight)) {
            et_weight.setError("Required");
            valid = false;
        } else {
            et_weight.setError(null);
        }

        String waist = et_waist.getText().toString();
        if (TextUtils.isEmpty(waist)) {
            et_waist.setError("Required");
            valid = false;
        } else {
            et_waist.setError(null);
        }

        if(smoke_answer.getResponse() == null){
            Toast.makeText(getActivity(), "You must select atleast one answer", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            Toast.makeText(getActivity(), "Your Response is: " + smoke_answer.getResponse().toString(), Toast.LENGTH_SHORT).show();
        }
        return valid;
    }
}
