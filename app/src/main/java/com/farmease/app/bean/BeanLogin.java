package com.farmease.app.bean;

import com.farmease.app.model.User;
import com.google.gson.annotations.SerializedName;

public class BeanLogin {

    @SerializedName("statusCode")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private User result;

    public BeanLogin(String message, String status, User result) {
        this.message = message;
        this.status = status;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public User getResult() {
        return result;
    }
}