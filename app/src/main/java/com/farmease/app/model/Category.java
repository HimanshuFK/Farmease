package com.farmease.app.model;

public class Category {

    int id;
    String category_name,category_image;

    public Category(String category_name, String category_image, int id) {
        this.category_name = category_name;
        this.category_image = category_image;
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public int getId() {
        return id;
    }

}
