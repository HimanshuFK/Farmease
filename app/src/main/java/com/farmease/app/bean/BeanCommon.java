package com.farmease.app.bean;

import com.google.gson.annotations.SerializedName;

public class BeanCommon {
    @SerializedName("statusCode")
    private String status;
    @SerializedName("message")
    private String message;

    public BeanCommon(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
