package com.farmease.app.login;

public class User {

    String token,id,role;

    public User(String token, String id, String role) {
        this.token = token;
        this.id = id;
        this.role = role;


    }

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }

    public String getRole() {
        return role;
    }
}
