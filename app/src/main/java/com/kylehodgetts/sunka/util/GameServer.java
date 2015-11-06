package com.kylehodgetts.sunka.util;

import android.content.Context;
import android.util.Log;

import com.kylehodgetts.sunka.WiFiDirectActivity;
import com.kylehodgetts.sunka.controller.bus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Kyle Hodgetts
 * @author Charlie Baker
 */
public class GameServer implements Runnable {
    private ServerSocket serverSocket;
    private Socket client;
    private EventBus bus;

    public GameServer(EventBus bus){
        this.bus = bus;
    }

    @Override
    public void run() {
        Log.d(WiFiDirectActivity.TAG, "Entered Server Thread");
        String read = null;

        try {
            serverSocket = new ServerSocket(WiFiDirectActivity.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(!Thread.currentThread().isInterrupted()) {
            try{
                client = serverSocket.accept();
                Log.d(WiFiDirectActivity.TAG, "Server accepted");

                ReceivingThread receivingThread = new ReceivingThread(client);
                receivingThread.doInBackground(null);
                SendingThread sendingThread = new SendingThread(client);
                //sendingThread.run();

                Log.d(WiFiDirectActivity.TAG, read);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            serverSocket.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void setSocket(Socket socket) throws IOException {
        socket = serverSocket.accept();
    }

}
