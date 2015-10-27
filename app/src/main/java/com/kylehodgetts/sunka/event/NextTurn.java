package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Event that notifies that the next turn has began
 */
public class NextTurn implements Event {

    private int player;

    public NextTurn(int player) {
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }
}
