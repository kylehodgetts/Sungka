package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.model.Player;

/**
 * Created by CBaker on 06/11/2015.
 */
public class HighLightTray implements Event {

    private int tray;
    private int player;
    private int currentPlayersTurn;

    public HighLightTray(int tray, int player, int currentPlayersTurn) {
        this.tray = tray;
        this.player = player;
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
