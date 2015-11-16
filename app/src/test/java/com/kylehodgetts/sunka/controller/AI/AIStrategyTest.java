package com.kylehodgetts.sunka.controller.AI;

import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.util.Tuple2;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * @author Jonathan Burton
 * @version 1.0
 */
public class AIStrategyTest extends AIStrategy {

    Tuple2<Boolean, Integer> result;
    private AIStrategy ai;
    private Board board;

    @Before
    public void setUp() {
        ai = new AIStrategy();
    }

    /**
     * Tests if the ai can see and execute getting another turn
     */
    @Test
    public void testCanPlayerGetAnotherTurn() throws Exception {
        for (int tray = 0; tray < 7; tray++) {
            board = new Board();
            board.setTray(AI.PLAYER_AI, tray, 7 - tray);
            result = ai.canPlayerGetAnotherTurn(AI.PLAYER_AI, board);
            Assert.assertEquals((int) result.getY(), tray);
        }
    }

    /**
     * This test confirms the AI can perform the capture rule
     */
    @Test
    public void testCanPlayerPerformCaptureRule() throws Exception {
        for (int emptyTray = 1; emptyTray < 7; emptyTray++) {
            for (int trayToChoose = 0; trayToChoose < 7; trayToChoose++) {
                board = new Board();

                board.setTray(AI.PLAYER_AI, emptyTray, 0);
                board.setTray(AI.PLAYER_AI, trayToChoose, emptyTray - trayToChoose);
                result = ai.canPlayerPerformCaptureRule(AI.PLAYER_AI, board);

                if (trayToChoose < emptyTray) {
                    Assert.assertTrue(result.getX());
                    Assert.assertEquals((int) result.getY(), trayToChoose);
                } else {
                    Assert.assertFalse(result.getX());
                }
            }
        }
    }

    /**
     * This tests that the AI can find a potential capture from the other player,
     * and select a pot to stop this from happening
     */
    @Test
    public void testDefendAgainstCaptureRule() throws Exception {

        for (int emptyTray = 1; emptyTray < 7; emptyTray++) {
            for (int captureTray = 0; captureTray < emptyTray; captureTray++) {
                for (int AITray = 0; AITray < 7; AITray++) {
                    board = new Board();
                    board.setTray(AI.PLAYER_HUMAN, emptyTray, 0);
                    board.setTray(AI.PLAYER_HUMAN, captureTray, emptyTray - captureTray);

                    //set ai trays to one so they do not interfere with the one we want to test
                    for (int i = 0; i < 7; i++) {
                        board.setTray(AI.PLAYER_AI, i, 1);
                    }

                    //this checks that the human side of the board is set up correctly
                    result = ai.canPlayerPerformCaptureRule(AI.PLAYER_HUMAN, board);
                    Assert.assertTrue(result.getX());

                    //set tray to be valid for this move
                    int distance = (7 - AITray) + (captureTray + 1);
                    board.setTray(AI.PLAYER_AI, AITray, distance);

                    //check the ai found it
                    result = ai.defendAgainstCaptureRule(board);
                    Assert.assertTrue(result.getX());
                    Assert.assertEquals((int) result.getY(), AITray);
                }
            }
        }
    }

    /**
     * Tests that the ai can determine if the other player can get a turn, and if so can
     * the ai choose a pot that lands in that opponents tray so that the human cannot get
     * another turn
     */
    @Test
    public void testDefendAgainstAnotherTurn() throws Exception {

        for (int opponentsTray = 0; opponentsTray < 7; opponentsTray++) {
            board = new Board();
            for (int tray = 0; tray < 7; tray++) {
                board.setTray(AI.PLAYER_AI, tray, 1);
            }

            board.setTray(AI.PLAYER_HUMAN, 0, 8);
            board.setTray(AIStrategy.PLAYER_HUMAN, opponentsTray, 7 - opponentsTray);

            for (int myTray = 0; myTray < 7; myTray++) {
                int potsToTravel = (7 - myTray) + (opponentsTray + 1);
                Random r = new Random();
                board.setTray(AI.PLAYER_AI, myTray, potsToTravel + r.nextInt(7));

                result = ai.defendAgainstAnotherTurn(board);
                Assert.assertTrue(result.getX());
                Assert.assertEquals(myTray, (int) result.getY());
            }
        }
    }

    /**
     * Tests that the ai can pick a tray that has shells in
     */
    @Test
    public void testRandomTray() throws Exception {

        for (int positionWithShells = 0; positionWithShells < 7; positionWithShells++) {
            board = new Board();
            for (int position = 0; position < 7; position++) {
                //zero all other trays so the AI can't pick that tray
                if (positionWithShells != position) {
                    board.setTray(AI.PLAYER_AI, position, 0);
                }
            }
            int choice = ai.getRandomTray(board);
            Assert.assertEquals(choice, positionWithShells);
        }
    }
}
