package com.kylehodgetts.sunka.event;

import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.model.Player;

/**
 * Created by CBaker on 06/11/2015.
 */
public class HighLightTray implements Event {

    private int player;
    private int tray;
    private boolean setHighlighted;

    public HighLightTray(int player, boolean setHighlighted, int tray) {
        this.player = player;
        this.setHighlighted = setHighlighted;
        this.tray = tray;
    }

    public int getTray() {
        return tray;
    }

    public boolean isSetHighlighted() {
        return setHighlighted;
    }

    public int getPlayer() {
        return player;
    }
}
