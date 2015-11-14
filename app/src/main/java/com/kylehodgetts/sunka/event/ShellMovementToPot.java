package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * @author Jonathan Burton
 * @version 1.0
 *          Event that notifies that shells are about to be placed in pot
 */
public class ShellMovementToPot implements Event {

    private int playerPotIndex;
    private int nextPlayerIndex;
    private int shells;

    /**
     * Default constructor
     * @param playerPotIndex            Side of the board
     * @param shellsStillToBePlaced     Number of shells to be placed
     */
    public ShellMovementToPot(int playerPotIndex, int shellsStillToBePlaced) {
        this.playerPotIndex = playerPotIndex;
        this.nextPlayerIndex = (playerPotIndex + 1) % 2;
        this.shells = shellsStillToBePlaced;
    }

    /**
     *
     * @return return the number of shells
     */
    public int getShells() {
        return shells;
    }

    /**
     *
     * @return get the player index for a given pot
     */
    public int getPlayerIndexOfThisPot() {
        return playerPotIndex;
    }

    /**
     *
     * @return return the index of the next player
     */
    public int getNextPlayerIndex() {
        return nextPlayerIndex;
    }


}
