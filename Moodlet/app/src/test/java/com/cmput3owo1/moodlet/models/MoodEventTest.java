package com.cmput3owo1.moodlet.models;

import android.graphics.Bitmap;

import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

public class MoodEventTest {

    @Mock
    Bitmap bitmap;

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
    public void testPhotograph() {
        MoodEvent testEvent = new MoodEvent(EmotionalState.JEALOUS);
        testEvent.setPhotograph(bitmap);
        assertEquals(bitmap, testEvent.getPhotograph());
    }
}
