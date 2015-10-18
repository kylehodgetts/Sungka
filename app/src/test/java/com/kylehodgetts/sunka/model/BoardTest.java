package com.kylehodgetts.sunka.model;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class for the Board Class
 */
public class BoardTest {

    private Board board;    // field to hold a board object

    /**
     * Set's up a new board object before each test is run ensuring it is at its default
     * state before the test.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        board = new Board();
    }

    /**
     * Test the setTray method where each tray is set to its column number and checked it has been
     * changed by getting the result at the tray cell location matches the current column number
     * @throws Exception
     */
    @Test
    public void testSetTray() throws Exception {
        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                board.setTray(i, j, j);
                Assert.assertEquals("setTray(" + i + "," + j + "," + j + ") not working", board.getTray(i, j), j);
            }
        }
    }

    /**
     * Test the get tray method retrieves the default number seven which each tray should be set up to hold.
     * @throws Exception
     */
    @Test
    public void testGetTray() throws Exception {
        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                Assert.assertEquals("Tray [" + i + "," + j + "] != 7 initial stones", board.getTray(i, j), 7);
            }
        }
    }

    /**
     * Tests that each tray increments by 1 and contains 8
     * @throws Exception
     */
    @Test
    public void testIncrementTray() throws Exception {
        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                board.incrementTray(i, j);
                Assert.assertEquals("increment on [" + i + "," + j + "] failed", board.getTray(i, j), 8);
            }
        }
    }

    /**
     * Tests that each tray decrements by 1 and is holding 6
     * @throws Exception
     */
    @Test
    public void testDecrementTray() throws Exception {
        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                board.decrementTray(i, j);
                Assert.assertEquals("increment on [" + i + "," + j + "] failed", board.getTray(i, j), 6);
            }
        }
    }

    /**
     * Tests that each tray is emptied and then contains 0 shells
     * @throws Exception
     */
    @Test
    public void testEmptyTray() throws Exception {
        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                board.emptyTray(i, j);
                Assert.assertEquals("emptyTray on [" + i + "," + j + "] failed", board.getTray(i, j), 0);
            }
        }
    }

    /**
     * Tests that after the tray is emptied the isEmpty method returns true
     * @throws Exception
     */
    @Test
    public void testIsEmptyTray() throws Exception {
        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                board.emptyTray(i, j);
                Assert.assertTrue("IsemptyTray on [" + i + "," + j + "] failed", board.isEmptyTray(i, j));
            }
        }
    }
}