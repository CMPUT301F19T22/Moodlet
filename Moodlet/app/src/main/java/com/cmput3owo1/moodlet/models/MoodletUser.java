package com.cmput3owo1.moodlet.models;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

public class MoodletUser extends FollowableUser {
    private Deque<MoodEvent> moodEventHistory;

    public MoodletUser(String username) {
        super(username);
        moodEventHistory = new ArrayDeque<>();
    }

    public Collection<MoodEvent> getMoodEventHistory() {
        return moodEventHistory;
    }

    public MoodEvent getRecentMoodEvent() {
        return moodEventHistory.getLast();
    }
}
