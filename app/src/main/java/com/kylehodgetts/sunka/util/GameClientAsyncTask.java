package com.kylehodgetts.sunka.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kylehodgetts.sunka.controller.bus.EventBus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by CBaker on 05/11/2015.
 */
public class GameClientAsyncTask extends AsyncTask{

    private EventBus bus;
    private Socket socket;


    public GameClientAsyncTask(EventBus bus) {
        this.bus = bus;
        socket = new Socket();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        try {
            socket.bind(null);
            socket.connect(new InetSocketAddress(8888), 500);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = socket.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));


                        Log.d("Client Recieving Thread", reader.readLine() );

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OutputStream outputStream = socket.getOutputStream();

                        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)), true);
                        writer.println("testing this shit works!!!!!");
                        writer.flush();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
