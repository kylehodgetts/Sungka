package com.kylehodgetts.sunka.model;

import junit.framework.TestCase;

/**
 * @author Kyle Hodgetts
 */
public class GameStateTest extends TestCase {
    private GameState gameState;
    private Board board;
    private Player p1;
    private Player p2;

    public void setUp() throws Exception {
        super.setUp();
        board = new Board();
        p1 = new Player();
        p2 = new Player();
        gameState = new GameState(board, p1, p2);
    }

    public void testGetBoard() throws Exception {
        assertTrue(gameState.getBoard() == board);
    }

    public void testSetBoard() throws Exception {
        Board newBoard = new Board();
        gameState.setBoard(newBoard);
        assertFalse(gameState.getBoard() == board);
    }

    public void testGetPlayer1() throws Exception {
        assertTrue(gameState.getPlayer1() == p1);
    }

    public void testSetPlayer1() throws Exception {
        Player newP1 = new Player();
        gameState.setPlayer1(newP1);
        assertFalse(gameState.getPlayer1() == p1);
    }

    public void testGetPlayer2() throws Exception {
        assertTrue(gameState.getPlayer2() == p2);
    }

    public void testSetPlayer2() throws Exception {
        Player newP2 = new Player();
        gameState.setPlayer2(newP2);
        assertFalse(gameState.getPlayer2() == p2);
    }

    public void testGetCurrentPlayerIndex() throws Exception {
        gameState.setCurrentPlayerIndex(0);
        assertTrue(gameState.getCurrentPlayerIndex() == 0);
    }

    public void testSetCurrentPlayerIndex() throws Exception {
        gameState.setCurrentPlayerIndex(0);
        assertTrue(gameState.getCurrentPlayerIndex() == 0);
        gameState.setCurrentPlayerIndex(1);
        assertFalse(gameState.getCurrentPlayerIndex() == 0);
        assertTrue(gameState.getCurrentPlayerIndex() == 1);
    }

    public void testCurrentPlayerRow() throws Exception {
        gameState.setCurrentPlayerIndex(0);
        assertTrue(gameState.currentPlayerRow() == 0);
    }

    public void testGetCurrentPlayer() throws Exception {
        gameState.setCurrentPlayerIndex(0);
        assertTrue(gameState.getCurrentPlayer() == p1);
        gameState.setCurrentPlayerIndex(1);
        assertTrue(gameState.getCurrentPlayer() == p2);
    }

    public void testIsInitialising() throws Exception {}

    public void testNextInitPhase() throws Exception {}

    public void testFinishInit() throws Exception {}

    public void testPlayerInitialising() throws Exception {}

    public void testGetPlayerFor() throws Exception {
        assertTrue(gameState.getPlayerFor(0) == p1);
        assertTrue(gameState.getPlayerFor(1) == p2);
    }

    public void testIsDoingMove() throws Exception {
        gameState.setDoingMove(true);
        assertTrue(gameState.isDoingMove());
        gameState.setDoingMove(false);
        assertFalse(gameState.isDoingMove());
    }

    public void testSetDoingMove() throws Exception {
        gameState.setDoingMove(true);
        assertTrue(gameState.isDoingMove());
        gameState.setDoingMove(false);
        assertFalse(gameState.isDoingMove());
    }
}