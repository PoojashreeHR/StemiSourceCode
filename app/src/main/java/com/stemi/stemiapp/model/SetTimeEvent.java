package com.stemi.stemiapp.model;

/**
 * Created by praburaam on 27/09/17.
 */

public class SetTimeEvent {
    private int time;
    private String date;

    public SetTimeEvent(int time, String date){
        this.time = time;
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
