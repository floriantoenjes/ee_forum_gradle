package com.floriantoenjes.ee.forum.ejb.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ThreadTest {
    private Board board;
    private Thread thread;


    @Before
    public void setUp() throws Exception {
        board = new Board();
        thread = new Thread();
        board.addThread(thread);
    }

    @Test
    public void addPostShouldSetLastPost() {
        Post post = new Post();

        thread.addPost(post);

        assertEquals(thread.getLastPost(), post);
        assertEquals(thread.getPostCount().longValue(), 1);
        assertEquals(board.getLastThread(), thread);
    }

    @Test
    public void removePostShouldRemoveLastPost() {
        Post post = new Post();
        thread.addPost(post);

        thread.removePost(post);

        assertNull(post.getThread());
        assertEquals(thread.getPostCount().longValue(), 0);
    }

    @Test
    public void removePostShouldDecrementPostNumbers() {
        Post post1 = new Post();
        Post post2 = new Post();
        thread.addPost(post1);
        thread.addPost(post2);

        thread.removePost(post1);

        assertEquals(post2.getPostNumber().longValue(), 1);
    }

    @Test
    public void clearLastPost() {
        Post post = new Post();
        thread.addPost(post);

        thread.clearLastPost();

        assertNull(thread.getLastPost());
    }

    @Test
    public void getPages() {
        for (int i = 0; i < 15; i++) {
            thread.addPost(new Post());
        }

        assertEquals(thread.getPages(), 2);
    }
}