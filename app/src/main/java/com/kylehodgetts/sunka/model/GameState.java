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
    private boolean playerOneTurn;


    public GameState(Board board, Player player1, Player player2, boolean playerOneTurn) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.playerOneTurn = playerOneTurn;
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

    public boolean isPlayerOneTurn() {
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

    public void setPlayerOneTurn(boolean playerOneTurn) {
        this.playerOneTurn = playerOneTurn;
    }

    public int currentPlayerRow(){
        return playerOneTurn? 0 : 1;
    }

    public Player getCurrentPlayer(){
        return playerOneTurn?player1:player2;
    }
}
