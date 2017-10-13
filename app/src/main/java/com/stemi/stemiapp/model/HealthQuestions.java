package com.stemi.stemiapp.model;

import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pooja on 17-07-2017.
 */

public class HealthQuestions extends RegisteredUserDetails{

    private String questions;
    private int size;
    private String answer;
    private boolean isClicked;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    private List<HealthAnswers> healthAnswers = new ArrayList<>();


    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<HealthAnswers> getHealthAnswers() {
        return healthAnswers;
    }

    public void setHealthAnswers(ArrayList<HealthAnswers> healthAnswers) {
        this.healthAnswers = healthAnswers;
    }

    public HealthQuestions() {

    }

    public HealthQuestions(String questions, int size, List<HealthAnswers> healthAnswers) {
        this.questions = questions;
        this.size = size;
        this.healthAnswers = healthAnswers;
    }
}
