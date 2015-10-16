package com.kylehodgetts.sunka.model;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Models the player in the game
 */
public class Player {
    private int stonesInPot;

    /**
     * Creates a new player setting stones in pot to 0
     */
    public Player(){
        stonesInPot = 0;
    }

    /**
     * Adds <code>x</code> amount of stones to pot
     * @param x number to collected stones
     */
    public void addToPot(int x) {
        stonesInPot += x;
    }

}
