package com.stemi.stemiapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.activity.RegistrationActivity;
import com.stemi.stemiapp.customviews.AnswerTemplateView;
import com.stemi.stemiapp.model.HealthAnswers;
import com.stemi.stemiapp.model.HealthQuestions;
import com.stemi.stemiapp.model.RegisteredUserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pooja on 17-07-2017.
 */

public class HealthDetailFragment_1 extends Fragment implements View.OnClickListener {

    RecyclerView healthRecycler;
    private List<HealthQuestions> healthQuestions;
    private ArrayList<HealthAnswers> healthAnswers = new ArrayList<>();
    private HealthAdapter healthAdapter;
    Button registerButton;
    private RegisteredUserDetails user;
    private boolean editmode;

    public HealthDetailFragment_1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_health_detail_1, container, false);
        healthRecycler = (RecyclerView) view.findViewById(R.id.health_recyclerview);
        healthQuestions = new ArrayList<>();
        registerButton = (Button) view.findViewById(R.id.bt_healthNext);
        registerButton.setOnClickListener(this);
        healthAdapter = new HealthAdapter(healthQuestions, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        healthRecycler.setLayoutManager(mLayoutManager);
        healthRecycler.setAdapter(healthAdapter);
        healthData();
        user = this.getArguments().getParcelable("user");
        if(user != null){
            editmode = true;
        }
        return view;
    }


    private void prepareHealthData(String question) {
        HealthQuestions questions = new HealthQuestions();
        questions.setQuestions(question);
        healthQuestions.add(questions);
        RegistrationActivity.registeredUserDetails.setHealthQuestions(healthQuestions);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_healthNext:
                //Move to Fragment 03
                if (validateAllFields()) {
                    ((RegistrationActivity) getActivity()).showFragment(new ProfilePhotoFragment());
                }
        }
    }

    public boolean validateAllFields(){
        boolean valid = true;
        if(editmode){
            return true;
        }
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
    private void healthData() {
        prepareHealthData(getResources().getString(R.string.have_diabetes));
        prepareHealthData(getResources().getString(R.string.have_blood_presssure));
        prepareHealthData(getResources().getString(R.string.have_cholesterol));
        prepareHealthData(getResources().getString(R.string.had_paralytic_stroke));
        prepareHealthData(getResources().getString(R.string.have_astama));
        prepareHealthData(getResources().getString(R.string.family_health));

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

            if(user != null){
                switch (position){
                    case 0 :
                        holder.answerTemplateView.setResponse(user.getDiabetes());
                        RegistrationActivity.registeredUserDetails.setDiabetes(user.getDiabetes());
                        break;

                    case 1:
                        holder.answerTemplateView.setResponse(user.getBlood_pressure());
                        RegistrationActivity.registeredUserDetails.setBlood_pressure(user.getBlood_pressure());
                        break;

                    case 2:
                        holder.answerTemplateView.setResponse(user.getCholesterol());
                        RegistrationActivity.registeredUserDetails.setCholesterol(user.getCholesterol());
                        break;

                    case 3:
                        holder.answerTemplateView.setResponse(user.getHad_paralytic_stroke());
                        RegistrationActivity.registeredUserDetails.setHad_paralytic_stroke(user.getHad_paralytic_stroke());
                        break;

                    case 4:
                        holder.answerTemplateView.setResponse(user.getHave_asthma());
                        RegistrationActivity.registeredUserDetails.setHave_asthma(user.getHave_asthma());
                        break;

                    case 5:
                        holder.answerTemplateView.setResponse(user.getFamily_had_heart_attack());
                        RegistrationActivity.registeredUserDetails.setFamily_had_heart_attack(user.getFamily_had_heart_attack());
                        break;
                }
            }

            holder.answerTemplateView.setResponseChangedListener(new AnswerTemplateView.ResponseChangedListener() {
                @Override
                public void onResponse(String response) {
                    saveValues(position,response);

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
