package com.kylehodgetts.sunka.util;

import android.content.Context;
import android.os.AsyncTask;

import com.kylehodgetts.sunka.controller.bus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by kylehodgetts on 05/11/2015.
 */
public class GameClientAsyncTask extends AsyncTask {

    private Context context;
    private EventBus bus;
    private Socket socket;


    public GameClientAsyncTask(Context context, EventBus bus) {
        this.context = context;
        this.bus = bus;
        this.socket = new Socket();
    }

    @Override
    protected Void doInBackground(Object[] params) {

        try {
            socket.bind(null);
            socket.connect(new InetSocketAddress(8888));

            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
