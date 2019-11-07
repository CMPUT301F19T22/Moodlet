package com.cmput3owo1.moodlet.models;

import android.location.Location;

import java.util.Date;

/**
 * This is a generic class that keeps track of an Event. The event has a date set to the current
 * date and a location that can be set.
 */
public class Event {
    private Date date;
    private Location location;

    /**
     * Constructor for the event class. Sets the date to the current date
     */
    public Event() {
        // This sets the current date
        this.date = new Date();
    }

    /**
     * This returns the date when the event occurred
     * @return Return the event date
     */
    public Date getDate() {
        return date;
    }

    /**
     * This returns the location of the event
     * @return Return the event location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * This sets the event location
     * @param location The event location
     */
    public void setLocation(Location location) {
        this.location = location;
    }
}
