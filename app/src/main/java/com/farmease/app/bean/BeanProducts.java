package com.farmease.app.bean;

import com.farmease.app.model.Product;
import com.farmease.app.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BeanProducts {

    @SerializedName("statusCode")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private ArrayList<Product> result;

    public BeanProducts(String message, String status, ArrayList<Product>result) {
        this.message = message;
        this.status = status;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Product> getResult() {
        return result;
    }
}