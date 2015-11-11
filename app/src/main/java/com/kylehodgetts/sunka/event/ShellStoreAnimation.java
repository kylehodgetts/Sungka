package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 *
 * @author Charlie Baker
 * @version 1.0
 */
public class ShellStoreAnimation implements Event {

    private int fromTray;
    private int player;


    public ShellStoreAnimation(int fromTray, int player) {
        this.fromTray = fromTray;
        this.player = player;
    }

    public int getFromTray() {
        return fromTray;
    }

    public int getPlayer() {
        return player;
    }
    
}
