package com.kylehodgetts.sunka.model;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Jonathan Burton
 */
public class GameStateTest {

    private GameState gameState;
    private Board board = new Board();
    private Player player1 = new Player();
    private Player player2 = new Player();


    /**
     * Sets the gameState up with relevant parameters for testing
     */
    @Before
    public void setUp() {
        gameState = new GameState(board, player1, player2);
    }

    /**
     * Test that all initial values are as they should be
     * @throws Exception if values not as should be
     */
    @Test
    public void checkInitialValues() throws Exception {
        Assert.assertEquals(gameState.getCurrentPlayerIndex(), -1);
        Assert.assertEquals(gameState.playerHasMoved(0), false);
        Assert.assertEquals(gameState.playerHasMoved(1), false);
        Assert.assertEquals(gameState.isFirstMoveOverForPlayer(0), false);
        Assert.assertEquals(gameState.isFirstMoveOverForPlayer(1), false);
    }

    /**
     * Test that switching players works as expected
     */
    @Test
    public void setAndSwitchCurrentPlayer() throws Exception {
        gameState.switchCurrentPlayerIndex();
        Assert.assertEquals(gameState.getCurrentPlayerIndex(), -1);

        for (int player = 0; player < 2; player++) {
            int otherPLayer = (player + 1) % 2;
            gameState.setCurrentPlayerIndex(player);
            Assert.assertEquals(gameState.getCurrentPlayerIndex(), player);

            gameState.switchCurrentPlayerIndex();
            Assert.assertEquals(gameState.getCurrentPlayerIndex(), otherPLayer);
            gameState.switchCurrentPlayerIndex();
            Assert.assertEquals(gameState.getCurrentPlayerIndex(), player);

        }
    }

    /**
     * Test that players have moved or not
     * @throws Exception
     */
    @Test
    public void setPlayerMoved() throws Exception {

        for (int player = 0; player < 2; player++) {
            gameState = new GameState(board, player1, player2);
            int otherPLayer = (player + 1) % 2;

            Assert.assertEquals(gameState.playerWhoWentFirst(), -1);

            gameState.setPlayerHasMoved(player);

            Assert.assertTrue(gameState.playerHasMoved(player));
            Assert.assertFalse(gameState.playerHasMoved(otherPLayer));

            Assert.assertEquals(gameState.playerWhoWentFirst(), player);

            gameState.setPlayerHasMoved(otherPLayer);

            Assert.assertTrue(gameState.playerHasMoved(otherPLayer));
            Assert.assertEquals(gameState.playerWhoWentFirst(), player);
        }
    }

}
