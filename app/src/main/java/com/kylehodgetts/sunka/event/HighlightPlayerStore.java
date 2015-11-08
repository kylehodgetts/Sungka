package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 *
 * @author Charlie Baker
 * @version 1.0
 */
public class HighlightPlayerStore implements Event {

    private int player;


    public HighlightPlayerStore(int player) {
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }

}
