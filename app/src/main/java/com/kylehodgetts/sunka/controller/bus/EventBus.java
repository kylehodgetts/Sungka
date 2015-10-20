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
 *
 * A stateful event bus.
 *
 * A messaging system for components of the application. Every single component/controller can be
 * represented as an event handler. The event handler then listens on any event
 * that is being send to the bus. The event is send to every handler that processes the event and
 * makes necessary changes to the state of the bus. If any handler deems it necessary to change the view
 * the bus will call render on every handler after the the event buffer is cleared
 */
public class EventBus<T extends BusState> {

    private Queue<Event> eventBuffer;
    private List<EventHandler<T>> handlers;
    private T state;
    private Activity activity;
    private boolean dispatching;
    private boolean render;

    /**
     * Constructor for event bus
     * @param initialState  The initial state of the event bus
     * @param activity      The active activity
     */
    public EventBus(T initialState, Activity activity){
        this.eventBuffer = new LinkedList<>();
        this.handlers = new ArrayList<>();
        this.state = initialState;
        this.activity = activity;
        this.dispatching = false;
        this.render = false;
    }

    /**
     * Registers a handler to the event bus
     * @param handler   Handler to be added to the bus
     * @return          A boolean that denotes whether the the handler was successfully added
     */
    public boolean registerHandler(EventHandler<T> handler){
        for (EventHandler<T> eventHandler : handlers) {
            if(eventHandler.getId().equals(handler.getId()))
                return false;
        }
        this.handlers.add(handler);
        return true;
    }

    /**
     * Removes a given handler from the event bus
     * @param handlerId The id of the handler to be removed
     */
    public void removeHandler(String handlerId){
        for (EventHandler handler:handlers) {
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
     * @param event The incoming event to the bus
     */
    public void feedEvent(Event event){
        if (dispatching){
            eventBuffer.offer(event);
        }else {
            dispatching = true;
            handleOne(event);
            while (!eventBuffer.isEmpty()){
                handleOne(eventBuffer.remove());
            }
            if (render){
                for (EventHandler<T> handler : handlers) {
                    handler.render(state, activity);
                }
                render = false;
            }
            dispatching = false;

        }
    }

    private void handleOne(Event event){
        for (EventHandler<T> handler : handlers) {
            Tuple2<T,Boolean> result = handler.handleEvent(event,state,this);
            render = render ? render : result.getY();
            state = result.getX();
        }
    }

}
