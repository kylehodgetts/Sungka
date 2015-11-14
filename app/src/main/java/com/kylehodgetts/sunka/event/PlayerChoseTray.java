package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          Event that notifies that the player has selected next tray to be played
 */
public class PlayerChoseTray implements Event {
    private int trayIndex, playerIndex;

    /**
     * Default constructor
     * @param playerIndex   The side of the tray chosen
     * @param trayIndex     The tray index chosen
     */
    public PlayerChoseTray(int playerIndex, int trayIndex) {
        this.playerIndex = playerIndex;
        this.trayIndex = trayIndex;
    }

    /**
     *
     * @return the chosen tray index
     */
    public int getTrayIndex() {
        return trayIndex;
    }

    /**
     *
     * @return the chosen side index
     */
    public int getPlayerIndex() {
        return playerIndex;
    }
}
