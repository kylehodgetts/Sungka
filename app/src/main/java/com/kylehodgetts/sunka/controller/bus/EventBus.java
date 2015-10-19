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
 * <-INPUT DESC->
 */
public class EventBus<T extends BusState> {

    private Queue<Event> eventQueue;
    private List<EventHandler<T>> handlers;
    private T state;
    private Activity activity;
    private boolean dispatching;
    private boolean render;

    public EventBus(T initialState, Activity activity){
        this.eventQueue = new LinkedList<>();
        this.handlers = new ArrayList<>();
        this.state = initialState;
        this.activity = activity;
        this.dispatching = false;
        this.render = false;
    }

    public boolean registerHandler(EventHandler<T> handler){
        for (EventHandler<T> eventHandler : handlers) {
            if(eventHandler.getId().equals(handler.getId()))
                return false;
        }
        this.handlers.add(handler);
        return true;
    }

    public void removeHandler(String handlerId){
        for (EventHandler handler:handlers) {
            if (handler.getId().equals(handlerId)) {
                this.handlers.remove(handler);
                return;
            }
        }
    }

    public void feedEvent(Event event){
        if (dispatching){
            eventQueue.offer(event);
        }else {
            dispatching = true;
            handleOne(event);
            while (!eventQueue.isEmpty()){
                handleOne(eventQueue.remove());
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
