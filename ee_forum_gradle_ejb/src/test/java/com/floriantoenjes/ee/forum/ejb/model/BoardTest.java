package com.floriantoenjes.ee.forum.ejb.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {
    private Board board;


    @Before
    public void setUp() throws Exception {
        board = new Board();
    }

    @Test
    public void addThreadShouldIncrementThreadCount() {
        Thread thread = new Thread();

        board.addThread(thread);

        assertEquals(board.getThreadCount().longValue(), 1L);
    }

    @Test
    public void removeThreadShouldDecrementThreadCount() {
        Thread thread = new Thread();
        board.addThread(thread);

        board.removeThread(thread);

        assertEquals(board.getThreadCount().longValue(), 0L);
    }

    @Test
    public void clearLastThreadShouldReturnLastThread() {
        Thread thread = new Thread();
        board.setLastThread(thread);

        assertEquals(board.clearLastThread().get(), thread);
        assertNull(board.getLastThread());
    }

    @Test
    public void clearLastThreadShouldReturnEmptyOptional() {
        assertFalse(board.clearLastThread().isPresent());
    }
}