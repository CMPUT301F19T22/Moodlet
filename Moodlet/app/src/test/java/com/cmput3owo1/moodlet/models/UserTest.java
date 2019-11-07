package com.cmput3owo1.moodlet.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private static String username = "UwU";

    private User mockUser() {
        return new User(username);
    }

    @Test
    public void testConstructor() {
        User user = new User(username);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testUsername() {
        User user = mockUser();
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testFullName() {
        User user = mockUser();
        String fullName = "U Wu";
        user.setFullName(fullName);
        assertEquals(fullName, user.getFullName());
    }
}
