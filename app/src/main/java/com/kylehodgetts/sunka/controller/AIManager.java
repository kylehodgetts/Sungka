package com.kylehodgetts.sunka.controller;

import android.app.Activity;
import android.util.Log;

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

public class AIManager extends EventHandler<GameState> {
    //At this point it only evaluates the best move for the first round
    private static final String TAG = "SUNGKAAITAG";

    private final int myIndex = 1;
    private final int TIMERDELAY = 1000; //delay before AI makes a move
    private EventBus<GameState> bus;
    private Timer timer;

    private AI ai;

    public AIManager(EventBus<GameState> bus) {
        super("ai");
        this.bus = bus;
        timer = new Timer("aiTimer");
        ai = new AIStrategy();
    }

    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, final GameState state) {

        //normal turn
        if (event instanceof NextTurn && state.getCurrentPlayerIndex() == myIndex) {
            Log.i(TAG, "Start turn");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    bus.feedEvent(new PlayerChoseTray(ai.chooseTray(state), myIndex));
                }
            }, TIMERDELAY);
            Log.i(TAG, "End turn");
            Log.i(TAG, " ");


            //race to finish first turn
        } else if (event instanceof NewGame && state.getCurrentPlayerIndex() < 1) {
            Log.i(TAG, "Start turn");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    bus.feedEvent(new PlayerChoseTray(ai.chooseTray(state), myIndex));
                }
            }, TIMERDELAY);
            Log.i(TAG, "End turn");
            Log.i(TAG, " ");
        }

        return new Tuple2<>(state, false);
    }

    @Override
    public void updateView(GameState state, Activity activity) {

    }





}
