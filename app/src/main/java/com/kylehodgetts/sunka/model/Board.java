package com.kylehodgetts.sunka.model;

import java.io.Serializable;

/**
 * @author Charlie Baker
 * @version 1.1
 * Class created to represent the data model of the game board.
 */
public class Board implements Serializable {

    /*
     * board is represented by 2D integer array
     */
    private int[][] board;

    /**
     * Default constructor to create a new board initialising every Tray to 7 shells.
     */
    public Board() {
        board = new int[2][7];
        for(int i=0; i < 2; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                board[i][j] = 7;
            }
        }
    }

    /**
     * Sets the sepcified tray at player position x and tray position y to the new value given as a new value parameter.
     * @param player player position between 0 and 1
     * @param tray tray position between 0 and 6
     * @param newValue new value parameter the tray contents number should be changed to
     */
    public void setTray(int player, int tray, int newValue) {
        board[player][tray] = newValue;
    }

    /**
     * Get's the total number of shells in a specified tray at player position x and tray position y
     * @param player player position between 0 and 1
     * @param tray tray position between 0 and 6
     * @return current total number of shells stored in the specified tray
     */
    public int getTray(int player, int tray) {
        return board[player][tray];
    }

    /**
     * Increments the specified tray's contents by 1 shell.
     * @param player player position between 0 and 1
     * @param tray tray position between 0 and 6
     */
    public void incrementTray(int player, int tray) {
        ++board[player][tray];
    }

    /**
     * Decrements the specified tray's contents by 1 shell, if the tray's contents are not empty
     * @param player player position between 0 and 1
     * @param tray tray position between 0 and 6
     */
    public void decrementTray(int player, int tray) {
        if (board[player][tray] > 0) --board[player][tray];
    }

    /**
     * Empties the contents of the specified tray setting its total shell content value to 0 and
     * returns the contents that it had.
     * @param player player position between 0 and 1
     * @param tray tray position between 0 and 6
     * @return the total number of shells that was in the tray
     */
    public int emptyTray(int player, int tray) {
        int counters = board[player][tray];
        board[player][tray] = 0;
        return counters;
    }

    /**
     * Checks to see if the specified tray is empty
     * @param player player position between 0 and 1
     * @param tray tray position between 0 and 6
     * @return true is the tray is empty, false otherwise
     */
    public boolean isEmptyTray(int player, int tray) {
        return board[player][tray] == 0;
    }

    /**
     * Checks to see whether the specified player is empty
     * @param player the player position between 0 and 1
     * @return
     */
    public boolean isEmptyRow(int player) {
        for (int i : board[player]) {
            if(i != 0) return false;
        }
        return true;
    }

    /**
     * Gives a string interpretation of the current state of the board
     * @return a String interpretation of the board
     */
    @Override
    public String toString() {
        String s = "";

        for(int j=6; j >= 0; j--) { s += "["+board[0][j]+"]"; }
        s+= "\n";
        for(int j=0; j < 7; ++j) { s += "["+board[1][j]+"]"; }

        return s;
    }
}
