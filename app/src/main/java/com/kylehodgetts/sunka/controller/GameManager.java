package com.kylehodgetts.sunka.controller;

import android.app.Activity;

import com.kylehodgetts.sunka.controller.bus.Event;
import com.kylehodgetts.sunka.controller.bus.EventBus;
import com.kylehodgetts.sunka.controller.bus.EventHandler;
import com.kylehodgetts.sunka.event.EndGame;
import com.kylehodgetts.sunka.event.HighLightTray;
import com.kylehodgetts.sunka.event.HighlightPlayerStore;
import com.kylehodgetts.sunka.event.NewGame;
import com.kylehodgetts.sunka.event.NextTurn;
import com.kylehodgetts.sunka.event.PlayerChoseTray;
import com.kylehodgetts.sunka.event.ShellMovement;
import com.kylehodgetts.sunka.event.ShellMovementToPot;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.model.Player;
import com.kylehodgetts.sunka.util.Tuple2;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Adam Chlupacek. V2 By Jonathan Burton
 *         ,2.1 Phileas Hocquard
 * @version 2.1
 *          The controller for the main game logic
 */
public class GameManager extends EventHandler<GameState> {

    private static final int delay = 1000;
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
        } else if (event instanceof ShellMovement) {
            return new Tuple2<>(placeShellEvent(state, (ShellMovement) event), true);
        } else if (event instanceof ShellMovementToPot) {
            return new Tuple2<>(placeShellInPotEvent(state, (ShellMovementToPot) event), true);
        }
        else if (event instanceof NextTurn) {
            if (((NextTurn) event).finishInit()) {
                state.finishInit();
            }
            state.setDoingMove(false);
            return new Tuple2<>(state, true);
        } else if (event instanceof NewGame) {
            state.getPlayer1().resetStonesInPot();
            state.getPlayer2().resetStonesInPot();
            state = new GameState(new Board(), state.getPlayer1(), state.getPlayer2());
            return new Tuple2<>(state, true);
        } else return new Tuple2<>(state, false);
    }

    @Override
    public void updateView(GameState state, Activity activity) {
    }


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

            shells = state.getBoard().emptyTray(playerIndex, trayIndex);
            moveShellsToNextPosition(trayIndex, playerIndex, shells, playerIndex);

        }
        return state;
    }

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
        int playerWhoTurnItIs = move.getPlayer();

        state.getBoard().incrementTray(playerIndex, trayIndex);
        shellsLeft--;

        if (shellsLeft > 0) {
            moveShellsToNextPosition(trayIndex, playerIndex, shellsLeft, playerWhoTurnItIs);
            return state;
        } else {
            return endTurn(state, trayIndex, playerIndex, false, playerWhoTurnItIs);
        }

    }

    private GameState placeShellInPotEvent(GameState state, ShellMovementToPot event) {

        int p = event.getPlayerIndexOfThisPot();
        Player player = (p == 0) ? state.getPlayer1() : state.getPlayer2();
        player.addToPot(1);


        scheduleEvent(new HighLightTray(0, event.getNextPlayerIndex(), event.getPlayerIndexOfThisPot()), 0, event.getPlayerIndexOfThisPot());
        scheduleEvent(new ShellMovement(0, event.getNextPlayerIndex(), event.getShells() - 1, false, event.getPlayerIndexOfThisPot()), delay, event.getPlayerIndexOfThisPot());

        return state;
    }


    /**
     * Figures out where we should move next, and schedules all relevant events
     *
     * @param trayIndex         the board index of the current tray
     * @param playerIndex       the board index of current player
     * @param shellsLeft        the number of shells left to palce
     * @param playerWhoTurnItIs the player index of who's turn it is
     */
    private void moveShellsToNextPosition(int trayIndex, int playerIndex, int shellsLeft, int playerWhoTurnItIs) {

        if (trayIndex == 6) {
            if (playerWhoTurnItIs == playerIndex) {
                scheduleEvent(new HighlightPlayerStore(playerWhoTurnItIs), 0, playerWhoTurnItIs);
                scheduleEvent(new ShellMovementToPot(playerWhoTurnItIs, shellsLeft), delay, playerWhoTurnItIs);
            } else {
                int otherPlayer = (playerIndex + 1) % 2;
                scheduleEvent(new HighLightTray(0, otherPlayer, playerWhoTurnItIs), 0, playerWhoTurnItIs);
                scheduleEvent(new ShellMovement(0, otherPlayer, shellsLeft, false, playerWhoTurnItIs), delay, playerWhoTurnItIs);
            }
        } else {
            scheduleEvent(new HighLightTray(trayIndex + 1, playerIndex, playerWhoTurnItIs), 0, playerWhoTurnItIs);
            scheduleEvent(new ShellMovement(trayIndex + 1, playerIndex, shellsLeft, false, playerWhoTurnItIs), delay, playerWhoTurnItIs);
        }
    }


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
            if (state.getPlayer1().getStonesInPot() > state.getPlayer2().getStonesInPot()) {
                state.getPlayer1().addWonGames();
            } else {
                state.getPlayer2().addWonGames();
            }
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
