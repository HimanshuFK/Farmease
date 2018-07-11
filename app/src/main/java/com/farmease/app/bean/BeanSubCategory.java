package com.farmease.app.bean;

import com.farmease.app.model.SubCategory;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BeanSubCategory {

    @SerializedName("statusCode")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private SubCategory[] result;

    public BeanSubCategory(String message, String status, SubCategory[] result) {
        this.message = message;
        this.status = status;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public SubCategory[] getResult() {
        return result;
    }
}