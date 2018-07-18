package com.farmease.app.bean;

import com.farmease.app.model.CartProduct;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BeanSearch {

    @SerializedName("statusCode")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private ArrayList<SearchItem> result;

    public BeanSearch(String message, String status, ArrayList<SearchItem> result) {
        this.message = message;
        this.status = status;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<SearchItem> getResult() {
        return result;
    }

    public class SearchItem {

        int id;
        String name,featured_image;
        public String getName() {
            return name;
        }

        public String getImage() {
            return featured_image;
        }

        public int getId() {
            return id;
        }
    }
}