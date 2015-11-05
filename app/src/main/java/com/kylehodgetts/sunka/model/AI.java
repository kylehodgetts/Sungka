package com.kylehodgetts.sunka.model;

import android.app.Activity;

import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.NewGame;
import com.kylehodgetts.sunka.event.NextTurn;
import com.kylehodgetts.sunka.event.PlayerChoseTray;
import com.kylehodgetts.sunka.util.Tuple2;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AI extends EventHandler<GameState> {
//At this point it only evaluates the best move for the first round

    private final int myIndex = 1;
    private EventBus<GameState> bus;
    private Timer timer;
    private final int TIMERDELAY = 1000; //delay before AI makes a move

    public AI(EventBus<GameState> bus) {
        super("ai");
        this.bus = bus;
        timer = new Timer("aiTimer");
    }

    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, final GameState state) {

        //normal turn
        if (event instanceof NextTurn && state.getCurrentPlayerIndex() == myIndex) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    bus.feedEvent(new PlayerChoseTray(getRandomTray(state), myIndex));
                }
            }, TIMERDELAY);
        //race to finish first turn
        } else if (event instanceof NewGame && state.getCurrentPlayerIndex() < 1) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    bus.feedEvent(new PlayerChoseTray(getRandomTray(state), myIndex));
                }
            }, TIMERDELAY);
        }


        return new Tuple2<>(state, false);
    }

    private int getRandomTray(GameState state) {

        Random r = new Random();
        int i = r.nextInt(7);
        Board b = state.getBoard();

        while (b.getTray(myIndex, i) == 0) {
            r.nextInt(7);
        }

        return i;
    }

    public void strategyTray(Board board, int MaxSide) {
        Board boardMinSide;
        int sevenTrays[] = new int[7];
        for (int x = 0; x < 7; x++) {
            moveChance(MaxSide, x, board);

        }

    }


    private int moveChance(int i, int j, Board board) {
        int highLow = 0;
//            moveShells(i,j,board);

        return highLow;
    }

    private Board moveShells(int i, int j, Board board) {
        //Algorithm;


        return board;
    }


    @Override
    public void updateView(GameState state, Activity activity) {

    }

}
