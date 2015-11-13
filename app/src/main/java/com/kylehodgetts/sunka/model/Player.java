package com.kylehodgetts.sunka.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Models the player in the game
 */
public class Player implements Parcelable, Serializable {
    private int stonesInPot;
    private int wonGames;
    private int side;


    /**
     * Creates a new player setting stones in pot to 0
     */
    public Player(){
        stonesInPot = 0;
    }

    /**
     * @return the side the user is on
     */
    public int getSide() {
        return side;
    }

    /**
     *
     * @param side set the side the user
     */
    public void setSide(int side) {
        this.side = side;
    }

    /**
     *
     * @return the number of games the player has won
     */
    public int getWonGames() {
        return wonGames;
    }

    /**
     * Increment the number of games the user has won by one.
     */
    public void addWonGames() {
        wonGames++;
    }

    /**
     * @return number of stones in the player's pot
     */
    public int getStonesInPot(){
        return stonesInPot;
    }

    public void setStonesInPot(int stones) {
        stonesInPot = stones;
    }

    /**
     * Adds <code>x</code> amount of stones to pot
     * @param x number to collected stones
     */
    public void addToPot(int x) {
        stonesInPot += x;
    }

    public void resetStonesInPot(){
        stonesInPot = 0;
    }


    protected Player(Parcel in) {
        stonesInPot = in.readInt();
        wonGames = in.readInt();
        side = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stonesInPot);
        dest.writeInt(wonGames);
        dest.writeInt(side);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}