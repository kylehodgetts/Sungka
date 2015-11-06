package com.kylehodgetts.sunka.controller;

import android.app.Activity;
import android.graphics.Color;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kylehodgetts.sunka.R;
import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.EndGame;
import com.kylehodgetts.sunka.event.NewGame;
import com.kylehodgetts.sunka.event.NextTurn;
import com.kylehodgetts.sunka.event.PlayerChoseTray;
import com.kylehodgetts.sunka.event.ShellMovement;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.util.Tuple2;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Adam Chlupacek. V2 By Jonathan Burton
 * @version 2.0
 *          The controller for the main game logic
 */
public class GameManager extends EventHandler<GameState> {

    private static final int delay = 300;
    private Timer timer;
    private EventBus<GameState> bus;

    /**
     * Default constructor for event handler, assigns its id that should be unique
     */
    public GameManager(EventBus<GameState> bus) {
        super("GameManager");
        timer = new Timer("GameManager");
        this.bus = bus;
    }

    /**
     * A method that processes events that were send to a event bus
     *
     * @param event The incoming event from the bus to this handler
     * @param state The current state of the bus
     * @return Returns the modified state together with a flag whether a render is required
     */
    @Override
    public Tuple2<GameState, Boolean> handleEvent(Event event, GameState state) {


        if (event instanceof PlayerChoseTray) {
            return new Tuple2<>(playerSelectedTrayEvent(state, (PlayerChoseTray) event), true);
        } else if (event instanceof ShellMovement)
            return new Tuple2<>(placeShellEvent(state, (ShellMovement) event), true);
        else if (event instanceof NextTurn) {
            if (((NextTurn) event).finishInit()) {
                state.finishInit();
            }
            state.setDoingMove(false);
            return new Tuple2<>(state, true);
        } else if (event instanceof NewGame) {
            return new Tuple2<>(state, true); //TODO Should set the board to initial state
        } else return new Tuple2<>(state, false);
    }

    @Override
    public void updateView(GameState state, Activity activity) { }


    //TODO rename this to remove the 'event' bit
    /**
     * Processes the player move, and starts the moving of the pebbles
     *
     * @param trayChosen the location of selected tray
     * @return new gameState
     */
    private GameState playerSelectedTrayEvent(GameState state, PlayerChoseTray trayChosen) {
        int trayIndex = trayChosen.getTrayIndex();
        int playerIndex = trayChosen.getPlayerIndex();
        int shells = state.getBoard().getTray(playerIndex, trayIndex);

        //horrific thing that determines if this move is valid, or just a player pressing random buttons
        if (shells != 0 && ((state.getCurrentPlayerIndex() == playerIndex && !state.isDoingMove() && !state.isInitialising()) || state.playerInitialising(playerIndex))) {
            state.setDoingMove(true);
            if (state.getCurrentPlayerIndex() == -1)
                state.setCurrentPlayerIndex(trayChosen.getPlayerIndex());
            if (state.isInitialising()) state.nextInitPhase(trayChosen.getPlayerIndex());
            scheduleEvent(new ShellMovement(trayIndex == 6 ? 0 : trayIndex + 1, trayIndex == 6 ? (playerIndex + 1) % 2 : playerIndex, state.getBoard().emptyTray(playerIndex, trayIndex), true, trayChosen.getPlayerIndex()), delay, trayChosen.getPlayerIndex());

        
        }
        return state;
    }

    //TODO rename this to remove the 'event' bit
    /**
     * Processes one of the move tick of the players move
     * Moves the beads by one tray/store
     *
     * @return new GameState
     */
    private GameState placeShellEvent(GameState state, ShellMovement move) {

        int trayIndex = move.getTrayIndex();
        int playerIndex = move.getPlayerIndex();
        int shellsLeft = move.getShellsStillToBePlaced();
        boolean first = move.isFirstShellMoved();
        int player = move.getPlayer();
        boolean repeatTurn = false;

        //we go past the pot and then have to go back, which is why the animation 'skips' when adding to the pots
        if (first && trayIndex == 0) {
            //TODO fix disgusting hack for fixing of the moving straight into the store. Schedule same event, but with one less shell?
            state.getPlayerFor(player).addToPot(1);
            shellsLeft--;
            repeatTurn = true;
            if (shellsLeft > 0) {
                state.getBoard().incrementTray(playerIndex, trayIndex);
                shellsLeft--;
                repeatTurn = false;
            }

        } else {
            state.getBoard().incrementTray(playerIndex, trayIndex);
            shellsLeft--;
            if (trayIndex == 6 && player == playerIndex && shellsLeft > 0) {
                state.getPlayerFor(player).addToPot(1);
                shellsLeft--;
                repeatTurn = true;
            }
        }

        if (shellsLeft == 0) {
            return endTurn(state, trayIndex, playerIndex, repeatTurn, player);
        } else {
            scheduleEvent(new ShellMovement(trayIndex == 6 ? 0 : trayIndex + 1, trayIndex == 6 ? (playerIndex + 1) % 2 : playerIndex, shellsLeft, false, player), delay, player);
            return state;
        }
    }

    //TODO rename this so all individual event handler methods have a consistent naming scheme
    /**
     * Processes the end of the turn for current player
     *
     * @param trayIndex   the column of the last tray we putted beads into
     * @param playerIndex the row of the last tray we putted beads into
     * @param repeat      whether the last bead ended in the store
     * @return new GameState
     */
    private GameState endTurn(GameState state, int trayIndex, int playerIndex, boolean repeat, int player) {
        Board b = state.getBoard();
        if (!repeat) {
            if (b.getTray(playerIndex, trayIndex) == 1 && playerIndex == player) {
                int oppositeTray = b.emptyTray((playerIndex + 1) % 2, 6 - trayIndex);
                state.getPlayerFor(player).addToPot(oppositeTray + 1);
                b.emptyTray(playerIndex, trayIndex);
            }
            if (!state.isInitialising()) {
                state.setCurrentPlayerIndex((state.getCurrentPlayerIndex() + 1) % 2);
            }
        }
        if (b.isEmptyRow(0) && b.isEmptyRow(1)) {
            bus.feedEvent(new EndGame());
            return state;
        }

        if (!state.isInitialising() && b.isEmptyRow(state.currentPlayerRow()))
            state.setCurrentPlayerIndex((state.getCurrentPlayerIndex() + 1) % 2);

        scheduleEvent(new NextTurn(state.isInitialising() && state.getCurrentPlayerIndex() != player), delay, player);
        return state;
    }


    //TODO potentially move this into the bus class for less code replication
    /**
     * Schedules an event to the timer for later dispatching
     *
     * @param event  the event to be dispatched to the bus
     * @param millis the amount in millis by which the execution is delayed
     */
    private void scheduleEvent(final Event event, long millis, int player) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                bus.feedEvent(event);
            }
        }, millis);
    }

}
