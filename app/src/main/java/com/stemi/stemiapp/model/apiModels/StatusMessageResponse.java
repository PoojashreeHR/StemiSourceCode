package com.stemi.stemiapp.model.apiModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja on 18-08-2017.
 */

public class StatusMessageResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;

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
}
