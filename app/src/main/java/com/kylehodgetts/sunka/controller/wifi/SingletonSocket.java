package com.kylehodgetts.sunka.controller.wifi;

import java.net.Socket;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Returns a static socket that can be utilised anywhere where information needs to be
 * tranferred between devices
 */
public class SingletonSocket {
    private static Socket socket;


    private SingletonSocket() {}

    /**
     *
     * @return the game socket
     */
    public static synchronized Socket getSocket(){
        if(socket == null) {
            setSocket(new Socket());
        }
        return socket;
    }

    /**
     * Update the game socket
     * @param socket new game socket
     */
    public static synchronized void setSocket(Socket socket){
        SingletonSocket.socket = socket;
    }
}
