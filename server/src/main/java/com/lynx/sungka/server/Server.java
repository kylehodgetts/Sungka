package com.lynx.sungka.server;

import com.lynx.sungka.server.http.MethodType;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.http.header.ContentLength;
import com.lynx.sungka.server.http.header.ContentType;
import com.lynx.sungka.server.http.header.Header;
import com.lynx.sungka.server.http.route.Alternative;
import com.lynx.sungka.server.http.route.Bind;
import com.lynx.sungka.server.http.route.Method;
import com.lynx.sungka.server.http.route.Read;
import com.lynx.sungka.server.http.route.Route;
import com.lynx.sungka.server.http.route.Segment;
import com.lynx.sungka.server.util.IDGen;
import com.lynx.sungka.server.util.Tuple2;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBAddress;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    public static final String GAME_WON ="GameWon";
    public static final String MAX_SCORE ="MaxScore";


    public static final int BATCH_SIZE = 10;

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
        route =
            new Alternative(
                new Segment("user",new Alternative(
                    new Segment("id", new Method(MethodType.POST,new Bind() {
                        @Override
                        public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
                            //Save to db
                            Object read = body.get(USER_NAME);
                            if (!(read instanceof String))
                                return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST);

                            String userName = (String)body.get(USER_NAME);
                            String serverId = IDGen.genId();
                            DBObject nameId = new BasicDBObject(USER_NAME,userName);
                            nameId.put(SERVER_ID,serverId);
                            context.getNameCollection().insert(nameId);

                            //Response
                            byte[] resp = new BasicDBObject(SERVER_ID, serverId).toString().getBytes();
                            List<Header> headers = new ArrayList<>();
                            headers.add(new ContentLength(resp.length));
                            headers.add(ContentType.json());
                            return new RequestResponse(headers, resp, RequestResponse.ResponseCode.OK);
                        }
                    }))
                )),
                new Segment("statistics", new Alternative(
                    new Method(MethodType.POST, new Bind() {
                        @Override
                        public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
                            Object won = body.get(GAME_WON);
                            if (!(won instanceof Boolean))
                                return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST);
                            Boolean wonp = (Boolean) won;

                            Object score = body.get(MAX_SCORE);
                            if (!(score instanceof Number))
                                return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST);
                            Integer scorep = ((Number) score).intValue();

                            Object id = body.get(SERVER_ID);
                            if (!(id instanceof String))
                                return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST);
                            String idp = (String) id;

                            DBObject record = context.getStatisticsCollection().findOne(new BasicDBObject(SERVER_ID, idp));
                            if (record != null){
                                if (wonp)
                                    record.put(GAMES_WON,((Number)record.get(GAMES_WON)).intValue()+1);
                                int maxScore = ((Number)record.get(MAX_SCORE)).intValue();
                                if (maxScore<scorep)
                                    record.put(MAX_SCORE,scorep);
                            } else {
                                record = new BasicDBObject(SERVER_ID,idp);
                                record.put(MAX_SCORE,scorep);
                                record.put(GAMES_WON,1);
                            }

                            context.getStatisticsCollection().update(new BasicDBObject(SERVER_ID, idp), record, true, false);

                            return new RequestResponse(new ArrayList<Header>(), new byte[]{}, RequestResponse.ResponseCode.NO_CONTENT);
                        }
                    }),
                    new Method(MethodType.GET,new Read("\\d+", new Read("\\d" ,new Bind() {
                        @Override
                        public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
                            try{
                                int page = Integer.parseInt(args.get(0));
                                int type = Integer.parseInt(args.get(1));
                                DBCursor cursor;
                                switch (type){
                                    case 0:
                                        cursor = context.getStatisticsCollection().find().sort(new BasicDBObject(GAMES_WON, -1));
                                        break;
                                    case 1:
                                        cursor = context.getStatisticsCollection().find().sort(new BasicDBObject(GAMES_WON, 1));
                                        break;
                                    case 2:
                                        cursor = context.getStatisticsCollection().find().sort(new BasicDBObject(MAX_SCORE, -1));
                                        break;
                                    case 3:
                                        cursor = context.getStatisticsCollection().find().sort(new BasicDBObject(MAX_SCORE, 1));
                                        break;
                                    default:
                                        return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST);
                                }

                                List<DBObject> objects = new ArrayList<>();
                                for (DBObject object:cursor.skip(page*BATCH_SIZE).batchSize(BATCH_SIZE)){
                                    objects.add(object);
                                }
                                
                                byte[] resp = JSON.serialize(objects).getBytes();
                                List<Header> headers = new ArrayList<>();
                                headers.add(new ContentLength(resp.length));
                                headers.add(ContentType.json());
                                return new RequestResponse(headers, resp, RequestResponse.ResponseCode.OK);
                            }catch (Exception ex){
                                return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST);
                            }
                        }
                    })))
                )),
                new Segment("debug", new Alternative(
                    new Segment("names",new Method(MethodType.GET, new Bind() {
                        @Override
                        public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
                            List<DBObject> objects = new ArrayList<>();
                            for (DBObject object : context.getNameCollection().find()) {
                                objects.add(object);
                            }
                            byte[] resp = JSON.serialize(objects).getBytes();
                            List<Header> headers = new ArrayList<>();
                            headers.add(new ContentLength(resp.length));
                            headers.add(ContentType.json());
                            return new RequestResponse(headers, resp, RequestResponse.ResponseCode.OK);
                        }
                    })),
                    new Segment("stats",new Method(MethodType.GET, new Bind() {
                        @Override
                        public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
                            List<DBObject> objects = new ArrayList<>();
                            for (DBObject object : context.getStatisticsCollection().find()) {
                                objects.add(object);
                            }
                            byte[] resp = JSON.serialize(objects).getBytes();
                            List<Header> headers = new ArrayList<>();
                            headers.add(new ContentLength(resp.length));
                            headers.add(ContentType.json());
                            return new RequestResponse(headers, resp, RequestResponse.ResponseCode.OK);
                        }
                    }))
                ))
            );


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
