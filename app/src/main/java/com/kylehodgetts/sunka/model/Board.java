package com.kylehodgetts.sunka.model;

/**
 * Created by CBaker on 16/10/2015.
 */
public class Board {

    private int[][] board;


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

    public void setPot(int x, int y, int newValue)
    {
        board[x][y] = newValue;
    }

    public int getPot(int x, int y)
    {
        return board[x][y];
    }

    public void incrementPot(int x, int y)
    {
        ++board[x][y];
    }

    public void decrementPot(int x, int y)
    {
        --board[x][y];
    }

    public int emptyPot(int x, int y)
    {
        int counters = board[x][y];
        board[x][y] = 0;
        return counters;
    }

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
