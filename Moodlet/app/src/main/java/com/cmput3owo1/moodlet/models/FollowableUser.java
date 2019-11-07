package com.cmput3owo1.moodlet.models;

import java.util.ArrayList;
import java.util.List;


public class FollowableUser extends User {
    private FollowList following;
    private FollowList  followers;
    private List<FollowRequest> followRequests;

    public FollowableUser(String username) {
        super(username);
        following = new FollowList();
        followers = new FollowList();
        followRequests = new ArrayList<>();
    }

    public FollowList getFollowers() {
        return followers;
    }

    public FollowList getFollowing() {
        return following;
    }

    public List<FollowRequest> getFollowRequests() {
        return followRequests;
    }
}
