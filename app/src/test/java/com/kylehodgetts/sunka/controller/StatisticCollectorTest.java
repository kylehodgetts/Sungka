package com.kylehodgetts.sunka.controller;

import android.app.Activity;
import android.content.Context;
import android.test.mock.MockContext;

import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.event.EndGame;
import com.kylehodgetts.sunka.event.NewGame;
import com.kylehodgetts.sunka.event.PlayerChoseTray;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.Player;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static com.kylehodgetts.sunka.MainActivity.AVG_TIME;
import static com.kylehodgetts.sunka.MainActivity.GAMES_LOST;
import static com.kylehodgetts.sunka.MainActivity.GAMES_WON;
import static com.kylehodgetts.sunka.MainActivity.MAX_SCORE;
import static com.kylehodgetts.sunka.MainActivity.STATS_LOCAL;


/**
 * Test class to test the collection of statistics
 *
 * @author Adam Chlupacek
 * @version 1.0
 */
public class StatisticCollectorTest extends TestCase {

    private EventBus<GameState> bus;
    private Context context;

    /**
     * Setup method to create a new game state, new players and a new EventBus to handle the events and
     * changes to the game state. Then registers the statistics collector with it.
     *
     * @throws Exception
     */
    @Before
    protected void setUp() throws Exception {
        super.setUp();

        context = new MockContext();
        new File(context.getFilesDir(), STATS_LOCAL).delete();
        bus = new EventBus<>(new GameState(new Board(), new Player(), new Player()),null);
        bus.registerHandler(new BoardControllerTest.ManagerTest(bus));
        bus.registerHandler(new StatisticsCollector(context,null));
    }

    /**
     * Tests whether the Collector collects and saves correct statistics after one turn
     *
     * @throws Exception
     */
    @Test
    public void testCollectStatistics() throws Exception {
        try {
            bus.feedEvent(new NewGame());
            Thread.sleep(1000);
            bus.feedEvent(new PlayerChoseTray(0, 6));
            Thread.sleep(10000);
            bus.feedEvent(new EndGame());

            File file = new File(context.getFilesDir(), STATS_LOCAL);
            FileInputStream inp = new FileInputStream(file);
            byte[] bytes = new byte[(int) inp.getChannel().size()];
            inp.read(bytes);
            inp.close();
            JSONObject obj = new JSONObject(new String(bytes));

            assertEquals(1,obj.getInt(MAX_SCORE));
            assertEquals(1,obj.getInt(GAMES_WON));
            assertEquals(0,obj.getInt(GAMES_LOST));
            assertEquals(1,obj.getInt(AVG_TIME)/1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
