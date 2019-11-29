package com.cmput3owo1.moodlet.utils;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.EmotionalState;

import org.junit.Test;

import java.util.Calendar;

import static com.cmput3owo1.moodlet.utils.Utils.*;
import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void testGetTimeDifferenceYear() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, date.get(Calendar.YEAR)-4);
        String timeDifference = getTimeDifference(date.getTime());

        assertEquals(timeDifference, "4 y");
    }

    @Test
    public void testGetTimeDifferenceDay() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR, date.get(Calendar.HOUR)-6);
        String timeDifference = getTimeDifference(date.getTime());

        assertEquals(timeDifference, "6 h");
    }

    @Test
    public void testGetTimeDifferenceHour() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR, date.get(Calendar.HOUR)-2);
        String timeDifference = getTimeDifference(date.getTime());

        assertEquals(timeDifference, "2 h");
    }

    @Test
    public void testGetTimeDifferenceMinute() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.MINUTE, date.get(Calendar.MINUTE)-10);
        String timeDifference = getTimeDifference(date.getTime());

        assertEquals(timeDifference, "10 m");
    }

    @Test
    public void testGetTimeDifferenceSecond() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.SECOND, date.get(Calendar.SECOND)-20);
        String timeDifference = getTimeDifference(date.getTime());

        assertEquals(timeDifference, "20 s");
    }

    @Test
    public void testGetTimeDifferenceNow() {
        Calendar date = Calendar.getInstance();
        String timeDifference = getTimeDifference(date.getTime());

        assertEquals(timeDifference, "Just now");
    }

    @Test
    public void testGetSadMoodEmoticon() {
        int emoticonResource = getMoodEmoticon(EmotionalState.SAD);

        assertEquals(emoticonResource, R.drawable.ic_mood_sad);
    }

    @Test
    public void testGetAngryMoodEmoticon() {
        int emoticonResource = getMoodEmoticon(EmotionalState.ANGRY);

        assertEquals(emoticonResource, R.drawable.ic_mood_angry);
    }

    @Test
    public void testGetConfusedMoodEmoticon() {
        int emoticonResource = getMoodEmoticon(EmotionalState.CONFUSED);

        assertEquals(emoticonResource, R.drawable.ic_mood_confused);
    }

    @Test
    public void testGetExcitedMoodEmoticon() {
        int emoticonResource = getMoodEmoticon(EmotionalState.EXCITED);

        assertEquals(emoticonResource, R.drawable.ic_mood_excited);
    }

    @Test
    public void testGetHappyMoodEmoticon() {
        int emoticonResource = getMoodEmoticon(EmotionalState.HAPPY);

        assertEquals(emoticonResource, R.drawable.ic_mood_happy);
    }

    @Test
    public void testGetJealousMoodEmoticon() {
        int emoticonResource = getMoodEmoticon(EmotionalState.JEALOUS);

        assertEquals(emoticonResource, R.drawable.ic_mood_jealous);
    }

    @Test
    public void testGetScaredMoodEmoticon() {
        int emoticonResource = getMoodEmoticon(EmotionalState.SCARED);

        assertEquals(emoticonResource, R.drawable.ic_mood_scared);
    }
}
