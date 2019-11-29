package com.cmput3owo1.moodlet.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    private static String username = "UwU";
    private static String email = "myemail@email.com";
    private static String fullName = "Peppa Pig";

    private User mockUser() {
        return new User(username);
    }

    @Test
    public void testUsernameConstructor() {
        User user = new User(username);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertFalse(user.isFollowing());
        assertFalse(user.isRequested());
    }

    @Test
    public void testMultipleArgumentConstructor() {
        User user = new User(username, fullName, email);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals(fullName, user.getFullName());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testUsername() {
        User user = mockUser();
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testFullName() {
        User user = mockUser();
        user.setFullName(fullName);
        assertEquals(fullName, user.getFullName());
    }

    @Test
    public void testEmail() {
        User user = mockUser();
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testRequested() {
        User user = mockUser();
        user.setRequested(true);
        assertTrue(user.isRequested());
    }

    @Test
    public void testFollowing() {
        User user = mockUser();
        user.setFollowing(true);
        assertTrue(user.isFollowing());
    }
}
