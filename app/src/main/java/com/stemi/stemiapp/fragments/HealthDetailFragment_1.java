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

import com.stemi.stemiapp.R;
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
       // HealthData();// view.findViewById(R.id.userDetailButton).setOnClickListener(this);
        return view;
    }


    private HealthAnswers faqProduct(String values,boolean isClicked) {
        HealthAnswers answers = new HealthAnswers();
        answers.setValues(values);
        answers.setClicked(isClicked);
        return answers;
    }


    private void prepareHealthData(String Question,int size){
        HealthQuestions questions = new HealthQuestions();
        questions.setQuestions(Question);
        questions.setSize(size);
        //questions.setHealthAnswers(answers);
        healthQuestions.add(questions);
    }

    @Override
    public void onClick(View v) {

    }

    private void HealthData(){

        ArrayList<HealthAnswers> answers = new ArrayList<>();
        /*answers.add(faqProduct("YES",false));
        answers.add(faqProduct("NO",false));
        answers.add(faqProduct("DON'T KNOW",false));*/
        prepareHealthData("Do you have diabetes?",3);
        prepareHealthData("Do you have high blood pressure?",3);
        prepareHealthData("Do you have high cholesterol?",3);
        prepareHealthData("Did you ever had paralytic stroke?",3);
        prepareHealthData("Do you have bronchial astama?",3);
        prepareHealthData("Did anyone in your immediate family had a heart attack?",3);

    }

    public class HealthAdapter extends RecyclerView.Adapter<HealthAdapter.MyViewHolder> {

        private List<HealthQuestions> healthQuestions;
        List<HealthAnswers> healthAnswers;
        Context mContext;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView yes, no,dont_know, question;

            public MyViewHolder(View view) {
                super(view);
              //  title = (TextView) view.findViewById(R.id.title);
                question = (TextView) view.findViewById(R.id.have_diabetes);
                yes = (TextView) view.findViewById(R.id.tv_yes);
                no = (TextView) view.findViewById(R.id.tv_no);
                dont_know = (TextView) view.findViewById(R.id.tv_dont_know);
                // = (TextView) view.findViewById(R.id.);
            }
        }


        public HealthAdapter(List<HealthQuestions> healthQuestions,Context context) {
            this.healthQuestions = healthQuestions;
            this.mContext =context;
         //   healthAnswers = healthQuestions.get(0).getHealthAnswers();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.health_details_single_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            HealthQuestions questions = healthQuestions.get(position);
            holder.question.setText(questions.getQuestions());
            /*for(int i=0; i<questions.getHealthAnswers().size();i++) {
                    holder.yes.setText(questions.getHealthAnswers().get(0).getValues());
                    holder.no.setText(questions.getHealthAnswers().get(1).getValues());
                    holder.dont_know.setText(questions.getHealthAnswers().get(2).getValues());
            }

            for (int i = 0; i < questions.getHealthAnswers().size(); i++) {
                if(questions.getHealthAnswers().get(i).getClicked()){

                }
            }
            holder.yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        holder.yes.setBackground(getResources().getDrawable(R.drawable.inverted_textcolor));
                    }else {
                        holder.yes.setBackgroundDrawable(getResources().getDrawable(R.drawable.inverted_textcolor));
                    }
                }
            });*/
          //  holder.year.setText(movie.getYear());
        }

        @Override
        public int getItemCount() {
            return healthQuestions.size();
        }
    }
}
