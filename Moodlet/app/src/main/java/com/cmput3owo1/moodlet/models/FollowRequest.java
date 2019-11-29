package com.cmput3owo1.moodlet.models;

/**
 * A class that represents a follow request from one {@link User} to another
 */
public class FollowRequest {
    private String requestFrom;
    private String requestTo;
    private String id;

    /**
     * Default constructor for the FollowRequest
     */
    public FollowRequest() {
        // For Firebase
    }

    /**
     * Constructor for the FollowRequest that takes the users involved in the request
     * @param requestFrom The user the request is from
     * @param requestTo The user the request is sent to
     */
    public FollowRequest(String requestFrom, String requestTo) {
        this.requestFrom = requestFrom;
        this.requestTo = requestTo;
    }

    /**
     * This gets the user that the request is from
     * @return The user
     */
    public String getRequestFrom() {
        return requestFrom;
    }

    /**
     * This sets the user that the request is from
     * @param requestFrom The user
     */
    public void setRequestFrom(String requestFrom) {
        this.requestFrom = requestFrom;
    }

    /**
     * This gets the user that the request is sent to
     * @return The user
     */
    public String getRequestTo() {
        return requestTo;
    }

    /**
     * This sets the user that the request is sent to
     * @param requestTo The user
     */
    public void setRequestTo(String requestTo) {
        this.requestTo = requestTo;
    }

    /**
     * This gets the ID of the follow request
     * @return Returns the ID of the follow request
     */
    public String getId() {
        return id;
    }

    /**
     * This sets the ID of the follow request
     * @param id The new ID of the follow request
     */
    public void setId(String id) {
        this.id = id;
    }
}
