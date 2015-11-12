package com.kylehodgetts.sunka.model;

import com.kylehodgetts.sunka.controller.bus.BusState;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          The current state of the game, contains all of the models for the game
 */
public class GameState implements BusState {

    private Board board;
    private Player player1;
    private Player player2;
    private int currentPlayerIndex;
    private boolean doingMove;

    private int whoWentFirst;
    private boolean raceState;
    private boolean player1HasMoved;
    private boolean player2HasMoved;
    private boolean player1FirstMoveEnded;
    private boolean player2FirstMoveEnded;

    /**
     * Default constructor for a GameState
     *
     * @param board   the game {@Link Board}
     * @param player1 the object that represents player 1
     * @param player2 the object that represents player 2
     */
    public GameState(Board board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayerIndex = -1;
        this.doingMove = false;

        this.whoWentFirst = -1;
        this.raceState = true;
        this.player1HasMoved = false;
        this.player2HasMoved = false;
        this.player1FirstMoveEnded = false;
        this.player2FirstMoveEnded = false;
    }


    /**
     * Get the Board
     * @return the board object
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Set the board object
     * @param board the object to set as the Board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Get the object representing the first player
     * @return Get the object representing the first player
     */
    public Player getPlayer1() {
        return player1;
    }

    /**
     * Set the object representing the first player
     * @param player1 Set the object representing the first player
     */
    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    /**
     * Get the object representing the second player
     * @return Get the object representing the second player
     */
    public Player getPlayer2() {
        return player2;
    }

    /**
     * Set the Object of the second player
     * @param player2 Object to represents player 2
     */
    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    /**
     * Get the index of the current player
     * @return Get the index of the current player
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Set the index of the player who's turn it is
     * @param currentPlayerIndex the player to set the current player to
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * Switch the current player index. Player 0 becomes 1 and 1 becomes 0
     */
    public void switchCurrentPlayerIndex() {
        if (currentPlayerIndex != -1) {
            this.currentPlayerIndex = (currentPlayerIndex + 1) % 2;
        }
    }

    /**
     * The board index of the current player
     * @return The board index of the current player
     */
    public int currentPlayerRow() {
        return currentPlayerIndex;
    }

    /**
     * Get the Object of the current player
     * @return the Object representation of the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayerIndex == 0 ? player1 : player2;
    }

    /**
     * Get the player object that has the player index specified
     * @param player the index of the player to get
     * @return the Object of the player
     */
    public Player getPlayerFor(int player) {
        return player == 0 ? player1 : player2;
    }

    /**
     * Player who took the first move of the game
     * @return the player index of the player who took the first turn
     */
    public int playerWhoWentFirst() {
        return whoWentFirst;
    }

    /**
     * Are we in the middle of a turn, with shells being moved?
     * @return true if shells are being moved, false if we are waiting for player input
     */
    public boolean isDoingMove() {
        return doingMove;
    }

    /**
     * Specify that we are in the middle of a turn, with shells being move around the board or not
     * @param doingMove true if shells are being moved, false if no move is being processed
     */
    public void setDoingMove(boolean doingMove) {
        this.doingMove = doingMove;
    }

    /**
     * Are we waiting for players first moves to finish?
     * @return true if moves have not been started or have not finished, false if we are both initial moves are over
     */
    public boolean isRaceState() {
        return raceState;
    }

    /**
     * Set that both players first moves have finished
     */
    public void setRaceStateOver() {
        raceState = false;
    }

    /**
     * Has the player chosen a tray this game?
     * @param player
     * @return true if they have chosen a tray, false if they haven't chosen one yet
     */
    public boolean playerHasMoved(int player) {
        return player == 0 ? player1HasMoved : player2HasMoved;
    }

    /**
     * Specify that the player has chosen a tray to move for the first time
     * @param player the player in question
     */
    public void setPlayerHasMoved(int player) {
        if (player == 0) {
            player1HasMoved = true;
        } else if (player == 1) {
            player2HasMoved = true;
        }

        if (playerWhoWentFirst() == -1)
            whoWentFirst = player;

    }

    /**
     * Specify that the players first move is over
     * @param player the player in question
     */
    public void setPlayerFirstMoveOver(int player) {
        if (player == 0) {
            player1FirstMoveEnded = true;
        } else if (player == 1) {
            player2FirstMoveEnded = true;
        }
    }

    /**
     * Has the first move of the player finished
     * @param player player to check
     * @return true if the move is over, false if the first move has not started or has not finished yet
     */
    public boolean isFirstMoveOverForPlayer(int player) {
        return player == 0 ? player1FirstMoveEnded : player2FirstMoveEnded;
    }

}
