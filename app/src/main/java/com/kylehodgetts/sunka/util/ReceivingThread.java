package com.kylehodgetts.sunka.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by CBaker on 05/11/2015.
 */
public class ReceivingThread extends AsyncTask {

    private Socket client;
    private BufferedReader bufferedReader;


    public ReceivingThread(Socket client) {
        this.client = client;

        try { bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));}
        catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public String doInBackground(Object[] params) {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                String readData = bufferedReader.readLine();
                Log.d("Recieving Thread", readData);    // Print to LogCat any data received.
                return readData;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
