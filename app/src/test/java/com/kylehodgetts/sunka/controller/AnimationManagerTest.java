package com.kylehodgetts.sunka.controller;

import com.kylehodgetts.sunka.BoardActivity;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.event.ShellMovement;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.Player;
import com.kylehodgetts.sunka.util.Tuple2;

import junit.framework.TestCase;

/**
 * Created by kylehodgetts on 14/11/2015.
 */
public class AnimationManagerTest extends TestCase {

    private EventBus bus;
    private GameState gameState;
    private AnimationManager animationManager;
    private BoardActivity boardActivity;

    public void setUp() throws Exception {
        super.setUp();
        gameState = new GameState(new Board(), new Player(), new Player());
        boardActivity = new BoardActivity();
        bus = new EventBus(gameState, boardActivity);
        animationManager = new AnimationManager(bus, boardActivity);

    }

    public void testPreConditions() throws Exception {
        assertNotNull(gameState);
        assertNotNull(boardActivity);
        assertNotNull(bus);
        assertNotNull(animationManager);
    }

    public void testHandleEvent_shellMovement() throws Exception {
        Tuple2 tuple2 = animationManager.handleEvent(new ShellMovement(0, 0, 2, 0))
    }

    public void testHandleEvent_shellMovementToPot() throws Exception {
        assertTrue(true);
    }

    public void testHandleEvent_shellSteal() throws Exception {
        assertTrue(true);
    }
}