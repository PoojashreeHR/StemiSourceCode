package com.stemi.stemiapp.model;

/**
 * Created by praburaam on 19/10/17.
 */

public class UserAcceptedEvent {
    private String message;

    public UserAcceptedEvent(String msg){
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }
}
