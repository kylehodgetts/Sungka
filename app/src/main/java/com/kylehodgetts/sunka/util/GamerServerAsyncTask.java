package com.kylehodgetts.sunka.util;

import android.os.AsyncTask;
import android.util.Log;

import com.kylehodgetts.sunka.controller.bus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by CBaker on 05/11/2015.
 */
public class GamerServerAsyncTask extends AsyncTask {
    private EventBus bus;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private Socket server;


    @Override
    protected Object doInBackground(Object[] params) {
        try{
            serverSocket = new ServerSocket(8888);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        clientSocket = serverSocket.accept();
                        InputStream inputStream = clientSocket.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                        Log.d("Client Recieving Thread", reader.readLine());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
