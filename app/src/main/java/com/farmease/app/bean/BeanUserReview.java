package com.farmease.app.bean;

import com.farmease.app.model.Product;
import com.farmease.app.model.UserReview;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BeanUserReview {

    @SerializedName("statusCode")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private ArrayList<UserReview> result;

    public BeanUserReview(String message, String status, ArrayList<UserReview> result) {
        this.message = message;
        this.status = status;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<UserReview> getResult() {
        return result;
    }
}