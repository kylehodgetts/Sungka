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

    public void updatePot(int x, int y)
    {

    }

    public void doMove(int fromX, int fromY)
    {
        int counters = board[fromX][fromY];


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
