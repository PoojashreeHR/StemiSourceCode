package com.stemi.stemiapp.model;

/**
 * Created by praburaam on 13/10/17.
 */

public class DataSavedEvent {
    private String message;

    public DataSavedEvent(String msg){
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
