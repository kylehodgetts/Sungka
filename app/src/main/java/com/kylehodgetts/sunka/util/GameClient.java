package com.kylehodgetts.sunka.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kylehodgetts.sunka.WiFiDirectActivity;
import com.kylehodgetts.sunka.controller.bus.EventBus;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by kylehodgetts on 05/11/2015.
 */
public class GameClient implements Runnable {

    public static final String GAME_CLIENT_SERVER_TAG = "Game Client Server";

    private EventBus bus;
    private Socket socket;


    public GameClient(EventBus bus) {
        this.bus = bus;
        this.socket = new Socket();
    }

    @Override
    public void run() {

        try {
            Log.d(WiFiDirectActivity.TAG, "Entered Client Thread");
            socket.bind(null);
            socket.connect(new InetSocketAddress(WiFiDirectActivity.PORT));
            Log.d(WiFiDirectActivity.TAG, "Client accepted");

           while(!Thread.currentThread().isInterrupted()) {
               SendingThread sendingThread = new SendingThread(socket);
               sendingThread.doInBackground(null);
               ReceivingThread receivingThread = new ReceivingThread(socket);
               //receivingThread.run();
           }


            socket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    
}
