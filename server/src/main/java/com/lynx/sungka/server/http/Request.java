package com.lynx.sungka.server.http;

import com.lynx.sungka.server.http.header.Header;
import com.lynx.sungka.server.http.route.Route;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class Request {

    private MethodType type;
    private Map<String,Header> headers;
    private String route;

    public Request(MethodType type, Map<String,Header> headers, String route) {
        this.type = type;
        this.headers = headers;
        this.route = route;
    }

    public String getRoute() {
        return route;
    }

    public Map<String,Header> getHeaders() {
        return headers;
    }

    public MethodType getType() {
        return type;
    }
}

