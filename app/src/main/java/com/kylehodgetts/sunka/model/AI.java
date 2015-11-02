package com.kylehodgetts.sunka.model;

public class AI extends Player {
//At this point it only evaluates the best move for the first round
   public AI(){
       this.setSide(0);
   }
    public void strategyTray(Board board,int MaxSide){
        Board  boardMinSide;
        int sevenTrays[] = new int[7];
        for( int x = 0; x<7; x++){
            moveChance(MaxSide,x,board);

        }

    }
    private int moveChance(int i,int j,Board board){
        int highLow = 0;
//            moveShells(i,j,board);

        return highLow;
    }

    private Board moveShells(int i,int j,Board board){
        //Algorithm;


        return board;
    }

}
