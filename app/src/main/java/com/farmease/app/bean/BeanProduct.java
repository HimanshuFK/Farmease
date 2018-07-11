package com.farmease.app.bean;

import com.farmease.app.model.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BeanProduct {

    @SerializedName("statusCode")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private Product result;

    public BeanProduct(String message, String status, Product result) {
        this.message = message;
        this.status = status;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public Product getResult() {
        return result;
    }
}