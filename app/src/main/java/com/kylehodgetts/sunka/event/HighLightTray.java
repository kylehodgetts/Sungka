package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * @author Charlie Baker
 * @version 1.0
 * Class denoting a highlighted tray event
 */
public class HighLightTray implements Event {

    private int tray;
    private int player;
    private int currentPlayersTurn;

    /**
     * Default constructor
     * @param player                The side of the board to be highlighted
     * @param tray                  The tray index to be highlighted
     * @param currentPlayersTurn    The current <code>Player</code>
     */
    public HighLightTray(int player, int tray, int currentPlayersTurn) {
        this.player = player;
        this.tray = tray;
        this.currentPlayersTurn = currentPlayersTurn;
    }

    /**
     *
     * @return the tray index
     */
    public int getTray() {
        return tray;
    }

    /**
     *
     * @return the side index
     */
    public int getPlayer() {
        return player;
    }

    /**
     *
     * @return the current <code>Player</code>
     */
    public int getCurrentPlayersTurn() { return currentPlayersTurn; }
}
