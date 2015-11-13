package com.kylehodgetts.sunka.model;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class PlayerScores {
    private String name;
    private int maxScore, gamesWon, gamesLost;

    public PlayerScores(String name, int maxScore, int gamesWon, int gamesLost) {
        this.name = name;
        this.maxScore = maxScore;
        this.gamesWon = gamesWon;
        this.gamesLost = gamesLost;
    }

    public String getName() {
        return name;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }
}
