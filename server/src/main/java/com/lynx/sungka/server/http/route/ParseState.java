package com.lynx.sungka.server.http.route;

import com.lynx.sungka.server.http.Request;
import com.lynx.sungka.server.util.Tuple2;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * State of parsing for a given request.
 */
public class ParseState{
    private Stack<Tuple2<Integer,Route>> routes;
    private String[] path;
    private Stack<Tuple2<Integer,String>> args;
    private final Request request;
    private BufferedReader body;

    /**
     * Default constructor, splits the path of the request, so that we can match over it
     * @param request   The request tha tis being processed
     * @param body      An input for the body of the request
     */
    public ParseState(Request request,BufferedReader body) {
        String absPath = request.getRoute().trim();
        if (absPath.startsWith("/"))
            absPath = absPath.substring(1);

        List<String> path = new ArrayList<>();
        String[] splitted = absPath.split("/");
        for (int i = 0; i < splitted.length; i++) {
            if (!splitted[i].equals(""))
                path.add(splitted[i]);
        }

        this.request = request;
        this.path = path.toArray(new String[path.size()]);
        this.routes = new Stack<>();
        this.args = new Stack<>();
        this.body = body;
    }

    /**
     * Getter for the segmented path of the request
     * @return
     */
    public String[] getPath() {
        return path;
    }

    /**
     * Getter for the current request
     * @return
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Pops a route from the stack of the routes to be processed
     * @return
     */
    public Tuple2<Integer,Route> pop(){
        return routes.pop();
    }

    /**
     * Pushes a new route to be processed at some point onto the stack
     * @param add Route to be added, together with the depth from where the route comes
     */
    public void push(Tuple2<Integer,Route> add){
        routes.push(add);
        if (!args.isEmpty()) {
            Tuple2<Integer, String> peeked = args.peek();
            while (peeked.getX() > add.getX()) {
                args.pop();
                peeked = args.peek();
            }
        }
    }

    /**
     * Getter for the arguments ie the values that were "Read" from the path
     * @return
     */
    public List<String> getArgs() {
        return args.stream().map(Tuple2::getY).collect(Collectors.toList());
    }

    /**
     * Adds an argument to current read path
     * @param add value read from the path
     * @param idx the depth where the value was read
     */
    public void addArg(String add, int idx){
        args.add(new Tuple2<>(idx, add));
    }

    /**
     * Whether there is a remaining route to match agains
     * @return
     */
    public boolean isEmpty(){
        return routes.isEmpty();
    }

    /**
     * Getter for a stream of the body of the request
     * @return
     */
    public BufferedReader getBody() {
        return body;
    }
}
