package com.kylehodgetts.sunka.controller.bus;

import android.app.Activity;

import com.kylehodgetts.sunka.util.Tuple2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <p/>
 *          A stateful event bus.
 *          <p/>
 *          A messaging system for components of the application. Every single component/controller can be
 *          represented as an event handler. The event handler then listens on any event
 *          that is being send to the bus. The event is send to every handler that processes the event and
 *          makes necessary changes to the state of the bus. If any handler deems it necessary to change the view
 *          the bus will call render on every handler after the the event buffer is cleared
 */
public class EventBus<T extends BusState> {

    private Queue<Event> eventBuffer;
    private List<EventHandler<T>> handlers;
    private T state;
    private Activity activity;
    private boolean dispatchingEvent;
    private boolean displayNeedsUpdating;

    /**
     * Constructor for event bus
     *
     * @param initialState The initial state of the event bus
     * @param activity     The active activity
     */
    public EventBus(T initialState, Activity activity) {
        this.eventBuffer = new LinkedList<>();
        this.handlers = new ArrayList<>();
        this.state = initialState;
        this.activity = activity;
        this.dispatchingEvent = false;
        this.displayNeedsUpdating = false;
    }

    /**
     * Registers a handler to the event bus
     *
     * @param handler Handler to be added to the bus
     * @return A boolean that denotes whether the the handler was successfully added
     */
    public boolean registerHandler(EventHandler<T> handler) {
        for (EventHandler<T> eventHandler : handlers) {
            if (eventHandler.getId().equals(handler.getId()))
                return false;
        }
        this.handlers.add(handler);
        return true;
    }

    /**
     * Removes a given handler from the event bus
     *
     * @param handlerId The id of the handler to be removed
     */
    public void removeHandler(String handlerId) {
        for (EventHandler handler : handlers) {
            if (handler.getId().equals(handlerId)) {
                this.handlers.remove(handler);
                return;
            }
        }
    }

    /**
     * Feeds an event to the bus, if the bus is already dispatching an event to its handlers,
     * the event will get stored in a queue to be dealt with after the bus deals with current event
     * Once the bus is finished with dispatching events it checks whether there was a request to render
     * if so then the bus calls render method on every single of the handlers
     *
     * @param event The incoming event to the bus
     */
    public void feedEvent(Event event) {
        if (dispatchingEvent) {
            eventBuffer.offer(event);
        } else {
            dispatchingEvent = true;
            performEvent(event);
            while (!eventBuffer.isEmpty()) {
                performEvent(eventBuffer.remove());
            }
            if (displayNeedsUpdating) {
                for (EventHandler<T> handler : handlers) {
                    handler.updateView(state, activity);
                }
                displayNeedsUpdating = false;
            }
            dispatchingEvent = false;

        }
    }

    /**
     * Dispatches an event to the handler
     *
     * @param event event to be dispatched
     */
    private void performEvent(Event event) {
        for (EventHandler<T> handler : handlers) {
            Tuple2<T, Boolean> result = handler.handleEvent(event, state, this);

            if (!displayNeedsUpdating) displayNeedsUpdating = result.getY();
            state = result.getX();
        }
    }

}
