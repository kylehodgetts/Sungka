package com.lynx.sungka.server;

import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.http.route.Bind;
import com.lynx.sungka.server.http.route.Route;
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
 *          <-INPUT DESC->
 */
public class Server implements Runnable {

    private ScheduledExecutorService pool;
    private ServerSocket socket;
    private Boolean running;
    private ServerContext context;
    private Route route;

    public Server() {
        pool = Executors.newScheduledThreadPool(5);
        running = true;
        context = new ServerContext();//TODO open db connections and stuff
        route = new Bind() {
            @Override
            public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
                return new RequestResponse(new ArrayList<>(),"<a>Hello</a>".getBytes(), RequestResponse.ResponseCode.OK);
            }
        };
        try {
            socket = new ServerSocket(8080);
            System.out.println("Starting server at: "+socket.getInetAddress().getCanonicalHostName());
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running){
            try {
                Socket s = socket.accept();

                BufferedReader input =
                        new BufferedReader(new InputStreamReader(s.
                                getInputStream()));
                DataOutputStream output =
                        new DataOutputStream(s.getOutputStream());

                pool.execute(new HandleRequest(route,input,output, context));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
