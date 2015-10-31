package com.kylehodgetts.sunka.controller;

import android.app.Activity;

import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.util.Tuple2;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class StatiscticController extends EventHandler<GameState> {



    /**
     * Default constructor for event handler, assigns its id that should be unique
     *
     * @param id
     */
    public StatiscticController(String id) {
        super(id);
    }

    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, GameState state, EventBus<GameState> bus) {
        return null;
    }

    @Override
    public void render(GameState state, Activity activity) {

    }
}
