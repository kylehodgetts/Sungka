package com.kylehodgetts.sunka.controller;

import android.app.Activity;

import com.kylehodgetts.sunka.view.BoardActivity;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.Player;

import junit.framework.TestCase;

/**
 * Created by CBaker on 14/11/2015.
 */
public class AnimationManagerTest extends TestCase {

    private BoardActivity boardActivity;
    private AnimationManager animationManager;
    private EventBus bus;
    private GameState state;

    public void setUp() throws Exception {
        super.setUp();

        boardActivity = new BoardActivity();
        state = new GameState(new Board(), new Player(), new Player());
        bus = new EventBus<GameState>(state, new Activity());
        animationManager = new AnimationManager(bus, new Activity());
        bus.registerHandler(animationManager);

    }

    public void testHandleEvent() throws Exception {

    }
}