package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * Steal {@link Event} class in order to notify the relevant {@link com.kylehodgetts.sunka.controller.bus.EventHandler}
 * that the capture rule applies as the current {@link com.kylehodgetts.sunka.model.Player} steals the
 * shells from their opponents tray.
 *
 */
public class ShellSteal implements Event {

    private int player;             // Current player
    private int trayToStealFrom;    // Tray number to steal from
    private int playersTray;        // Player's tray to move their one shell from


    /**
     * Default constructor to create the {@link Event}
     *
     * @param player the current player
     * @param playersTray the current player's tray
     * @param trayToStealFrom the tray number to steal from
     */
    public ShellSteal(int player, int playersTray, int trayToStealFrom) {

        this.player = player;
        this.playersTray = playersTray;
        this.trayToStealFrom = trayToStealFrom;
    }

    /**
     * Get's the current player
     * @return current player as an integer
     */
    public int getPlayer() {
        return player;
    }

    /**
     * Get's the current player's tray
     * @return the current player's tray as an integer
     */
    public int getPlayersTray() {
        return playersTray;
    }

    /**
     * Get's the tray to steal from
     * @return the oppenents player's tray as an integer
     */
    public int getTrayToStealFrom() {
        return trayToStealFrom;
    }

}
