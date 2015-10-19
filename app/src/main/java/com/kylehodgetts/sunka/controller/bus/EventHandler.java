package com.kylehodgetts.sunka.controller.bus;

import android.app.Activity;

import com.kylehodgetts.sunka.util.Tuple2;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public abstract class EventHandler<T extends BusState> {

    private String id;

    public EventHandler(String id){
        this.id = id;
    }

    public abstract Tuple2<T,Boolean> handleEvent(Event event, T state, EventBus<T> bus);

    public abstract void render(T state, Activity activity);

    public String getId() {
        return id;
    }
}
