package com.stemi.stemiapp.model.apiModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja on 17-08-2017.
 */

public class SignUpResponseModel {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("token")
    private String token;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
