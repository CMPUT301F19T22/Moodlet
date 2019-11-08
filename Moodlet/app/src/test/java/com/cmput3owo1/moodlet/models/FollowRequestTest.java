package com.cmput3owo1.moodlet.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class FollowRequestTest {

    private String user1Username = "User 1";
    private String user2Username = "User 2";
    private User user1 = new User(user1Username);
    private User user2 = new User(user2Username);

    private FollowRequest mockFollowRequest() {
        return new FollowRequest(user1, user2);
    }

    @Test
    public void testConstructor() {
        FollowRequest request = new FollowRequest(user1, user2);
        assertNotNull(request);
        assertEquals(user1, request.getRequestFrom());
        assertEquals(user2, request.getRequestTo());
    }

    @Test
    public void testRequestFrom() {
        FollowRequest request = mockFollowRequest();
        request.setRequestFrom(user2);
        assertEquals(user2, request.getRequestFrom());
    }

    @Test
    public void testRequestTo() {
        FollowRequest request = mockFollowRequest();
        request.setRequestTo(user1);
        assertEquals(user1, request.getRequestTo());
    }
}
