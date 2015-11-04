package com.kylehodgetts.sunka.controller;

import android.app.Activity;
import android.widget.Button;

import com.kylehodgetts.sunka.R;
import com.kylehodgetts.sunka.TrayOnClick;
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
 * @author Adam Chlupacek
 * @version 1.0
 *          <p/>
 *          The controller for the main game logic
 */
public class GameManager extends EventHandler<GameState> {

    private Timer timer;
    private EventBus<GameState> bus;
    private int delay = 300;

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

    /**
     * Renders any changes made to the model and the game state on to the GUI on the UI Thread.
     *
     * @param state    The current state of the event bus
     * @param activity The active activity
     */
    @Override
    public void updateView(final GameState state, final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button playerOneStore = (Button) activity.findViewById(R.id.buttonbs);
                playerOneStore.setText(Integer.toString(state.getPlayer1().getStonesInPot()));

                Button playerTwoStore = (Button) activity.findViewById(R.id.buttonas);
                playerTwoStore.setText(Integer.toString(state.getPlayer2().getStonesInPot()));

                Board currentBoard = state.getBoard();
                for (int row = 0; row < 2; ++row) {
                    for (int column = 0; column < 7; ++column) {
                        Button button = (Button) activity.findViewById(Integer.parseInt(row + "" + column));
                        button.setText(Integer.toString(currentBoard.getTray(row, column)));
                        if (
                                !state.getBoard().isEmptyTray(row, column)
                                        && (
                                        (state.getCurrentPlayerIndex() == row && !state.isDoingMove() && !state.isInitialising())
                                                || state.playerInitialising(row)
                                )
                                ) {
                            button.setOnClickListener(new TrayOnClick(column, row, bus));
                        } else {
                            button.setOnClickListener(null);
                        }
                    }
                }
            }
        });
    }

    /**
     * Processes the player move, and starts the moving of the pebbles
     *
     * @param trayChosen the location of selected tray
     * @return
     */
    private GameState playerSelectedTrayEvent(GameState state, PlayerChoseTray trayChosen) {
        int trayIndex = trayChosen.getTrayIndex();
        int playerIndex = trayChosen.getPlayerIndex();

        scheduleEvent(new ShellMovement(trayIndex == 6 ? 0 : trayIndex + 1, trayIndex == 6 ? (playerIndex + 1) % 2 : playerIndex, state.getBoard().emptyTray(playerIndex, trayIndex), true, trayChosen.getPlayerIndex()), delay, trayChosen.getPlayerIndex());
        if (state.getCurrentPlayerIndex() == -1) {
            state.setCurrentPlayerIndex(trayChosen.getPlayerIndex());
        }
        //System.out.println("Player " + state.getCurrentPlayerIndex()); //why not Log.v()?
        if (state.isInitialising())
            state.nextInitPhase(trayChosen.getPlayerIndex());
        state.getBoard().emptyTray(playerIndex, trayIndex);
        state.setDoingMove(true);
        return state;
    }

    /**
     * Processes one of the move tick of the players move
     * Moves the beads by one tray/store
     *
     * @return
     */
    private GameState placeShellEvent(GameState state, ShellMovement move) {

        int trayIndex = move.getTrayIndex();
        int playerIndex = move.getPlayerIndex();
        int shellsLeft = move.getShellsStillToBePlaced();
        boolean first = move.isFirstShellMoved();
        int player = move.getPlayer();
        boolean repeatTurn = false;

        //we go past the pot and then have to go back, which is why the animation 'skips' when adding to the pots
        if (first && trayIndex == 0) { //TODO disgusting hack for fixing of the moving straight into the store
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

    /**
     * Processes the end of the turn for current player
     *
     * @param trayIndex   the column of the last tray we putted beads into
     * @param playerIndex the row of the last tray we putted beads into
     * @param repeat      whether the last bead ended in the store
     * @return
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
