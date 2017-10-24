package com.stemi.stemiapp.model;

import android.text.Spannable;

/**
 * Created by Pooja on 24-10-2017.
 */

public class Privacy {

    String Question;
    private Spannable answer;


    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public Spannable getAnswer() {
        return answer;
    }

    public void setAnswer(Spannable answer) {
        this.answer = answer;
    }

    public Privacy(String question, Spannable answer) {
        this.Question = question;
        this.answer = answer;
    }
}
