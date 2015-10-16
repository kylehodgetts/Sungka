package com.kylehodgetts.sunka.model;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by CBaker on 16/10/2015.
 */
public class BoardTest extends TestCase {

    private Board board;

    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        board = new Board();
    }

    @Test
    public void testSetPot() throws Exception
    {
        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                board.setPot(i, j, j);
                assertEquals("setPot("+i+","+j+","+j+") not working", board.getPot(i, j), j);
            }
        }
    }

    @Test
    public void testGetPot() throws Exception
    {
        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                assertEquals("Pot ["+i+","+j+"] != 7 initial stones", board.getPot(i, j), 7);
            }
        }
    }

    @Test
    public void testIncrementPot() throws Exception
    {
        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                board.incrementPot(i, j);
                assertEquals("increment on ["+i+","+j+"] failed", board.getPot(i, j), 8);
            }
        }
    }

    @Test
    public void testDecrementPot() throws Exception
    {
        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                board.decrementPot(i, j);
                assertEquals("increment on ["+i+","+j+"] failed", board.getPot(i, j), 6);
            }
        }
    }

    @Test
    public void testEmptyPot() throws Exception
    {
        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                board.emptyPot(i, j);
                assertEquals("emptyPot on ["+i+","+j+"] failed", board.getPot(i, j), 0);
            }
        }
    }

    @Test
    public void testIsEmptyPot() throws Exception
    {
        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                board.emptyPot(i, j);
                assertTrue("IsemptyPot on ["+i+","+j+"] failed", board.isEmptyPot(i, j));
            }
        }
    }
}