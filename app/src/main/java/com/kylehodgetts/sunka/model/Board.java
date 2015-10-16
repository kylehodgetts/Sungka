package com.kylehodgetts.sunka.model;

/**
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
    public Board()
    {
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
     * @param x row position between 0 and 1
     * @param y column position between 0 and 6
     * @param newValue new value parameter the tray contents number should be changed to
     */
    public void setTray(int x, int y, int newValue)
    {
        board[x][y] = newValue;
    }

    /**
     * Get's the total number of shells in a specified tray at row position x and column position y
     * @param x row position between 0 and 1
     * @param y column position between 0 and 6
     * @return current total number of shells stored in the specified tray
     */
    public int getTray(int x, int y)
    {
        return board[x][y];
    }

    /**
     * Increments the specified tray's contents by 1 shell.
     * @param x row position between 0 and 1
     * @param y column position between 0 and 6
     */
    public void incrementTray(int x, int y)
    {
        ++board[x][y];
    }

    /**
     * Decrements the specified tray's contents by 1 shell, if the tray's contents are not empty
     * @param x row position between 0 and 1
     * @param y column position between 0 and 6
     */
    public void decrementTray(int x, int y)
    {
        if(board[x][y] > 0) --board[x][y];
    }

    /**
     * Empties the contents of the specified tray setting its total shell content value to 0 and
     * returns the contents that it had.
     * @param x row position between 0 and 1
     * @param y column position between 0 and 6
     * @return the total number of shells that was in the tray
     */
    public int emptyTray(int x, int y)
    {
        int counters = board[x][y];
        board[x][y] = 0;
        return counters;
    }

    /**
     * Checks to see if the specified tray is empty
     * @param x row position between 0 and 1
     * @param y column position between 0 and 6
     * @return true is the tray is empty, false otherwise
     */
    public boolean isEmptyTray(int x, int y)
    {
        return board[x][y] == 0;
    }

    /**
     * Gives a string interpretation of the current state of the board
     * @return a String interpretation of the board
     */
    public String toString()
    {
        String s = "";

        for(int i=0; i < board.length; ++i)
        {
            for(int j=0; j < 7; ++j)
            {
                s += "["+board[i][j]+"]";
            }
            s+= "\n";
        }
        return s;
    }


    public static void main(String[] args)
    {
        Board board = new Board();
        System.out.println(board);
    }

}
