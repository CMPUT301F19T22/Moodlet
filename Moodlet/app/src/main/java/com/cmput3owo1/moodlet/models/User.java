package com.cmput3owo1.moodlet.models;

/**
 * Class to represent an app user. Contains just the basic info about the user: username, fullName,
 * and email.
 */
public class User {
    private String username;
    private String fullname;
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
    public String getFullname() {
        return fullname;
    }

    /**
     * Set the full name of the user
     * @param fullname The full name
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public boolean isRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

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
