package com.farmease.app.model;

public class SubCategory {

    String subcategory_name,subcategory_image;
    int id;

    public SubCategory(String subcategory_name, String subcategory_image, int id) {
        this.subcategory_name = subcategory_name;
        this.subcategory_image = subcategory_image;
        this.id = id;
    }

    public String getsubcategory_name() {
        return subcategory_name;
    }

    public String getsubcategory_image() {
        return subcategory_image;
    }

    public int getId() {
        return id;
    }

}
