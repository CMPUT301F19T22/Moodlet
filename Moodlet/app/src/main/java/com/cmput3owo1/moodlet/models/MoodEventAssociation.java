package com.cmput3owo1.moodlet.models;

/**
 * This is a simple class to match a {@link MoodEvent} to the user that it belongs to.
 */
public class MoodEventAssociation {
    private String username;
    private MoodEvent moodEvent;

    /**
     * Constructor takes the MoodEvent and the username of the user it belongs to
     * @param moodEvent The mood event
     * @param username The username
     */
    public MoodEventAssociation(MoodEvent moodEvent, String username) {
        this.moodEvent = moodEvent;
        this.username = username;
    }

    /**
     * Returns the username that is part of the association
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the MoodEvent that is part of the association
     * @return The mood event
     */
    public MoodEvent getMoodEvent() {
        return moodEvent;
    }
}
