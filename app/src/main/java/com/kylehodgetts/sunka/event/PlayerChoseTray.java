package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          Event that notifies that the player has selected next tray to be played
 */
public class PlayerChoseTray implements Event {
    private int trayIndex, playerIndex;


    //TODO change order of parameters to match Board class ordering
    public PlayerChoseTray(int trayIndex, int playerIndex) {
        this.trayIndex = trayIndex;
        this.playerIndex = playerIndex;

    }

    public int getTrayIndex() {
        return trayIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
