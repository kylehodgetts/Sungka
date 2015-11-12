package com.kylehodgetts.sunka.controller;

import android.app.Activity;
import android.test.suitebuilder.annotation.SmallTest;

import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.event.PlayerChoseTray;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.Player;
import com.kylehodgetts.sunka.util.Tuple2;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Responsible for testing the Online Game Manager class.
 */
public class OnlineGameManagerTest extends TestCase {
    private OnlineGameManager onlineGameManager;
    private GameState gameState;
    private EventBus<GameState> bus;

    /**
     * Initialises the game state, bus and online game manager
     * @throws Exception
     */
    @BeforeClass
    @Override
    public void setUp() throws Exception {
        super.setUp();
        gameState = new GameState(new Board(), new Player(), new Player());
        bus = new EventBus<>(gameState, new Activity());
        onlineGameManager = new OnlineGameManager(bus);

    }

    /**
     * Assert that the player chose tray event is handled and returned properly
     * @throws Exception
     */
    @Test
    public void testHandleEvent() throws Exception {
        Tuple2 expected = new Tuple2(gameState, true);
        Tuple2 actual = onlineGameManager.handleEvent(new PlayerChoseTray(1, 1), gameState);
        assertEquals(expected.getX(), actual.getX());
    }
}
