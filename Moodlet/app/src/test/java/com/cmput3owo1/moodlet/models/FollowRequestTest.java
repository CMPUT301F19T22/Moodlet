package com.cmput3owo1.moodlet.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class FollowRequestTest {

    private String user1Username = "User 1";
    private String user2Username = "User 2";

    private FollowRequest mockFollowRequest() {
        return new FollowRequest(user1Username, user2Username);
    }

    @Test
    public void testConstructor() {
        FollowRequest request = new FollowRequest(user1Username, user2Username);
        assertNotNull(request);
        assertEquals(user1Username, request.getRequestFrom());
        assertEquals(user2Username, request.getRequestTo());
    }

    @Test
    public void testRequestFrom() {
        FollowRequest request = mockFollowRequest();
        request.setRequestFrom(user2Username);
        assertEquals(user2Username, request.getRequestFrom());
    }

    @Test
    public void testRequestTo() {
        FollowRequest request = mockFollowRequest();
        request.setRequestTo(user1Username);
        assertEquals(user1Username, request.getRequestTo());
    }
}
