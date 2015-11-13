package com.kylehodgetts.sunka.model;

import java.io.Serializable;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Models the player in the game
 */
public class Player implements Serializable {
    private int stonesInPot;
    private int wonGames;
    private int side;


    /**
     * Creates a new player setting stones in pot to 0
     */
    public Player(){
        stonesInPot = 0;
    }

    /**
     * @return the side the user is on
     */
    public int getSide() {
        return side;
    }

    /**
     *
     * @param side set the side the user
     */
    public void setSide(int side) {
        this.side = side;
    }

    /**
     *
     * @return the number of games the player has won
     */
    public int getWonGames() {
        return wonGames;
    }

    /**
     * Increment the number of games the user has won by one.
     */
    public void addWonGames() {
        wonGames++;
    }

    /**
     * @return number of stones in the player's pot
     */
    public int getStonesInPot(){
        return stonesInPot;
    }

    public void setStonesInPot(int stones) {
        stonesInPot = stones;
    }

    /**
     * Adds <code>x</code> amount of stones to pot
     * @param x number to collected stones
     */
    public void addToPot(int x) {
        stonesInPot += x;
    }

    public void resetStonesInPot(){
        stonesInPot = 0;
    }
}