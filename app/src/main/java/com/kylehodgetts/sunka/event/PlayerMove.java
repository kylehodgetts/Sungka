package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Event that notifies that the player has selected next tray to be played
 */
public class PlayerMove implements Event {
    private int x,y;
    private int player;


    public PlayerMove(int x, int y, int player) {
        this.x = x;
        this.y = y;
        this.player = player;

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlayer() {
        return player;
    }
}
