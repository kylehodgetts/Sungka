package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.model.Player;

/**
 * @author Charlie Baker
 * @version 1.0
 * Class denoting a highlighted tray event
 */
public class HighLightTray implements Event {

    private int tray;
    private int player;
    private int currentPlayersTurn;

    public HighLightTray(int player, int tray, int currentPlayersTurn) {
        this.player = player;
        this.tray = tray;
        this.currentPlayersTurn = currentPlayersTurn;
    }

    public int getTray() {
        return tray;
    }

    public int getPlayer() {
        return player;
    }

    public int getCurrentPlayersTurn() { return currentPlayersTurn; }
}
