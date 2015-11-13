package com.kylehodgetts.sunka.controller;

import android.app.Activity;

import com.kylehodgetts.sunka.controller.AI.AI;
import com.kylehodgetts.sunka.controller.AI.AIStrategy;
import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.NewGame;
import com.kylehodgetts.sunka.event.NextTurn;
import com.kylehodgetts.sunka.event.PlayerChoseTray;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.util.Tuple2;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Jonathan
 * @version 0.1
 */
public class AIManager extends EventHandler<GameState> {

    private final int AI_DELAY = 1000; //delay before AI makes a move //TODO make this change on user request
    private EventBus<GameState> bus;
    private Timer timer;

    private AI ai;

    /**
     * Default constructor for event handler, assigns its id that should be unique
     *
     * @param bus the event bus that this should feed events into
     */
    public AIManager(EventBus<GameState> bus) {
        super("ai");
        this.bus = bus;
        timer = new Timer("aiTimer");

        ai = new AIStrategy(); //TODO change this based on a difficulty setting once we have multiple AI
    }

    /**
     * A method that processes events that were send to a event bus
     *
     * @param event The incoming event from the bus to this handler
     * @param state The current state of the bus
     * @return Returns the modified state together with a flag whether a render is required
     */
    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, final GameState state) {

        //   normal turn                                                            || race to finish first turn
        if ((event instanceof NextTurn && state.getCurrentPlayerIndex() == AI.PLAYER_AI) || (event instanceof NewGame && state.getCurrentPlayerIndex() < 1)) {
            final PlayerChoseTray aiMove = new PlayerChoseTray(ai.chooseTray(state), AI.PLAYER_AI);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //Log.i(AI.TAG, "AI feeding delayed event");
                    bus.feedEvent(aiMove);
                }
            }, AI_DELAY);
            //Log.i(AI.TAG, "AI scheduled an event at tray position " + aiMove.getTrayIndex());

        }

        return new Tuple2<>(state, false);
    }

    @Override
    public void updateView(GameState state, Activity activity) {

    }
}
