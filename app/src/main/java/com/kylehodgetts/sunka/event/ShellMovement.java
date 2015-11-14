package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          Event that notifies that there has been next tick for the moving of the beads in trays
 *          Was: TickDistribution
 */
public class ShellMovement implements Event {
    private int trayIndex, playerIndex;
    private int shellsStillToBePlaced;
    private boolean isFirstShellMoved;
    private int player;

    public ShellMovement(int playerIndex, int trayIndex, int shellsStillToBePlaced, boolean isFirstShellMoved, int player) {
        this.playerIndex = playerIndex;
        this.trayIndex = trayIndex;
        this.shellsStillToBePlaced = shellsStillToBePlaced;
        this.isFirstShellMoved = isFirstShellMoved;
        this.player = player;
    }

    public int getTrayIndex() {
        return trayIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getShellsStillToBePlaced() {
        return shellsStillToBePlaced;
    }

    public boolean isFirstShellMoved() {
        return isFirstShellMoved;
    }

    public int getPlayer() {
        return player;
    }
}
