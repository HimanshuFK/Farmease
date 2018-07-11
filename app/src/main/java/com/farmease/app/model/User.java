package com.farmease.app.model;

public class User {

    String token,id,role;

    String firstname,lastname,username,email,mobile,entrytime;
    public User(String token, String id, String role) {
        this.token = token;
        this.id = id;
        this.role = role;


    }

    public User(String firstname, String lastname, String username, String email, String mobile, String entrytime) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.entrytime = entrytime;
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
