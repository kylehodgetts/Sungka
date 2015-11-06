package com.kylehodgetts.sunka.util;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by CBaker on 05/11/2015.
 */
public class SendingThread extends AsyncTask {

    private Socket client;
    private PrintWriter writer;


    public SendingThread(Socket client) {
        this.client = client;

        try { writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true); }
        catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    public Void doInBackground(Object[] params) {
        while(!Thread.currentThread().isInterrupted()) {
            writer.println("Sending data to server test as a string");
            writer.flush();
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
