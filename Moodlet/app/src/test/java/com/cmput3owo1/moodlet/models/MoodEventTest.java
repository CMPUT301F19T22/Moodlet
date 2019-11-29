package com.cmput3owo1.moodlet.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoodEventTest {

    String photographPath = "pathToPhoto";

    @Test
    public void testConstructor() {
        MoodEvent newMoodEvent = new MoodEvent(EmotionalState.EXCITED);
        assertNotNull(newMoodEvent);
        assertEquals(EmotionalState.EXCITED, newMoodEvent.getEmotionalState());
    }

    @Test
    public void testEmotionalState() {
        MoodEvent testEvent = new MoodEvent(EmotionalState.ANGRY);
        assertEquals(EmotionalState.ANGRY, testEvent.getEmotionalState());
        testEvent.setEmotionalState(EmotionalState.CONFUSED);
        assertEquals(EmotionalState.CONFUSED, testEvent.getEmotionalState());
    }

    @Test
    public void testReasoning() {
        MoodEvent testEvent = new MoodEvent(EmotionalState.ANGRY);
        String reason = "Somebody touch spaghet";
        testEvent.setReasoning(reason);
        assertEquals(reason, testEvent.getReasoning());
    }

    @Test
    public void testSocialSituation() {
        MoodEvent testEvent = new MoodEvent(EmotionalState.SAD);
        testEvent.setSocialSituation(SocialSituation.ALONE);
        assertEquals(SocialSituation.ALONE, testEvent.getSocialSituation());
    }

    @Test
    public void testPhotographPath() {
        MoodEvent testEvent = new MoodEvent(EmotionalState.JEALOUS);
        testEvent.setPhotographPath(photographPath);
        assertEquals(photographPath, testEvent.getPhotographPath());
    }

    @Test
    public void testLocationDescription() {
        String locationDescription = "Peppa Pig's House";
        MoodEvent testEvent = new MoodEvent(EmotionalState.JEALOUS);
        testEvent.setLocationDescription(locationDescription);
        assertEquals(locationDescription, testEvent.getLocationDescription());
    }

    @Test
    public void testLocationAddress() {
        MoodEvent testEvent = new MoodEvent(EmotionalState.HAPPY);
        String locationAddress = "123 St, 123 Ave";
        testEvent.setLocationAddress(locationAddress);
        assertEquals(locationAddress, testEvent.getLocationAddress());
    }
}
