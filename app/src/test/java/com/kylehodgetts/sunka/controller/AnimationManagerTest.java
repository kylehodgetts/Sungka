package com.kylehodgetts.sunka.controller;

import com.kylehodgetts.sunka.BoardActivity;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.event.ShellMovement;
import com.kylehodgetts.sunka.event.ShellMovementToPot;
import com.kylehodgetts.sunka.event.ShellSteal;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.Player;
import com.kylehodgetts.sunka.util.Tuple2;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Kyle Hodgetts
 * @author Charlie Baker
 * @version 1.0
 * Responsible for testing the <code>AnimationManager</code> class
 */
public class AnimationManagerTest extends TestCase {

    private EventBus bus;
    private GameState gameState;
    private AnimationManager animationManager;
    private BoardActivity boardActivity;

    /**
     * Initial set up for tests
     * @throws Exception
     */
    @BeforeClass
    @Override
    public void setUp() throws Exception {
        super.setUp();
        gameState = new GameState(new Board(), new Player(), new Player());
        boardActivity = new BoardActivity();
        bus = new EventBus(gameState, boardActivity);
        animationManager = new AnimationManager(bus, boardActivity);

    }

    /**
     * After set up, assert that all components are non null
     * @throws Exception
     */
    @Test
    public void testPreConditions() throws Exception {
        assertNotNull(gameState);
        assertNotNull(boardActivity);
        assertNotNull(bus);
        assertNotNull(animationManager);
    }

    /**
     * Test that a correct tuple is returned when handling a shell movement event
     * @throws Exception
     */
    @Test
    public void testHandleEvent_shellMovement() throws Exception {
        Tuple2 tuple2 = animationManager.handleEvent(new ShellMovement(0, 0, 2, 0), gameState);
        assertNotNull(tuple2);
        assertEquals(tuple2.getX(), gameState);
        assertFalse((boolean)tuple2.getY());
    }

    /**
     * Test that a correct tuple is returned when handling a shell movement to pot event
     * @throws Exception
     */
    @Test
    public void testHandleEvent_shellMovementToPot() throws Exception {
        Tuple2 tuple2 = animationManager.handleEvent(new ShellMovementToPot(0, 2), gameState);
        assertNotNull(tuple2);
        assertEquals(tuple2.getX(), gameState);
        assertFalse((boolean)tuple2.getY());
    }

    /**
     * Test that a correct tuple is returned when handling a shell steal event
     * @throws Exception
     */
    @Test
    public void testHandleEvent_shellSteal() throws Exception {
        Tuple2 tuple2 = animationManager.handleEvent(new ShellSteal(0, 1, 1), gameState);
        assertNotNull(tuple2);
        assertEquals(tuple2.getX(), gameState);
        assertFalse((boolean)tuple2.getY());
    }
}
