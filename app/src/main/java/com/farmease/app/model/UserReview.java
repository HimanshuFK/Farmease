package com.farmease.app.model;

public class UserReview {

    int id,user_id,product_id,rating,totalReviews;
    String first_name,last_name,review,created_at,category_name;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getRating() {
        return rating;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getReview() {
        return review;
    }

    public String getCreated_at() {
        return created_at;
    }
}
