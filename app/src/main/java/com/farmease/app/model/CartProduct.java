package com.farmease.app.model;

import java.util.ArrayList;

/**
 * Created by HIMANSHU on 5/6/18.
 */
public class CartProduct {
    String  name, featured_image,created_at;

    int id,product_id,user_id,status,is_operator,is_tractor;
    float amount,per_hour_rate,total_hours;

    public String getName() {
        return name;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getId() {
        return id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getStatus() {
        return status;
    }

    public int getIs_operator() {
        return is_operator;
    }

    public int getIs_tractor() {
        return is_tractor;
    }

    public float getTotal_hours() {
        return total_hours;
    }

    public float getAmount() {
        return amount;
    }

    public float getPer_hour_rate() {
        return per_hour_rate;
    }
}



