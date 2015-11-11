package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 *
 * @author Charlie Baker
 * @version 1.0
 */
public class ShellTrayAnimation implements Event {

    private int shellsToMove;
    private int player;
    private int toTray;
    private int fromTray;


    public ShellTrayAnimation(int toTray, int fromTray, int player, int shellsToMove) {
        this.player = player;
        this.shellsToMove = shellsToMove;
        this.toTray = toTray;
        this.fromTray = fromTray;

    }

    public int getPlayer() {
        return player;
    }

    public int getShellsToMove() {
        return shellsToMove;
    }

    public int getToTray() {
        return toTray;
    }

    public int getFromTray() {
        return fromTray;
    }
}
