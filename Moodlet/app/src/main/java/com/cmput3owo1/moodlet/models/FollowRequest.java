package com.cmput3owo1.moodlet.models;

public class FollowRequest {
    private FollowableUser requestFrom;
    private FollowableUser requestTo;

    public FollowRequest(FollowableUser requestFrom, FollowableUser requestTo) {
        this.requestFrom = requestFrom;
        this.requestTo = requestTo;
    }

    public FollowableUser getRequestFrom() {
        return requestFrom;
    }

    public FollowableUser getRequestTo() {
        return requestTo;
    }
}
