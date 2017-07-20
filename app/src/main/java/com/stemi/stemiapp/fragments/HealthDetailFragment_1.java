package com.stemi.stemiapp.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.RegistrationActivity;
import com.stemi.stemiapp.customviews.AnswerTemplateView;
import com.stemi.stemiapp.databases.DBforUserDetails;
import com.stemi.stemiapp.model.HealthAnswers;
import com.stemi.stemiapp.model.HealthQuestions;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 17-07-2017.
 */

public class HealthDetailFragment_1 extends Fragment implements View.OnClickListener {

    RecyclerView healthRecycler;
    private List<HealthQuestions> healthQuestions = new ArrayList<>();
    private ArrayList<HealthAnswers> healthAnswers = new ArrayList<>();
    private HealthAdapter healthAdapter;
    Button registerButton;
    DBforUserDetails dBforUserDetails;
    public HealthDetailFragment_1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_health_detail_1, container, false);
        healthRecycler = (RecyclerView) view.findViewById(R.id.health_recyclerview);
        registerButton = (Button) view.findViewById(R.id.bt_register);
        registerButton.setOnClickListener(this);
        dBforUserDetails = new DBforUserDetails(getActivity());
        healthAdapter = new HealthAdapter(healthQuestions, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        healthRecycler.setLayoutManager(mLayoutManager);
        healthRecycler.setAdapter(healthAdapter);
        HealthData();
        return view;
    }


    private void prepareHealthData(String Question, int size) {
        HealthQuestions questions = new HealthQuestions();
        questions.setQuestions(Question);
        //  questions.setSize(size);
        healthQuestions.add(questions);
        RegistrationActivity.registeredUserDetails.setHealthQuestions(healthQuestions);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register:
                //Move to Fragment 03
                if (validateAllFields()) {
                   // if(dBforUserDetails.KET)
                    String deviceId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
                    Log.e(TAG, "onClick: "+ deviceId);
                    RegistrationActivity.registeredUserDetails.setUniqueId(deviceId);
                    dBforUserDetails.addEntry(RegistrationActivity.registeredUserDetails);
                    Toast.makeText(getActivity(), "One row added successfully", Toast.LENGTH_SHORT).show();
                }

        }
    }

    public boolean validateAllFields(){
        boolean valid = true;

        if(RegistrationActivity.registeredUserDetails.getDiabetes() == null) {
            Toast.makeText(getActivity(), "Please answer all the questions", Toast.LENGTH_SHORT).show();
            valid = false;
        }else if(RegistrationActivity.registeredUserDetails.getBlood_pressure() == null) {
            Toast.makeText(getActivity(), "Please answer all the questions", Toast.LENGTH_SHORT).show();
            valid = false;
        }else if(RegistrationActivity.registeredUserDetails.getCholesterol()  == null) {
            Toast.makeText(getActivity(), "Please answer all the questions", Toast.LENGTH_SHORT).show();
            valid = false;
        }else if(RegistrationActivity.registeredUserDetails.getHad_paralytic_stroke() == null) {
            Toast.makeText(getActivity(), "Please answer all the questions", Toast.LENGTH_SHORT).show();
            valid = false;
        }else if(RegistrationActivity.registeredUserDetails.getHave_asthma() == null) {
            Toast.makeText(getActivity(), "Please answer all the questions", Toast.LENGTH_SHORT).show();
            valid = false;
        }else if(RegistrationActivity.registeredUserDetails.getFamily_had_heart_attack() == null) {
            Toast.makeText(getActivity(), "Please answer all the questions", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    //Setting question and adding it to recyclerview
    private void HealthData() {
        prepareHealthData(getResources().getString(R.string.have_diabetes), 3);
        prepareHealthData(getResources().getString(R.string.have_blood_presssure), 3);
        prepareHealthData(getResources().getString(R.string.have_cholesterol), 3);
        prepareHealthData(getResources().getString(R.string.had_paralytic_stroke), 3);
        prepareHealthData(getResources().getString(R.string.have_astama), 3);
        prepareHealthData(getResources().getString(R.string.family_health), 3);

    }

    // Recycleview Adapter

    public class HealthAdapter extends RecyclerView.Adapter<HealthAdapter.MyViewHolder> {

        private List<HealthQuestions> healthQuestions;
        // RegisteredUserDetails questions = RegistrationActivity.registeredUserDetails;
        //  HealthAnswers healthAnswers = new HealthAnswers();
        Context mContext;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView question;
            public AnswerTemplateView answerTemplateView;

            public MyViewHolder(View view) {
                super(view);
                question = (TextView) view.findViewById(R.id.have_diabetes);
                answerTemplateView = (AnswerTemplateView) view.findViewById(R.id.answerLayout);
            }
        }

        public HealthAdapter(List<HealthQuestions> healthQuestions, Context context) {
            this.healthQuestions = healthQuestions;
            this.mContext = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.health_details_single_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.question.setText(RegistrationActivity.registeredUserDetails.getHealthQuestions().get(position).getQuestions());
            if (RegistrationActivity.registeredUserDetails.getDiabetes() != null && position == 0) {
                    holder.answerTemplateView.setResponse(RegistrationActivity.registeredUserDetails.getDiabetes());
            } if (RegistrationActivity.registeredUserDetails.getBlood_pressure() != null && position == 1) {
                holder.answerTemplateView.setResponse(RegistrationActivity.registeredUserDetails.getBlood_pressure());
            } if (RegistrationActivity.registeredUserDetails.getCholesterol() != null && position == 2) {
                holder.answerTemplateView.setResponse(RegistrationActivity.registeredUserDetails.getCholesterol());
            } if (RegistrationActivity.registeredUserDetails.getHad_paralytic_stroke() != null && position == 3) {
                holder.answerTemplateView.setResponse(RegistrationActivity.registeredUserDetails.getHad_paralytic_stroke());
            } if (RegistrationActivity.registeredUserDetails.getHave_asthma() != null && position == 4) {
                holder.answerTemplateView.setResponse(RegistrationActivity.registeredUserDetails.getHave_asthma());
            } if (RegistrationActivity.registeredUserDetails.getFamily_had_heart_attack() != null && position == 5) {
                holder.answerTemplateView.setResponse(RegistrationActivity.registeredUserDetails.getFamily_had_heart_attack());
            }


            holder.answerTemplateView.setResponseChangedListener(new AnswerTemplateView.ResponseChangedListener() {
                @Override
                public void onResponse(String response) {
                   // Toast.makeText(mContext, "You clicked " + response + "in position " + position, Toast.LENGTH_SHORT).show();
                    saveValues(position,response);
                    /*for (int i = 0; i < RegistrationActivity.registeredUserDetails.getHealthQuestions().size(); i++) {
                        if (RegistrationActivity.registeredUserDetails.getHealthQuestions().get(i).getQuestions()
                                .equals(RegistrationActivity.registeredUserDetails.getHealthQuestions().get(holder.getAdapterPosition()).getQuestions())) {
                            //Toast.makeText(mContext, "Equals"  , Toast.LENGTH_SHORT).show();
                            Log.e("HealthDetailFragment", " EQUAL");
                            RegistrationActivity.registeredUserDetails.getHealthQuestions().get(i).setClicked(true);

                        } else {
                            //Toast.makeText(mContext, "Not EQUAL", Toast.LENGTH_SHORT).show();
                            Log.e("HealthDetailFragment", " NOT EQUAL");
                        }
                    }
                    //healthQuestions.get(position).setAnswer(response);
                    //healthQuestions.get(position).setClicked(true);
                    RegistrationActivity.registeredUserDetails.setHealthQuestions(healthQuestions);
                    RegistrationActivity.registeredUserDetails.setClicked(true);
*/
                }
            });
        }

        @Override
        public int getItemCount() {
            return healthQuestions.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public  void saveValues(int position, String response){
        if(position == 0){
            RegistrationActivity.registeredUserDetails.setDiabetes(response);
        }else if(position == 1){
            RegistrationActivity.registeredUserDetails.setBlood_pressure(response);
        }else if(position == 2){
            RegistrationActivity.registeredUserDetails.setCholesterol(response);
        }else if(position == 3){
            RegistrationActivity.registeredUserDetails.setHad_paralytic_stroke(response);
        }else if(position == 4){
            RegistrationActivity.registeredUserDetails.setHave_asthma(response);
        }else if(position == 5){
            RegistrationActivity.registeredUserDetails.setFamily_had_heart_attack(response);
        }
    }
}
