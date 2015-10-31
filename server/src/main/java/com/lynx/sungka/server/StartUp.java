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
 *          <-INPUT DESC->
 */
public class StartUp {

    public static void main(String[] args){
//        try {
////            ServerSocket s = new ServerSocket(8000);
////            System.out.println("Starting at: " + s.getInetAddress());
////            Socket ss = s.accept();
////            BufferedReader input =
////                    new BufferedReader(new InputStreamReader(ss.
////                            getInputStream()));
////            String line;
////            while ((line = input.readLine())!=null){
////                System.out.println(line);
////            }
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        new Server();


    }


}
