package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * Created by CBaker on 13/11/2015.
 */
public class ShellSteal implements Event {

    private int player;
    private int trayToStealFrom;
    private int playersTray;


    public ShellSteal(int player, int playersTray, int trayToStealFrom) {

        this.player = player;
        this.playersTray = playersTray;
        this.trayToStealFrom = trayToStealFrom;
    }

    public int getPlayer() {
        return player;
    }

    public int getPlayersTray() {
        return playersTray;
    }

    public int getTrayToStealFrom() {
        return trayToStealFrom;
    }

}
