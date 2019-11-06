package com.cmput3owo1.moodlet.models;

public class User {
    private String username;
    private String fullName;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
