package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          Event that notifies that the player has selected next tray to be played
 */
public class PlayerChoseTray implements Event {
    private int trayIndex, playerIndex;

    public PlayerChoseTray(int playerIndex, int trayIndex) {
        this.playerIndex = playerIndex;
        this.trayIndex = trayIndex;
    }

    public int getTrayIndex() {
        return trayIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
