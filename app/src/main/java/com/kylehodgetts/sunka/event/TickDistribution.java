package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Event that notifies that there has been next tick for the moving of the beads in trays
 */
public class TickDistribution implements Event {
    private int x,y;
    private int left;
    private boolean first;

    public TickDistribution(int x, int y, int left, boolean first) {
        this.x = x;
        this.y = y;
        this.left = left;
        this.first = first;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLeft() {
        return left;
    }

    public boolean isFirst() {
        return first;
    }
}
