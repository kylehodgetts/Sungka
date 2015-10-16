package com.kylehodgetts.sunka.model;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Tests for the Player model
 */
public class PlayerTest {

    private Player p;

    @Before
    public void setUp(){
        p = new Player();
    }

    /**
     * Ensure that the stones in the pot is not a negative value
     */
    @Test
    public void getStonesInPot_isValid() {
        assertTrue(p.getStonesInPot() > -1);
    }

    /**
     * Ensure adding stones to the pot increments the value of stones in the pot
     */
    @Test
    public void addToPot_Increments() {
        int beforeAdding = p.getStonesInPot();
        p.addToPot(1);
        assertEquals(beforeAdding + 1, p.getStonesInPot());
    }

}