package com.farmease.app.login;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("statusCode")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private User result;

    public Result(String message, String status, User result) {
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