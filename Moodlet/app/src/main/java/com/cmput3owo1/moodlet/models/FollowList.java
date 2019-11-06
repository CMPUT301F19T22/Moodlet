package com.cmput3owo1.moodlet.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FollowList {
    private List<FollowableUser> users;

    public FollowList() {
        users = new ArrayList<>();
    }

    public void add(FollowableUser user) {
        if (hasUser(user)) {
            throw new IllegalArgumentException();
        }
        users.add(user);
    }

    public boolean hasUser(FollowableUser user) {
        return hasUser(user.getUsername());
    }

    public boolean hasUser(String username) {
        for (FollowableUser u : users) {
            if (u.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public Collection<FollowableUser> getUsers() {
        return users;
    }

    public int size() {
        return users.size();
    }
}
