package com.kylehodgetts.sunka.controller;

import android.app.Activity;

import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.event.PlayerChoseTray;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.Player;
import com.kylehodgetts.sunka.util.Tuple2;

import junit.framework.TestCase;

/**
 * @author Kyle Hodgetts
 */
public class OnlineGameManagerTest extends TestCase {
    private OnlineGameManager onlineGameManager;
    private GameState gameState;
    private EventBus<GameState> bus;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        gameState = new GameState(new Board(), new Player(), new Player());
        bus = new EventBus<>(gameState, new Activity());
        onlineGameManager = new OnlineGameManager(bus);

    }

    public void testHandleEvent() throws Exception {
        Tuple2 expected = new Tuple2(gameState, true);
        Tuple2 actual = onlineGameManager.handleEvent(new PlayerChoseTray(1, 1), gameState);
        assertEquals(expected.getX(), actual.getX());
    }
}