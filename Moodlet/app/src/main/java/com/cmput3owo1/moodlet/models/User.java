package com.cmput3owo1.moodlet.models;

/**
 * Class to represent an app user. Contains just the basic info about the user: username, fullName,
 * and email.
 */
public class User {
    private String username;
    private String fullName;
    private String email;

    /**
     * Public constructor with no arguments
     */
    public User() {
        // Need for Firebase
    }

    /**
     * Constructor that takes the user's username
     * @param username Username of the user
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * Returns the username of the user
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the user
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the full name of the user
     * @return The full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Set the full name of the user
     * @param fullName The full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Returns the email of the user
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email of the user
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
