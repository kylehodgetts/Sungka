package com.kylehodgetts.sunka.model;

/**
 * @author Charlie Baker
 * @version 1.0
 * Class created to represent the data model of the game board.
 */
public class Board {

    /*
     * board is represented by 2D integer array
     */
    private int[][] board;

    /**
     * Default constructor to create a new board initialising every Tray to 7 shells.
     */
    public Board() {
        board = new int[2][7];
        for(int i=0; i < board.length; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                board[i][j] = 7;
            }
        }
    }

    /**
     * Sets the sepcified tray at row position x and column position y to the new value given as a new value parameter.
     * @param row row position between 0 and 1
     * @param column column position between 0 and 6
     * @param newValue new value parameter the tray contents number should be changed to
     */
    public void setTray(int row, int column, int newValue) {
        board[row][column] = newValue;
    }

    /**
     * Get's the total number of shells in a specified tray at row position x and column position y
     * @param row row position between 0 and 1
     * @param column column position between 0 and 6
     * @return current total number of shells stored in the specified tray
     */
    public int getTray(int row, int column) {
        return board[row][column];
    }

    /**
     * Increments the specified tray's contents by 1 shell.
     * @param row row position between 0 and 1
     * @param column column position between 0 and 6
     */
    public void incrementTray(int row, int column) {
        ++board[row][column];
    }

    /**
     * Decrements the specified tray's contents by 1 shell, if the tray's contents are not empty
     * @param row row position between 0 and 1
     * @param column column position between 0 and 6
     */
    public void decrementTray(int row, int column) {
        if(board[row][column] > 0) --board[row][column];
    }

    /**
     * Empties the contents of the specified tray setting its total shell content value to 0 and
     * returns the contents that it had.
     * @param row row position between 0 and 1
     * @param column column position between 0 and 6
     * @return the total number of shells that was in the tray
     */
    public int emptyTray(int row, int column) {
        int counters = board[row][column];
        board[row][column] = 0;
        return counters;
    }

    /**
     * Checks to see if the specified tray is empty
     * @param row row position between 0 and 1
     * @param column column position between 0 and 6
     * @return true is the tray is empty, false otherwise
     */
    public boolean isEmptyTray(int row, int column) {
        return board[row][column] == 0;
    }

    /**
     * Checks to see whether the specified row is empty
     * @param row the row position between 0 and 1
     * @return
     */
    public boolean isEmptyRow(int row) {
        for (int i : board[row]) {
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

        for(int i=1; i >= 0; i--)
        {
            for(int j=0; j < 7; ++j)
            {
                s += "["+board[i][j]+"]";
            }
            s+= "\n";
        }
        return s;
    }
}
