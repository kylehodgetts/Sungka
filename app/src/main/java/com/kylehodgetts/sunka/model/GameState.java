package com.kylehodgetts.sunka.model;

import com.kylehodgetts.sunka.controller.bus.BusState;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          The current state of the game, contains all of the models for the game
 */
public class GameState implements BusState {

    //TODO comment everything in this class

    private Board board;
    private Player player1;
    private Player player2;
    private int currentPlayerIndex;
    private boolean doingMove;

    private int whoWentFirst;
    private boolean raceStateOver;
    private boolean player1HasMoved;
    private boolean player2HasMoved;
    private boolean player1FirstMoveEnded;
    private boolean player2FirstMoveEnded;



    public GameState(Board board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayerIndex = -1;
        this.doingMove = false;

        this.whoWentFirst = -1;
        this.raceStateOver = false;
        this.player1HasMoved = false;
        this.player2HasMoved = false;
        this.player1FirstMoveEnded = false;
        this.player2FirstMoveEnded = false;
    }


    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void switchCurrentPlayerIndex() {
        if (currentPlayerIndex != -1) {
            this.currentPlayerIndex = (currentPlayerIndex + 1) % 2;
        }
    }

    public int currentPlayerRow() {
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return currentPlayerIndex == 0 ? player1 : player2;
    }

    public Player getPlayerFor(int player) {
        return player == 0 ? player1 : player2;
    }

    public int playerWhoWentFirst() {
        return whoWentFirst;
    }

    public void setWhoWentFirst(int to) {
        whoWentFirst = to;
    }

    public boolean isDoingMove() {
        return doingMove;
    }

    public void setDoingMove(boolean doingMove) {
        this.doingMove = doingMove;
    }

    public boolean isRaceStateOver() {
        return raceStateOver;
    }

    public void setRaceStateOver() {
        raceStateOver = true;
    }

    public boolean playerHasMoved(int player) {
        return player == 0 ? player1HasMoved : player2HasMoved;
    }

    public void setPlayerHasMoved(boolean moved, int player) {
        if (player == 0) {
            player1HasMoved = moved;
        } else if (player == 1) {
            player2HasMoved = moved;
        }

    }

    public void setPlayerFirstMoveEnded(int player) {
        if (player == 0) {
            player1FirstMoveEnded = true;
        } else if (player == 1) {
            player2FirstMoveEnded = true;
        }
    }

    public boolean isFirstMoveOverForPlayer(int player) {
        return player == 0 ? player1HasMoved : player2FirstMoveEnded;
    }

}
