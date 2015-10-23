package com.kylehodgetts.sunka.controller;

import android.app.Activity;
import android.os.Handler;

import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.EndGame;
import com.kylehodgetts.sunka.event.NextTurn;
import com.kylehodgetts.sunka.event.PlayerMove;
import com.kylehodgetts.sunka.event.TickDistribution;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.util.Tuple2;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *
 * The controller for the main game logic
 */
public class GameManagerV2 extends EventHandler<GameState> {

    private Timer timer;

    /**
     * Default constructor for event handler, assigns its id that should be unique
     */
    public GameManagerV2() {
        super("GameManager");
        new Thread(new Runnable() {
            @Override
            public void run() {
                timer = new Timer("GameManager");
            }
        });

    }

    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, GameState state, EventBus<GameState> bus) {

        if (event instanceof PlayerMove) return new Tuple2<>(new BusContext(state,bus).event((PlayerMove) event),true);
        if (event instanceof TickDistribution) return new Tuple2<>(new BusContext(state,bus).event((TickDistribution) event),true);


        return new Tuple2<>(state,false);
    }

    @Override
    public void render(GameState state, Activity activity) {
            //todo hook with gui
    }

    /**
     * Convenience class to wrap the current state and bus together
     */
    private class BusContext{
        private GameState state;
        private EventBus<GameState> bus;

        public BusContext(GameState state, EventBus<GameState> bus) {
            this.state = state;
            this.bus = bus;
        }

        /**
         * Processes the player move, and starts the moving of the pebbles
         * @param selected the location of selected tray
         * @return
         */
        public GameState event(PlayerMove selected){
            int column = selected.getX();
            int row = selected.getY();

            schedule(new TickDistribution(column == 6 ? 0 : column + 1, column == 6 ? (row + 1) % 2 : row, state.getBoard().emptyTray(row,column), true), 300);

            state.getBoard().emptyTray(row,column);
            return state;
        }

        /**
         * Processes one of the move tick of the players move
         * @param tick the current sate of ticking
         * @return
         */
        public GameState event(TickDistribution tick){
            return move(tick.getX(),tick.getY(),tick.getLeft(), tick.isFirst());
        }

        /**
         * Moves the beads by one tray/store
         * @param x         the current column where we add the bead
         * @param y         the current row where we add the bead
         * @param amt       the amount of beads to move
         * @param first     whether this move is the first one
         * @return
         */
        private GameState move(int x, int y, int amt, boolean first){
            boolean repeatTurn = false;

            if (first && x == 0){ //TODO disgusting hack for fixing of the moving straight into the store
                state.getCurrentPlayer().addToPot(1);
                amt--;
                repeatTurn = true;
                if (amt>0){
                    state.getBoard().incrementTray(y, x);
                    amt--;
                    repeatTurn = false;
                }
            }else {
                state.getBoard().incrementTray(y, x);
                System.out.println("Board");
                System.out.println(state.getBoard());
                amt--;
                if (x == 6 && state.currentPlayerRow() == y && amt > 0) {
                    state.getCurrentPlayer().addToPot(1);
                    amt--;
                    repeatTurn = true;
                }
            }

            if(amt == 0){
                return endTurn(x, y, repeatTurn);
            } else {
                schedule(new TickDistribution(x == 6 ? 0 : x + 1, x == 6 ? (y + 1) % 2 : y, amt, false), 300);
                return state;
            }
        }

        /**
         * Processes the end of the turn for current player
         * @param x         the column of the last tray we putted beads into
         * @param y         the row of the last tray we putted beads into
         * @param repeat    whether the last bead ended in the store
         * @return
         */
        private GameState endTurn(int x, int y, boolean repeat){
            Board b = state.getBoard();
            if(!repeat){
                if (b.getTray(y,x)==1 && y == state.currentPlayerRow()){
                    int oppositeTray = b.emptyTray((y+1)%2,6-x);
                    state.getCurrentPlayer().addToPot(oppositeTray+1);
                    b.emptyTray(y,x);
                }
                state.setPlayerOneTurn(!state.isPlayerOneTurn());
            }
            if (b.isEmptyRow(0) && b.isEmptyRow(1)){
                bus.feedEvent(new EndGame());
                return state;
            }

            if(b.isEmptyRow(state.currentPlayerRow()))
                state.setPlayerOneTurn(!state.isPlayerOneTurn());

            schedule(new NextTurn(),300);
            return state;
        }


        /**
         * Schedules an event to the timer for later dispatching
         * @param event     the event to be dispatched to the bus
         * @param millis    the amount in millis by which the execution is delayed
         */
        private void schedule(final Event event, long millis){
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    bus.feedEvent(event);
                }
            }, millis);


        }
    }

}
