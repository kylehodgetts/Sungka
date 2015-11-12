package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * @author Jonathan
 * @version 1.0
 *          Event that notifies that shells are about to be placed in pot
 */
public class ShellMovementToPot implements Event {

    private int playerPotIndex;
    private int nextPlayerIndex;
    private int shells;

    public ShellMovementToPot(int playerPotIndex, int shellsStillToBePlaced) {
        this.playerPotIndex = playerPotIndex;
        this.nextPlayerIndex = (playerPotIndex + 1) % 2;
        this.shells = shellsStillToBePlaced;
    }

    public int getShells() {
        return shells;
    }

    public int getPlayerIndexOfThisPot() {
        return playerPotIndex;
    }

    public int getNextPlayerIndex() {
        return nextPlayerIndex;
    }


}
