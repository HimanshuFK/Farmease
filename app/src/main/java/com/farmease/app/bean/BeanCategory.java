package com.farmease.app.bean;

import com.farmease.app.model.Category;
import com.farmease.app.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BeanCategory {

    @SerializedName("statusCode")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private Category[] result;

    public BeanCategory(String message, String status, Category[] result) {
        this.message = message;
        this.status = status;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public Category[] getResult() {
        return result;
    }
}