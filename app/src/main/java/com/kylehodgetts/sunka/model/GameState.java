package com.kylehodgetts.sunka.model;

import com.kylehodgetts.sunka.controller.bus.BusState;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          The current state of the game, contains all of the models for the game
 */
public class GameState implements BusState {

    public final static int NO_ONE_DONE = -2;
    public final static int PLAYER1_DONE = -1;
    public final static int PLAYER2_DONE = 0;
    public final static int BOTH_DONE = 1;
    public final static int INIT_DONE = 2;

    //TODO comment everything in this class

    private Board board;
    private Player player1;
    private Player player2;
    private int currentPlayerIndex;
    private int initialising; // VALUE -2(noone has pressed anything yet); -1(player1 has pressed); 0(player2 has pressed); 1(All user init action done, waiting for processing); 2(No more init)
    private int whoWentFirst;
    private boolean doingMove;


    public GameState(Board board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayerIndex = -1;
        this.initialising = -2;
        this.whoWentFirst = -1;
        this.doingMove = false;
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

    public boolean isInitialising() {
        return initialising < INIT_DONE;
    }

    public void nextInitPhase(int player) {
        if (initialising == NO_ONE_DONE) {
            initialising += 1 + player;
        } else if ((initialising == PLAYER1_DONE && player == 1) || (initialising == PLAYER2_DONE && player == 0)) {
            initialising = BOTH_DONE;
        }
    }

    public void finishInit() {
        if (initialising == 1) {
            initialising++;
        }
    }


    public boolean playerInitialising(int player) {
        if (initialising > 1) return false;
        else
            return initialising < -1 || (player == 0 && initialising == 0) || (player == 1 && initialising == -1);
    }

    public Player getPlayerFor(int player) {
        return player == 0 ? player1 : player2;
    }

    public int getWhoWentFirst() {
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
}
