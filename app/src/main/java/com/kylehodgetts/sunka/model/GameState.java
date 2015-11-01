package com.kylehodgetts.sunka.model;

import com.kylehodgetts.sunka.controller.bus.BusState;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * The current state of the game, contains all of the models for the game
 */
public class GameState implements BusState {

    private Board board;
    private Player player1;
    private Player player2;
    private int playerOneTurn;
    private int initialising; // VALUE -2(noone has pressed anything yet); -1(player1 has pressed); 0(player2 has pressed); 1(All user init action done, waiting for processing); 2(No more init)
    private boolean doingMove;


    public GameState(Board board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.playerOneTurn = -1;
        this.initialising = -2;
        this.doingMove = false;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getPlayerOneTurn() {
        return playerOneTurn;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public void setPlayerOneTurn(int playerOneTurn) {
        this.playerOneTurn = playerOneTurn;
    }

    public int currentPlayerRow(){
        return playerOneTurn;
    }

    public Player getCurrentPlayer(){
        return playerOneTurn == 0? player1:player2;
    }

    public boolean isInitialising() {
        return initialising < 2; }

    public void nextInitPhase(int player) {
        if(initialising == -2){
            initialising += 1 + player;
        }else if((initialising == -1 && player == 1) || (initialising==0 && player==0)){
            initialising = 1;
        }
    }

    public void finishInit() {
        if(initialising == 1)
            ++initialising;
    }

    public boolean playerInitialising(int player) {
        if (initialising > 1) return false;
        else
            return initialising < -1 || (player == 0 && initialising == 0) || (player == 1 && initialising == -1);
    }

    public Player getPlayerFor(int player) {
        return player==0?player1:player2;
    }

    public boolean isDoingMove() {
        return doingMove;
    }

    public void setDoingMove(boolean doingMove) {
        this.doingMove = doingMove;
    }
}
