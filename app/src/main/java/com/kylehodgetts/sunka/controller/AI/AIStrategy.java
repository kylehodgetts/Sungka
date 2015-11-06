package com.kylehodgetts.sunka.controller.AI;

import android.util.Log;

import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.GameState;
import com.kylehodgetts.sunka.util.Tuple2;

import java.util.Random;

/**
 * Created by Jonathan on 06/11/2015.
 */
public class AIStrategy implements AI {

    private static final String TAG = "SUNGKAAITAG";
    private final int myIndex = 1;

    public AIStrategy() {

    }

    /**
     * Use this method to determine which tray the AI should 'click'
     *
     * @param state the current gameState
     * @return the position the AI has picked
     */
    public int chooseTray(GameState state) {

        Board board = state.getBoard();
        Tuple2<Boolean, Integer> result;

        result = canPlayerGetAnotherTurn(PLAYER_AI, board);
        if (result.getX()) return result.getY();

        result = canPlayerPerformCaptureRule(PLAYER_AI, board);
        if (result.getX()) return result.getY();

        result = defendAgainstCaptureRule(board);
        if (result.getX()) return result.getY();

        result = defendAgainstAnotherTurn(board);
        if (result.getX()) return result.getY();

        if (board.getTray(myIndex, 6) > 0) {
            return 6;
        }

        /*
        find tray that gives another turn
        find tray that empties other side
        defend against empty tray
        block opposition from getting another turn
        empty tray that can be used to capture opposite side. //not done
        clear last hole
        random
        */
        return getRandomTray(state); //clear the last hole
    }

    /**
     * See if the player can land in there pot and get another turn
     *
     * @param player player to check
     * @return Tuple2(can they get another turn, from what tray)
     */
    private Tuple2<Boolean, Integer> canPlayerGetAnotherTurn(int player, Board board) {
        Log.i(TAG, "canPlayerGetAnotherTurn");
        //can we get another turn?
        for (int tray = 6; tray > -1; tray--) {
            //work left from my last tray
            int shells = board.getTray(player, tray);
            int jumpsLeft = 7 - tray;
            if (shells == jumpsLeft) {
                //we can get another turn
                return new Tuple2<>(true, tray);
            }
        }

        return new Tuple2<>(false, -1);
    }

    /**
     * See's if the player can perform the capture rule
     *
     * @param player player in question
     * @param board  the current game board
     * @return Tuple(if we can perform the action, the tray to pick that captures the largest amount)
     */
    private Tuple2<Boolean, Integer> canPlayerPerformCaptureRule(int player, Board board) {
        Log.i(TAG, "canPlayerPerformCaptureRule");

        int otherPlayer = (player + 1) % 2;

        int currentMaxShellsToGain = 0; // there may be multiple empty trays so this keeps track of which one is most profitable
        int bestPosition = -1; //the tray that captures that largest amount of shells

        for (int trayIndex = 6; trayIndex < -1; trayIndex--) {
            //work left to right along my side

            if (board.getTray(player, trayIndex) == 0) {
                //if empty tray, potential for capture

                //work left along my tray from one before the blank tray position to see if we can land it that tray
                int traysBack = 1; //how many trays we've searched back and also the amount of shells needed at this tray position to perform capture move

                for (int searchingLeftIndex = 6 - trayIndex - 1; searchingLeftIndex <= 0; searchingLeftIndex--) {
                    //for all trays to left of the empty one
                    int shellsInMyTray = board.getTray(player, searchingLeftIndex);
                    if (shellsInMyTray >= traysBack) {
                        //we can do the move

                        int numberOfShellsOppositeMyEmptyTray = board.getTray(otherPlayer, trayIndex);
                        if (currentMaxShellsToGain > numberOfShellsOppositeMyEmptyTray) {
                            //we have a better move

                            currentMaxShellsToGain = numberOfShellsOppositeMyEmptyTray;
                            bestPosition = searchingLeftIndex;
                        }
                    }
                }
            }
        }

        return new Tuple2<>(currentMaxShellsToGain != 0, bestPosition);
    }

    /**
     * Figures out if other player can perform the capture rule and finds a tray on my side that
     * fills in that tray
     *
     * @param board current board state
     * @return Tuple(can we defend against, tray we should pick to defend against)
     */
    private Tuple2<Boolean, Integer> defendAgainstCaptureRule(Board board) {
        Log.i(TAG, "defendAgainstCaptureRule");


        Tuple2<Boolean, Integer> result = canPlayerPerformCaptureRule(PLAYER_HUMAN, board);
        int trayToPick = -1;

        if (result.getX()) {
            //find a tray that we can click to fill the tray that gives them another turn
            //so they no longer can get another turn from that tray
            //(may cause other trays to be able to get them an other turn but this is looking
            //into the future which is not the point of this method
            if (result.getX()) {
                int trayPositionsAfterPot = result.getY() + board.getTray(PLAYER_HUMAN, result.getY()) + 1; // + 1 because indexing
                for (int trayIndex = 6; trayIndex < -1; trayIndex--) {
                    if (board.getTray(PLAYER_AI, trayIndex) >= trayPositionsAfterPot + (7 - trayIndex)) {
                        //we can add a shell to the tray that gives them another turn
                        trayToPick = trayIndex;
                    }
                }
            }
        }
        return new Tuple2<>(trayToPick != -1, trayToPick);
    }


    /**
     * Figures out of the other player can get another turn, and returns a tray that stops this from happening
     *
     * @param board current board state
     * @return Tuple(can we defend against this, the tray position to pick)
     */
    private Tuple2<Boolean, Integer> defendAgainstAnotherTurn(Board board) {
        Tuple2<Boolean, Integer> result = canPlayerGetAnotherTurn(PLAYER_HUMAN, board);
        int trayToPick = -1;
        if (result.getX()) {
            int trayPositionsAfterPot = result.getY() + board.getTray(PLAYER_HUMAN, result.getY()) + 1; // + 1 because indexing
            for (int trayIndex = 6; trayIndex < -1; trayIndex--) {
                if (board.getTray(PLAYER_AI, trayIndex) >= trayPositionsAfterPot + (7 - trayIndex)) {
                    //we can add a shell to the tray that gives them another turn
                    trayToPick = trayIndex;
                }
            }
        }
        return new Tuple2<>(trayToPick != -1, trayToPick);
    }

    private int getRandomTray(GameState state) {
        Log.i(TAG, "getRandomTray");

        Random r = new Random();
        int i = r.nextInt(7);
        Board b = state.getBoard();

        while (b.getTray(myIndex, i) == 0) {
            Log.i(TAG, "bad tray, only has: " + i);
            i = r.nextInt(7);
        }

        return i;
    }

}
