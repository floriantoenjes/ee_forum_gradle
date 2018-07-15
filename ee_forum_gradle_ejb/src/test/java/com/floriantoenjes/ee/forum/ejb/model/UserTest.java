package com.floriantoenjes.ee.forum.ejb.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void addPost() {
        User user = new User();

        user.addPost(new Post());

        assertEquals(user.getPostCount().longValue(), 1);
    }
}