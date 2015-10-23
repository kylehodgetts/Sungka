package com.kylehodgetts.sunka.controller;

import android.test.InstrumentationTestCase;
import android.test.UiThreadTest;

import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.event.PlayerMove;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.Player;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Charlie Baker
 * @version 1.0
 */
public class BoardControllerTest extends TestCase {

    private GameManagerV2 manager;
    private GameState state;
    private EventBus<GameState> bus;
    

    @Before
    protected void setUp() throws Exception {
        super.setUp();

        manager = new GameManagerV2();
        state = new GameState(new Board(),new Player(),new Player(), true);
        bus = new EventBus<>(state,null);
        bus.registerHandler(manager);

    }

    @Test
    public void testMoveOne() throws Exception {
        bus.feedEvent(new PlayerMove(3, 0));
        System.out.println(state.getBoard());
        assertEquals(8, state.getBoard().getTray(0, 6));
        assertEquals(8, state.getBoard().getTray(0, 5));
        assertEquals(8, state.getBoard().getTray(0, 4));
        assertEquals(1, state.getPlayer1().getStonesInPot());
        assertEquals(8, state.getBoard().getTray(1, 0));
        assertEquals(8, state.getBoard().getTray(1, 1));
        assertEquals(8, state.getBoard().getTray(1, 2));
        assertEquals(7, state.getBoard().getTray(1, 3));
        assertEquals(7, state.getBoard().getTray(1, 4));
        assertEquals(7, state.getBoard().getTray(1, 5));
        assertEquals(7, state.getBoard().getTray(1, 6));
        assertEquals(0, state.getPlayer2().getStonesInPot());
        assertEquals(7, state.getBoard().getTray(0, 0));
        assertEquals(7, state.getBoard().getTray(0, 1));
        assertEquals(7, state.getBoard().getTray(0, 2));
        assertEquals(0, state.getBoard().getTray(0, 3));

    }
}