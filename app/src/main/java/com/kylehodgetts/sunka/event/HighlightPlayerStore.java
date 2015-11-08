package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * Event created to highlight a player's store when a move is in progress to provide the user with
 * some visual feedback as to which player's store a shell is being added to.
 *
 * @author Charlie Baker
 * @version 1.0
 */
public class HighlightPlayerStore implements Event {

    private int player;

    /**
     * Default constructor to initiate the event with the player who is playing their turn.
     *
     * @param player Integer representing which is the current player.
     */
    public HighlightPlayerStore(int player) {
        this.player = player;
    }


    /**
     * Get's the current player who initiated this event.
     *
     * @return An integer representing the player who initiated this event.
     */
    public int getPlayer() {
        return player;
    }

}
