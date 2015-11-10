package com.kylehodgetts.sunka.controller.wifi;

import java.net.Socket;

/**
 * Created by kylehodgetts on 06/11/2015.
 */
public class SingletonSocket {
    private static Socket socket;

    public static synchronized Socket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(Socket socket){
        SingletonSocket.socket = socket;
    }
}
