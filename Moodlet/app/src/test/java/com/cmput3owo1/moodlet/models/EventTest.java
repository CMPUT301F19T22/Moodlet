package com.cmput3owo1.moodlet.models;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class EventTest {
    Date date = new Date();
    GeoPoint geoPoint = new GeoPoint(0,0);

    private Event mockEvent() {
        Event mockEvent = new Event();
        mockEvent.setDate(date);
        mockEvent.setLocation(geoPoint);
        return mockEvent;
    }

    @Test
    public void testConstructor() {
        Date testDate = new Date();
        Event newEvent = new Event();
        assertNotNull(newEvent);
        assertEquals(testDate.getTime(), newEvent.getDate().getTime(), 1000);
    }

    @Test
    public void testDate() {
        Event newEvent = mockEvent();
        newEvent.setDate(date);
        assertEquals(date, newEvent.getDate());
    }

    @Test
    public void testLocation() {
        Event newEvent = mockEvent();
        newEvent.setLocation(geoPoint);
        assertEquals(geoPoint, newEvent.getLocation());
    }
}
