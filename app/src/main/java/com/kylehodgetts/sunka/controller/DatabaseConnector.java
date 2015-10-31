package com.kylehodgetts.sunka.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class DatabaseConnector extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Sungka";

    private static final String USER = "user";
    private static final String HIGHSCORE = "highscore";
    private static final String WONGAMES = "wongames";


    private static final String DICTIONARY_TABLE_NAME = "dictionary";
    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + DICTIONARY_TABLE_NAME + " (" +
                    USER + "TEXT," +
                    HIGHSCORE + " NUMBER, " +
                    WONGAMES + " NUMBER);";




    public DatabaseConnector(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //DIE
    }

    private void doStuff(){
        getReadableDatabase();
    }
}
