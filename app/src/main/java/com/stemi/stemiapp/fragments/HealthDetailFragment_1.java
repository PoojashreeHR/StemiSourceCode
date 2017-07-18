package com.stemi.stemiapp.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.customviews.AnswerTemplateView;
import com.stemi.stemiapp.model.HealthAnswers;
import com.stemi.stemiapp.model.HealthQuestions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pooja on 17-07-2017.
 */

public class HealthDetailFragment_1 extends Fragment implements View.OnClickListener {

    RecyclerView healthRecycler;
    private List<HealthQuestions> healthQuestions = new ArrayList<>();
    private ArrayList<HealthAnswers> healthAnswers = new ArrayList<>();
    private HealthAdapter healthAdapter;

    public HealthDetailFragment_1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_health_detail_1, container, false);
        healthRecycler = (RecyclerView) view.findViewById(R.id.health_recyclerview);
        healthAdapter = new HealthAdapter(healthQuestions,getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        healthRecycler.setLayoutManager(mLayoutManager);
        healthRecycler.setAdapter(healthAdapter);
        HealthData();
        return view;
    }


    private void prepareHealthData(String Question,int size){
        HealthQuestions questions = new HealthQuestions();
        questions.setQuestions(Question);
        questions.setSize(size);
        healthQuestions.add(questions);
    }

    @Override
    public void onClick(View v) {

    }


    //Setting question and adding it to recyclerview
    private void HealthData(){
        prepareHealthData(getResources().getString(R.string.have_diabetes),3);
        prepareHealthData(getResources().getString(R.string.have_blood_presssure),3);
        prepareHealthData(getResources().getString(R.string.have_cholesterol),3);
        prepareHealthData(getResources().getString(R.string.had_paralytic_stroke),3);
        prepareHealthData(getResources().getString(R.string.have_astama),3);
        prepareHealthData(getResources().getString(R.string.family_health),3);

    }

    // Recycleview Adapter

    public class HealthAdapter extends RecyclerView.Adapter<HealthAdapter.MyViewHolder> {

        private List<HealthQuestions> healthQuestions;
        List<HealthAnswers> healthAnswers;
        Context mContext;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView yes, no,dont_know, question;
            public AnswerTemplateView answerTemplateView;

            public MyViewHolder(View view) {
                super(view);
                question = (TextView) view.findViewById(R.id.have_diabetes);
                answerTemplateView = (AnswerTemplateView) view.findViewById(R.id.answerLayout);
               /* yes = (TextView) view.findViewById(R.id.tv_yes);
                no = (TextView) view.findViewById(R.id.tv_no);
                dont_know = (TextView) view.findViewById(R.id.tv_dont_know);*/
            }
        }

        public HealthAdapter(List<HealthQuestions> healthQuestions,Context context) {
            this.healthQuestions = healthQuestions;
            this.mContext =context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.health_details_single_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            HealthQuestions questions = healthQuestions.get(position);
            holder.question.setText(questions.getQuestions());
//            holder.answerTemplateView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext, "you Clicked : "+ holder.answerTemplateView.getResponse(), Toast.LENGTH_SHORT).show();
//
//                }
//            });
            holder.answerTemplateView.setResponseChangedListener(new AnswerTemplateView.ResponseChangedListener() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(mContext, "You clicked "+response, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return healthQuestions.size();
        }
    }
}
