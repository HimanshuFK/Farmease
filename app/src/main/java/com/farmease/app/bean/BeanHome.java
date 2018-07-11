package com.farmease.app.bean;

import com.farmease.app.model.Home;
import com.farmease.app.model.User;
import com.google.gson.annotations.SerializedName;

public class BeanHome {

    @SerializedName("statusCode")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private Home result;

    public BeanHome(String message, String status, Home result) {
        this.message = message;
        this.status = status;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public Home getResult() {
        return result;
    }
}