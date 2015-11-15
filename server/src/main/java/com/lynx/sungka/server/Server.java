package com.lynx.sungka.server;

import com.lynx.sungka.server.actions.GetId;
import com.lynx.sungka.server.actions.GetPages;
import com.lynx.sungka.server.actions.GetScores;
import com.lynx.sungka.server.actions.PopulateMockData;
import com.lynx.sungka.server.actions.ResetDB;
import com.lynx.sungka.server.actions.SaveScore;
import com.lynx.sungka.server.http.MethodType;
import com.lynx.sungka.server.http.route.Alternative;
import com.lynx.sungka.server.http.route.Method;
import com.lynx.sungka.server.http.route.Read;
import com.lynx.sungka.server.http.route.Route;
import com.lynx.sungka.server.http.route.Segment;
import com.mongodb.DBAddress;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Basic REST server that handles incoming requests. While giving accesses to its resources
 * while handling those requests
 * Is based on ExecutorService with 5 threads opened at any given time. As the request are made
 * to the server, they are being submitted to this Service and executed at some point.
 */
public class Server implements Runnable {

    public static final String USER_NAME ="UserName";
    public static final String SERVER_ID ="ServerID";
    public static final String GAMES_WON ="GamesWon";
    public static final String GAMES_LOST ="GamesLost";
    public static final String MAX_SCORE ="MaxScore";

    public static final int BATCH_SIZE = 40;

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

        route =
            new Alternative(
                new Segment("user",new Alternative(
                    new Segment("id", new Method(MethodType.POST,new GetId()))
                )),
                new Segment("statistics", new Alternative(
                    new Method(MethodType.POST, new SaveScore()),
                    new Method(MethodType.GET,new Read("\\d+", new Read("\\d" ,new GetScores()))), //Reads the two path parameters that are required for GetScores
                    new Segment("pages", new GetPages()),
                    new Segment("mock",new PopulateMockData())
                )),
                new Segment("resetdb",new ResetDB())
            );


        try {
            //Creating server context, access to all its resources
            context = new ServerContext(new DBAddress("127.0.0.1",27017,"local"));
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
