package com.farmease.app.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BeanBookedSlot {

    @SerializedName("statusCode")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private ArrayList<Slots> result;

    public BeanBookedSlot(String message, String status, ArrayList<Slots> result) {
        this.message = message;
        this.status = status;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Slots> getResult() {
        return result;
    }

    public class Slots{

        String created_at;

        public String getCreated_at() {
            return created_at;
        }
    }
}