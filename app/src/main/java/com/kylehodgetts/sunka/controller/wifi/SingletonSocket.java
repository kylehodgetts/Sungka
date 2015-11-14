package com.kylehodgetts.sunka.controller.wifi;

import java.net.Socket;

/**
 * @author Kyle Hodgetts
 * @version 1.0
 * Returns a static socket that can be utilised anywhere where information needs to be
 * transferred between devices
 */
public class SingletonSocket {
    private static Socket socket;

    /* Private constructor */
    private SingletonSocket() {}

    /**
     * Returns a the set socket, if socket is null, returns a new socket
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
