package com.kylehodgetts.sunka.util;

import android.os.AsyncTask;
import android.util.Log;

import com.kylehodgetts.sunka.WiFiDirectActivity;
import com.kylehodgetts.sunka.controller.bus.EventBus;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by CBaker on 05/11/2015.
 */
public class GameClientAsyncTask extends AsyncTask {

    private EventBus bus;
    private Socket socket;
    private InetAddress host;


    public GameClientAsyncTask(EventBus bus, final InetAddress host) {
        this.bus = bus;
        this.host = host;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket();
                    //socket.bind(null);
                    socket.connect(new InetSocketAddress(host, WiFiDirectActivity.PORT), 1000);

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected Void doInBackground(Object... params) {

        try {
            Log.d(WiFiDirectActivity.TAG, "Entered Client Thread");
            socket.connect(new InetSocketAddress(WiFiDirectActivity.PORT));
            Log.d(WiFiDirectActivity.TAG, "Client accepted");

            while (!Thread.currentThread().isInterrupted()) {
                SendingThread sendingThread = new SendingThread(socket);
                sendingThread.doInBackground(null);
                ReceivingThread receivingThread = new ReceivingThread(socket);
                //receivingThread.run();
            }


            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
