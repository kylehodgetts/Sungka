package com.kylehodgetts.sunka.controller;

import com.kylehodgetts.sunka.BoardActivity;
import com.kylehodgetts.sunka.model.Board;
import com.kylehodgetts.sunka.model.Player;

import java.util.Random;

/**
 * Controller class to link together the {@link Board} Data Model and the {@link BoardActivity} View.
 * This class contains all the logic methods of the game in order to control the board moves of the shells.
 * Including methods to check a move is valid and to determine if a player stells the shells in their
 * opponents opposite tray.
 *
 * @author Charlie Baker
 * @version 1.0
 */
public class BoardController {

    Board board;
    private Player playerOne;
    private Player playerTwo;

    private int playersTurn;
    private boolean bonusMove;

    private BoardActivity boardActivity;

    /**
     * Default constructor which requires two {@link Player} objects in order for a game board
     * to be created for a new game.
     * Constructs a new {@link BoardActivity} GUI and displays this on the device's screen.
     *
     * @param playerOne First player of the game usually referring to the first human player
     * @param playerTwo Second player of the game which could either be a human or AI player
     */
    public BoardController(Player playerOne, Player playerTwo) {
        board = new Board();
        boardActivity = new BoardActivity();

        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.bonusMove = false;

        determineFirstPlayer();
    }

    /**
     * Method to determine which player goes first.
     * Temporarily currently this meets Requirement 2 by selecting a player at random.
     */
    private void determineFirstPlayer() { // TODO: Needs Requirement 8 to be implemented here instead
        Random random = new Random();
        playersTurn = random.nextInt(2);
    }

    /**
     * Method to carry out a move from the specified tray if the move is valid and the player
     * can actually make a move because there are shells in their side of the board.
     * The method will empty the selected tray and increment each tray in an anti-clockwise direction.
     * At the end it will decide if the players turn has ended and switch to the other player. Otherwise
     * the current player still has another turn due to earning a bonus move.
     *
     * @param row The row being 0 or 1 of the tray being selected.
     * @param column The column 0 to 6 of the tray being selected.
     */
    public void doMove(int row, int column) {
        int currentRow = row;
        int currentColumn = column;

        if(moveValid(row, column) && canPlayerMove()) {
            int shellsInHand = board.emptyTray(row, column);
            while(shellsInHand > 0) {
                bonusMove = false;
                if(currentColumn == 6 && currentRow == 1) {
                    if(!addToPlayerStore(currentRow)) { ++shellsInHand; }
                    currentRow = 0;
                    ++currentColumn;
                    bonusMove = true;
                }
                else if(currentColumn == 0 && currentRow == 0) {
                    if(!addToPlayerStore(currentRow)) { ++shellsInHand; }
                    currentRow = 1;
                    --currentColumn;
                    bonusMove = true;
                }
                else if(currentRow == 0) {
                    board.incrementTray(currentRow, --currentColumn);
                    if(shellsInHand == 1 && hasStealOccurred(currentRow, currentColumn != 0? currentColumn-1 : currentColumn)) {
                        stealShells(currentRow +1, currentColumn);
                    }
                }
                else if(currentRow == 1) {
                    board.incrementTray(currentRow, ++currentColumn);
                    if(shellsInHand == 1 && hasStealOccurred(currentRow, currentColumn != 6? currentColumn+1 :currentColumn)) {
                        stealShells(currentRow -1, currentColumn);
                    }
                }
                --shellsInHand;
            }
        }
        endPlayersTurn();
    }

    /**
     * Adds a shell to the current players store if the next tray is their store when a move is happening.
     * Otherwise it is not their store to add the shell to and no shell is added to the opponent's store.
     *
     * @param currentRow The current row where the move is traversing from.
     * @return True is a shell has been added to the player's store Otherwise False if no shell was added to the store.
     */
    private boolean addToPlayerStore(int currentRow) {
        if(playersTurn==0 && currentRow == playersTurn) {
            playerOne.addToPot(1);
            return true;
        }
        else if(playersTurn == 1 && currentRow == playersTurn) {
            playerTwo.addToPot(1);
            return true;
        }
        return false;
    }

    /**
     * Method to determine if the current player's turn has ended. If so, then it is logged that it
     * is now the other player's turn. Otherwise the current player has earned a bonus move and the
     * current player can have another turn.
     */
    private void endPlayersTurn() {
        if(!hasBonusMove() && playersTurn == 0) { playersTurn = 1; }
        else if(!hasBonusMove() && playersTurn == 1) { playersTurn = 0; }
    }

    /**
     * Checks if the current player has earned an extra bonus move.
     *
     * @return True if they have earned a bonus move, otherwise False.
     */
    public boolean hasBonusMove() { return bonusMove; }

    /**
     * Checks if the current player steals shells from the opposite tray on their opponents side of
     * the board.
     * This checks if the last shell from their move lands in an empty shell on their own side of the
     * board and there are shells in the opposite tray to steal.
     *
     * @param currentRow The current Row of the last move
     * @param endingColumn The Column of the last move
     * @return True if the last shell has landed in an empty shell on the player's side of the board, otherwise False.
     */
    public boolean hasStealOccurred(int currentRow, int endingColumn) {
        int otherPlayersRow = currentRow == 0? 1 : 0;
        return board.isEmptyTray(currentRow, endingColumn) &&
                currentRow == playersTurn &&
                !board.isEmptyTray(otherPlayersRow, endingColumn);
    }

    /**
     * Steals the shells from the player's opponents opposite tray and places them in the player's store.
     * Including the last shell that landed in the opponents tray.
     *
     * @param row The row of opposite tray the steal is to take place on.
     * @param column The column of the tray the steal is to take place on.
     */
    private void stealShells(int row, int column) {
        if(playersTurn == 0) {
            playerOne.addToPot(board.emptyTray(row, column));
            playerOne.addToPot(board.emptyTray(row-1, column));
        }
        else {
            playerTwo.addToPot(board.emptyTray(row, column));
            playerTwo.addToPot(board.emptyTray(row+1, column));
        }
    }

    /**
     * Checks to see if a move is valid that the player has selected a tray on their own side
     * of the board. Also that the tray they have selected is not empty.
     *
     * @param row The row of the tray the move is to start from.
     * @param column The column of the tray the move is to start from.
     * @return True if it is a valid move, otherwise False.
     */
    public boolean moveValid(int row, int column) {
        return ((playersTurn == 1 && row == 1) || (playersTurn == 0 && row == 0))
                && !board.isEmptyTray(row, column);
    }

    /**
     * Checks to see that the current player can actually make a move.
     *
     * @return True if the player has shells remaining in their side and therefore can make a move this turn.
     */
    public boolean canPlayerMove() {
        for(int column = 0; column < 7; ++column) {
            if(!board.isEmptyTray(playersTurn, column)) { return true; }
        }
        return false;
    }

    /**
     * Get's the current players turn
     *
     * @return An integer where 0 is player one's turn and 1 for player two's.
     */
    public int getPlayersTurn() { return playersTurn; }

}
