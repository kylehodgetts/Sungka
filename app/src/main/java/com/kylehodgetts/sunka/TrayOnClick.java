package com.kylehodgetts.sunka;

import android.view.View;

import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.event.PlayerMove;
import com.kylehodgetts.sunka.model.GameState;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * A listener for tray clicks
 */
public class TrayOnClick implements View.OnClickListener {

    private int x,y,player;
    private EventBus<GameState> bus;
    private boolean pressed;

    public TrayOnClick(int x, int y, int player, EventBus<GameState> bus) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.bus = bus;
        this.pressed = false;
    }

    @Override
    public void onClick(View v) {
        if (!pressed){
            bus.feedEvent(new PlayerMove(x,y,player));
            pressed = true;
        }
    }
}
