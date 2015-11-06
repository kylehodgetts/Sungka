package com.kylehodgetts.sunka.util;

import android.os.AsyncTask;
import android.util.Log;

import com.kylehodgetts.sunka.WiFiDirectActivity;
import com.kylehodgetts.sunka.controller.bus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

/**
 * Created by CBaker on 05/11/2015.
 */
public class GamerServerAsyncTask extends AsyncTask {
    private EventBus bus;
    private ServerSocket serverSocket;
    private Socket client;
    private Socket server;


    public GamerServerAsyncTask(EventBus bus) {
        this.bus = bus;
        client = new Socket();
    }


    @Override
    protected Void doInBackground(Object ...params) {
        Log.d(WiFiDirectActivity.TAG, "Entered Server Thread");
        String read = null;

        try {
            serverSocket = new ServerSocket(WiFiDirectActivity.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!Thread.currentThread().isInterrupted()) {
            Log.d("GameServer", "do in background started");
            try{
                client = serverSocket.accept();
                Log.d(WiFiDirectActivity.TAG, "Server accepted");

                ReceivingThread receivingThread = new ReceivingThread(client);
                receivingThread.doInBackground(null);
                SendingThread sendingThread = new SendingThread(client);
                //sendingThread.run();

                Log.d("Server Recieve", receivingThread.get().toString());
                Log.d(WiFiDirectActivity.TAG, read);

            } catch (IOException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        try {
            serverSocket.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
