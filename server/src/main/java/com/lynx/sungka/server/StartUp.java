package com.lynx.sungka.server;

import com.mongodb.DBAddress;
import com.mongodb.Mongo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * The start up for the server
 */
public class StartUp {

    public static void main(String[] args){
        new Server();
    }
}
