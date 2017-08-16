package com.stemi.stemiapp.model;

import java.util.List;

/**
 * Created by Pooja on 18-07-2017.
 */

public class RegisteredUserDetails {
    String uniqueId;
    String name;
    String age;
    String gender;
    String  phone;
    String address;
    String  height;
    String  weight;
    String waist;
    String do_you_smoke;
    String heart_attack;
    List<HealthQuestions> healthQuestions;
    String diabetes;
    String blood_pressure;
    String cholesterol;
    String had_paralytic_stroke;
    String have_asthma;
    String family_had_heart_attack;
    Boolean isClicked;
    String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public List<HealthQuestions> getHealthQuestions() {
        return healthQuestions;
    }

    public void setHealthQuestions(List<HealthQuestions> healthQuestions) {
        this.healthQuestions = healthQuestions;
    }

    public Boolean getClicked() {
        return isClicked;
    }

    public void setClicked(Boolean clicked) {
        isClicked = clicked;
    }
/*
    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    private String questions;*/


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {return weight; }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWaist() {
        return waist;
    }

    public void setWaist(String waist) {
        this.waist = waist;
    }

    public String getDo_you_smoke() {
        return do_you_smoke;
    }

    public void setDo_you_smoke(String do_you_smoke) {
        this.do_you_smoke = do_you_smoke;
    }

    public String getHeart_attack() {
        return heart_attack;
    }

    public void setHeart_attack(String heart_attack) {
        this.heart_attack = heart_attack;
    }

    public String getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(String diabetes) {
        this.diabetes = diabetes;
    }

    public String getBlood_pressure() {
        return blood_pressure;
    }

    public void setBlood_pressure(String blood_pressure) {
        this.blood_pressure = blood_pressure;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getHad_paralytic_stroke() {
        return had_paralytic_stroke;
    }

    public void setHad_paralytic_stroke(String had_paralytic_stroke) {
        this.had_paralytic_stroke = had_paralytic_stroke;
    }

    public String getHave_asthma() {
        return have_asthma;
    }

    public void setHave_asthma(String have_asthma) {
        this.have_asthma = have_asthma;
    }

    public String getFamily_had_heart_attack() {
        return family_had_heart_attack;
    }

    public void setFamily_had_heart_attack(String family_had_heart_attack) {
        this.family_had_heart_attack = family_had_heart_attack;
    }
}
