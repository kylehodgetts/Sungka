package com.kylehodgetts.sunka.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kylehodgetts.sunka.WiFiDirectActivity;
import com.kylehodgetts.sunka.controller.bus.EventBus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Kyle Hodgetts
 * @author Charlie Baker
 */
public class GameServerAsyncTask extends AsyncTask {
    private Context context;
    private ServerSocket serverSocket;
    private Socket client;
    private EventBus bus;

    public GameServerAsyncTask(Context context, EventBus bus){
        this.context = context;
        this.bus = bus;
    }

    @Override
    protected Void doInBackground(Object[] params) {
        try{
            serverSocket = new ServerSocket(8888);
            client = serverSocket.accept();

            InputStream inputStream = client.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String read = bufferedReader.readLine();
            Log.d(WiFiDirectActivity.TAG, read);

            serverSocket.close();
            client.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
