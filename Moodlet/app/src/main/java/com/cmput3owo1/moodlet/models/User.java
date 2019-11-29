package com.cmput3owo1.moodlet.models;

import com.google.firebase.firestore.Exclude;

/**
 * Class to represent an app user. Contains just the basic info about the user: username, fullName,
 * and email.
 */
public class User {
    private String username;
    private String fullName;
    private String email;
    private boolean requested = false;
    private boolean following = false;

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
     * Constructor that takes the user's username, the user's full name, and the user's email
     * @param username Username of the user
     * @param fullName Full name of the user
     * @param email Email of the user
     */
    public User(String username, String fullName, String email) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
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

    /**
     * Returns the request status of the user - true if the current user has requested to follow
     * this user
     * @return The request status of the user
     */
    public boolean isRequested() {
        return requested;
    }

    /**
     * Set the request status of the user - true if the current user has requested to follow this
     * user
     * @param requested The new request status
     */
    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    /**
     * Returns the follow status of the user - true if the current user is following this user
     * @return The follow status of the user
     */
    public boolean isFollowing() {
        return following;
    }

    /**
     * Set the follow status of the user - true if the current user is following this user
     * @param following The new follow status
     */
    public void setFollowing(boolean following) {
        this.following = following;
    }

    /**
     * Indicates whether some other user is "equal to" this user. Users with the same username are
     * the same, since the username is unique
     * @param obj The reference object with which to compare
     * @return Returns true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if(obj == null || obj.getClass()!= this.getClass()) {
            return false;
        }

        User user = (User) obj;
        return this.username.equals(user.getUsername());
    }
}
