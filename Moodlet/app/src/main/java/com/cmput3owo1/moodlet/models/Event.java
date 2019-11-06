package com.cmput3owo1.moodlet.models;

import android.location.Location;

import java.util.Date;

public abstract class Event {
    protected Date date;
    private Location location;

    public Event() {
        // This sets the current date
        this.date = new Date();
    }
    public Date getDate() {
        return date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
