package com.lynx.sungka.server.http.route;

import com.lynx.sungka.server.http.Request;
import com.lynx.sungka.server.util.Tuple2;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class ParseState{
    private Stack<Tuple2<Integer,Route>> routes;
    private String[] path;
    private List<String> args;
    private final Request request;
    private BufferedReader body;

    public ParseState(Request request, String[] path,BufferedReader body) {
        this.request = request;
        this.path = path;
        this.routes = new Stack<>();
        this.args = new ArrayList<>();
        this.body = body;
    }

    public Stack<Tuple2<Integer, Route>> getRoutes() {
        return routes;
    }

    public String[] getPath() {
        return path;
    }

    public Request getRequest() {
        return request;
    }

    public Tuple2<Integer,Route> pop(){
        return routes.pop();
    }

    public void push(Tuple2<Integer,Route> add){
        routes.push(add);
    }

    public List<String> getArgs() {
        return args;
    }

    public void addArg(String add){
        args.add(add);
    }

    public boolean isEmpty(){
        return routes.isEmpty();
    }

    public BufferedReader getBody() {
        return body;
    }
}
