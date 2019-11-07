package com.cmput3owo1.moodlet.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoodEventAssociationTest {

    private MoodEvent moodEvent = new MoodEvent(EmotionalState.HAPPY);
    private String username = "UwU";

    private MoodEventAssociation mockMoodEventAssociation() {
        return new MoodEventAssociation(moodEvent, username);
    }

    @Test
    public void testConstructor() {
        MoodEventAssociation moodEventAssociation = new MoodEventAssociation(new MoodEvent(EmotionalState.CONFUSED), "Test");
        assertNotNull(moodEventAssociation);
    }

    @Test
    public void testGetMoodEvent() {
        MoodEventAssociation moodEventAssociation = mockMoodEventAssociation();
        assertEquals(moodEvent, moodEventAssociation.getMoodEvent());
    }

    @Test
    public void getUsername() {
        MoodEventAssociation moodEventAssociation = mockMoodEventAssociation();
        assertEquals(username, moodEventAssociation.getUsername());
    }
}
