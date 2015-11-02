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

    //TODO change order of parameters to match Board class ordering
    public ShellMovement(int trayIndex, int playerIndex, int shellsStillToBePlaced, boolean isFirstShellMoved, int player) {
        this.trayIndex = trayIndex;
        this.playerIndex = playerIndex;
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
