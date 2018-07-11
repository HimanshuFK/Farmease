package com.farmease.app.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HIMANSHU on 5/6/18.
 */
public class Product {
    String  category_id, subcategory_id, brand_id, name, description,featured_image,image_url, specifications;

    int id;
    float ratings,base_price,per_hour_rate,discount,discounted_price;
    UserReview reviews;
    ArrayList<Product> similarProducts;

    public Product(int id, String category_id, String subcategory_id, String brand_id, String name, String description,
                   String featured_image, String image_url, String specifications, float ratings, float base_price, float per_hour_rate,
                   float discount, float discounted_price) {
        this.id = id;
        this.category_id = category_id;
        this.subcategory_id = subcategory_id;
        this.brand_id = brand_id;
        this.name = name;
        this.description = description;
        this.featured_image = featured_image;
        this.image_url = image_url;
        this.specifications = specifications;
        this.ratings = ratings;
        this.base_price = base_price;
        this.per_hour_rate = per_hour_rate;
        this.discount = discount;
        this.discounted_price = discounted_price;
    }

    public float getDiscounted_price() {
        return discounted_price;
    }

    public UserReview getReviews() {
        return reviews;
    }

    public ArrayList<Product> getSimilarProducts() {
        return similarProducts;
    }

    public String getImage_url() {
        return image_url;
    }

    public int getId() {
        return id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getSubcategory_id() {
        return subcategory_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSpecifications() {
        return specifications;
    }

    public float getRatings() {
        return ratings;
    }

    public float getPrice() {
        return base_price;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public float getDiscount() {
        return discount;
    }

    public float getPer_hour_rate() {
        return per_hour_rate;
    }

    public float getBase_price() {
        return base_price;
    }

    public ArrayList<Banner> getImage(){
        String value=getImage_url();
        String[] arrayList = value.split(",");
        ArrayList<Banner> list = new ArrayList<Banner>();
        for (int i = 0; i < arrayList.length; i++) {
            Banner banner=new Banner();
            banner.setBanner_image(arrayList[i]);
            list.add(banner);
        }
        return list;
    }

}



