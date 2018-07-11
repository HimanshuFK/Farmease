package com.farmease.app.model;

import java.util.ArrayList;

public class Home {

    ArrayList<Banner> banners;
    ArrayList<Product> latestProducts, trendingProducts;

    public Home(ArrayList<Banner> banners, ArrayList<Product> latestProducts, ArrayList<Product> trendingProducts) {
        this.banners = banners;
        this.latestProducts = latestProducts;
        this.trendingProducts = trendingProducts;
    }

    public ArrayList<Banner> getBanners() {
        return banners;
    }

    public ArrayList<Product> getTrendingProducts() {
        return trendingProducts;
    }

    public ArrayList<Product> getLatestProducts() {
        return latestProducts;
    }
}
