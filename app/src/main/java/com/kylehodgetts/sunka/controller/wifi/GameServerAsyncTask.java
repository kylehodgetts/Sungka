package com.kylehodgetts.sunka.controller.wifi;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by kylehodgetts on 03/11/2015.
 */
public class GameServerAsyncTask extends AsyncTask{

    @Override
    protected Void doInBackground(Object[] params) {
        try{
            ServerSocket serverSocket = new ServerSocket(8888);
            while(true) {
                serverSocket.accept();

            }
        }
        catch(IOException e){

        }
        return null;
    }
}
