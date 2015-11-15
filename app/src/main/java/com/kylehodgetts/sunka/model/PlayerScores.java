package com.kylehodgetts.sunka.model;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * A model to represent scores of one player
 */
public class PlayerScores {
    private String name;
    private int maxScore, gamesWon, gamesLost;

    /**
     * Default constructor to make this model class
     * @param name      The name of the user
     * @param maxScore  The max score value this player achieved
     * @param gamesWon  The number of games won by this player
     * @param gamesLost The number of games lost by this player
     */
    public PlayerScores(String name, int maxScore, int gamesWon, int gamesLost) {
        this.name = name;
        this.maxScore = maxScore;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
    }

    /**
     * Gets the name of the user
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the max score this player achieved
     * @return
     */
    public int getMaxScore() {
        return maxScore;
    }

    /**
     * Gets the games won by this player
     * @return
     */
    public int getGamesWon() {
        return gamesWon;
    }

    /**
     * Gets the games lost by this player
     * @return
     */
    public int getGamesLost() {
        return gamesLost;
    }
}
