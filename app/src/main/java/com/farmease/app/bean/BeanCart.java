package com.farmease.app.bean;

import com.farmease.app.model.CartProduct;
import com.farmease.app.model.Product;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BeanCart {

    @SerializedName("statusCode")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private ArrayList<CartProduct> result;

    public BeanCart(String message, String status, ArrayList<CartProduct> result) {
        this.message = message;
        this.status = status;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<CartProduct> getResult() {
        return result;
    }
}