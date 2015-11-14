package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          Event that notifies that there has been next tick for the moving of the beads in trays
 *          Was: TickDistribution
 */
public class ShellMovement implements Event {
    private int trayIndex, playerIndex;
    private int shellsStillToBePlaced;
    private int player;

    /**
     * Default constructor
     * @param playerIndex               The side of the board
     * @param trayIndex                 The tray index
     * @param shellsStillToBePlaced     Amount of shells to be moved to new trays
     * @param player                    Current <code>Player</code>
     */
    public ShellMovement(int playerIndex, int trayIndex, int shellsStillToBePlaced, int player) {
        this.playerIndex = playerIndex;
        this.trayIndex = trayIndex;
        this.shellsStillToBePlaced = shellsStillToBePlaced;
        this.player = player;
    }

    /**
     *
     * @return the tray index
     */
    public int getTrayIndex() {
        return trayIndex;
    }

    /**
     *
     * @return the side of the board
     */
    public int getPlayerIndex() {
        return playerIndex;
    }

    /**
     *
     * @return number of shells left to be placed into new trays
     */
    public int getShellsStillToBePlaced() {
        return shellsStillToBePlaced;
    }

    /**
     *
     * @return index of current <code>Player</code>
     */
    public int getPlayer() {
        return player;
    }
}
