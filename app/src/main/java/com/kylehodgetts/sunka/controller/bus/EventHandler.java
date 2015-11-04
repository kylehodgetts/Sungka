package com.kylehodgetts.sunka.controller.bus;

import android.app.Activity;

import com.kylehodgetts.sunka.util.Tuple2;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          A handler for events in the event bus, ie controller
 */
public abstract class EventHandler<T extends BusState> {

    private String id;

    /**
     * Default constructor for event handler, assigns its id that should be unique
     *
     * @param id
     */
    public EventHandler(String id) {
        this.id = id;
    }

    /**
     * A method that processes events that were send to a event bus
     *
     * @param event The incoming event from the bus to this handler
     * @param state The current state of the bus
     * @return Returns the modified state together with a flag whether a render is required
     */
    public abstract Tuple2<T, Boolean> handleEvent(Event event, T state);

    /**
     * A method that renders the application, any changes to the view should be here
     * This is called only if any handler required a render
     * Will be called only once for every cascading events
     *
     * @param state    The current state of the event bus
     * @param activity The active activity
     */
    public abstract void updateView(T state, Activity activity);

    /**
     * Getter for id of event handler
     *
     * @return
     */
    public String getId() {
        return id;
    }
}
