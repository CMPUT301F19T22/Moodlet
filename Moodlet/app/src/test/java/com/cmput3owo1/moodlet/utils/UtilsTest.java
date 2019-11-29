package com.cmput3owo1.moodlet.utils;

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
}
