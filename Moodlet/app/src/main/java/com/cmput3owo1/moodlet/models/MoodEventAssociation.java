package com.cmput3owo1.moodlet.models;

public class MoodEventAssociation {
    private String username;
    private MoodEvent moodEvent;

    public MoodEventAssociation(MoodEvent moodEvent, String username) {
        this.moodEvent = moodEvent;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public MoodEvent getMoodEvent() {
        return moodEvent;
    }
}
