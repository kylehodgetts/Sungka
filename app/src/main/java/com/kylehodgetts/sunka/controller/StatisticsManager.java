package com.kylehodgetts.sunka.controller;

import android.app.Activity;

import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.util.Tuple2;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class StatisticsManager extends EventHandler<GameState> {
    /**
     * Default constructor for event handler, assigns its id that should be unique
     *
     */
    public StatisticsManager() {
        super("StatisticsManager");
    }

    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, GameState state) {

        return new Tuple2<>(state,false);
    }

    @Override
    public void updateView(GameState state, Activity activity) {

    }
}
