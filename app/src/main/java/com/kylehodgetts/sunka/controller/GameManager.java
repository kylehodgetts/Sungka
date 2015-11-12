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
 * @author Adam Chlupacek, Jonathan Burton
 * @version 3.0
 *          The controller for the main game logic
 */
public class GameManager extends EventHandler<GameState> {

    private static final int DELAY = 300;
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
        } else if (event instanceof NextTurn) {
            if (!state.isRaceState()) state.setDoingMove(false);
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

    /**
     * Processes the player move, and starts the moving of the pebbles
     *
     * @param trayChosen the location of selected tray
     * @return new gameState
     */
    private GameState playerSelectedTrayEvent(GameState state, PlayerChoseTray trayChosen) {
        int trayIndexOfTrayChosen = trayChosen.getTrayIndex();
        int playerIndexOfTrayChosen = trayChosen.getPlayerIndex();
        int shells = state.getBoard().getTray(playerIndexOfTrayChosen, trayIndexOfTrayChosen);


        if (shells != 0) { //because we don't want to register a move if they have nothing to move

            //   normal turn                                                                       || raceState
            if ((state.getCurrentPlayerIndex() == playerIndexOfTrayChosen && !state.isDoingMove()) || !state.playerHasMoved(playerIndexOfTrayChosen)) {

                state.setDoingMove(true);
                state.setPlayerHasMoved(playerIndexOfTrayChosen);

                shells = state.getBoard().emptyTray(playerIndexOfTrayChosen, trayIndexOfTrayChosen);
                moveShellsToNextPosition(trayIndexOfTrayChosen, playerIndexOfTrayChosen, shells, playerIndexOfTrayChosen);
            }
        }
        return state;
    }

    /**
     * Processes one of the move tick of the players move
     * Moves the beads by one tray/store
     *
     * @return new GameState
     */
    private GameState placeShellEvent(GameState state, ShellMovement event) {

        int trayIndex = event.getTrayIndex();
        int playerIndex = event.getPlayerIndex();
        int shellsLeft = event.getShellsStillToBePlaced();
        int playerWhoTurnItIs = event.getPlayer();

        state.getBoard().incrementTray(playerIndex, trayIndex);
        shellsLeft--;

        if (shellsLeft > 0) {
            moveShellsToNextPosition(trayIndex, playerIndex, shellsLeft, playerWhoTurnItIs);
            return state;
        } else {

            //capture rule
            Board board = state.getBoard();
            if (board.getTray(playerIndex, trayIndex) == 1 && playerIndex == playerWhoTurnItIs) {
                int otherPlayer = (playerIndex + 1) % 2;
                int shellsFromOtherSide = board.emptyTray(otherPlayer, 6 - trayIndex);
                board.emptyTray(playerIndex, trayIndex);
                state.getCurrentPlayer().addToPot(shellsFromOtherSide + 1);
            }

            return endTurn(state, false, playerWhoTurnItIs);
        }

    }

    /**
     * Processes placing a shell into the next tray
     *
     * @param state current gameState
     * @param event the ShellMovement event
     * @return the updated gameState
     */
    private GameState placeShellInPotEvent(GameState state, ShellMovementToPot event) {

        int playerWhoTurnItIs = event.getPlayerIndexOfThisPot();
        Player p = (playerWhoTurnItIs == 0) ? state.getPlayer1() : state.getPlayer2();
        p.addToPot(1);


        int shells = event.getShells() - 1;
        if (shells > 0) {
            scheduleEvent(new HighLightTray(0, event.getNextPlayerIndex(), playerWhoTurnItIs), 0);
            scheduleEvent(new ShellMovement(0, event.getNextPlayerIndex(), shells, false, playerWhoTurnItIs), DELAY);
            return state;

        } else return endTurn(state, true, playerWhoTurnItIs);
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
                scheduleEvent(new HighlightPlayerStore(playerWhoTurnItIs), 20);
                scheduleEvent(new ShellMovementToPot(playerWhoTurnItIs, shellsLeft), DELAY);
            } else {
                int otherPlayer = (playerIndex + 1) % 2;
                scheduleEvent(new HighLightTray(0, otherPlayer, playerWhoTurnItIs), 20);
                scheduleEvent(new ShellMovement(0, otherPlayer, shellsLeft, false, playerWhoTurnItIs), DELAY);
            }
        } else {
            scheduleEvent(new HighLightTray(trayIndex + 1, playerIndex, playerWhoTurnItIs), 50);
            scheduleEvent(new ShellMovement(trayIndex + 1, playerIndex, shellsLeft, false, playerWhoTurnItIs), DELAY);
        }
    }

    /**
     * Processes the end of the turn for current player
     *
     * @return new GameState
     */
    private GameState endTurn(GameState state, boolean playerHasAnotherTurn, int currentPlayer) {

        //is it the end game?
        Board board = state.getBoard();
        if (board.isEmptyRow(0) && board.isEmptyRow(1)) {
            if (state.getPlayer1().getStonesInPot() > state.getPlayer2().getStonesInPot()) {
                state.getPlayer1().addWonGames();
            } else {
                state.getPlayer2().addWonGames();
            }
            scheduleEvent(new EndGame(), 0);
            return state;
        }

        if (state.isRaceState()) {
            state.setPlayerFirstMoveOver(currentPlayer);
            if (state.isFirstMoveOverForPlayer(0) && state.isFirstMoveOverForPlayer(1)) {
                state.setRaceStateOver();
            }
        }

        //if not in race to start, they don't have another turn and the other side has shells then the other player can go
        if (!state.isRaceState() && !playerHasAnotherTurn && !board.isEmptyRow((currentPlayer + 1) % 2)) {
            state.switchCurrentPlayerIndex();
        }

        //if race state if over but we haven't chosen the next players turn yet,
        //then we need to specify it before the NextTurn event is scheduled
        if (!state.isRaceState() && state.getCurrentPlayerIndex() == -1) {
            state.setCurrentPlayerIndex(state.playerWhoWentFirst());
        }

        scheduleEvent(new NextTurn(), 0);

        return state;
    }

    //TODO potentially move this into the bus class for less code replication

    /**
     * Schedules an event to the timer for later dispatching
     *
     * @param event  the event to be dispatched to the bus
     * @param millis the amount in millis by which the execution is delayed
     */
    private void scheduleEvent(final Event event, long millis) {
        if (millis == 0) bus.feedEvent(event);
        else {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    bus.feedEvent(event);
                }
            }, millis);
        }
    }


}
