package com.kylehodgetts.sunka.controller;

import android.app.Activity;

import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.util.Tuple2;

/**
 * Created by CBaker on 05/11/2015.
 */
public class ViewManager extends EventHandler<GameState> {

    GameState state;

    public ViewManager(EventBus<GameState> bus) {
        super("ViewManager");
    }

    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, GameState state, EventBus<GameState> bus) {
        return null;
    }

    @Override
    public void render(GameState state, Activity activity) {

        
    }
}
