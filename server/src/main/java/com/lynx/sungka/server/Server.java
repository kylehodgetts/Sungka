package com.lynx.sungka.server;

import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.http.header.Header;
import com.lynx.sungka.server.http.route.Bind;
import com.lynx.sungka.server.http.route.Route;
import com.lynx.sungka.server.http.route.Segment;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Basic REST server that handles incoming requests. While giving accesses to its resources
 * while handling those requests
 * Is based on ExecutorService with 5 threads opened at any given time. As the request are made
 * to the server, they are being submitted to this Service and executed at some point.
 */
public class Server implements Runnable {

    private ScheduledExecutorService pool;
    private ServerSocket socket;
    private Boolean running;
    private ServerContext context;
    private Route route;


    /**
     * Default constructor that creates a server
     */
    public Server() {
        pool = Executors.newScheduledThreadPool(5);
        running = true;

        // TODO: implement proper routing construction, for now just create routes with this
        route = new Segment("me",new Bind() {
            @Override
            public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
                ArrayList<Header> headers = new ArrayList<>();
                headers.add(new Header("Server","Simple rest server"));
                headers.add(new Header("Content-Type", "text/html"));
                DB tDB = context.getMongo().getDB("test");
                DBObject o = new BasicDBObject("Hello","Yeah");
                tDB.getCollection("myCollection").insert(o);
                return new RequestResponse(headers,"<a>Hello</a>".getBytes(), RequestResponse.ResponseCode.OK);
            }
        });
        try {
            //Creating server context, access to all its resources
            context = new ServerContext(new DBAddress("localhost",27017,"local"));
            socket = new ServerSocket(8080);
            System.out.println("Starting server at: "+socket.getInetAddress().getHostName());
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running){
            try {
                //Accepts connection
                Socket s = socket.accept();

                //Creates output and input for the connection
                BufferedReader input =new BufferedReader(new InputStreamReader(s.getInputStream()));
                DataOutputStream output =new DataOutputStream(s.getOutputStream());

                //Handles the connection at some point
                pool.execute(new HandleRequest(route,input,output, context));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
