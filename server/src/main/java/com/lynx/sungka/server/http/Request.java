package com.lynx.sungka.server.http;

import com.lynx.sungka.server.http.header.Header;
import com.lynx.sungka.server.http.route.Route;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * An representation of a request that was made to the server
 */
public class Request {

    private MethodType type;
    private Map<String,Header> headers;
    private String route;

    /**
     * Constructs a representation of a request to the s server
     * @param type      Method type of the request
     * @param headers   The headers of the request
     * @param route     The path of the request on the server
     */
    public Request(MethodType type, Map<String,Header> headers, String route) {
        this.type = type;
        this.headers = headers;
        this.route = route;
    }

    /**
     * Getter for the route on the server
     * @return String, the route on the server
     */
    public String getRoute() {
        return route;
    }

    /**
     * Getter for the headers of the request
     * @return Map with all the headers of this request
     */
    public Map<String,Header> getHeaders() {
        return headers;
    }

    /**
     * Gets the method type of this request
     * @return the method type of the request
     */
    public MethodType getType() {
        return type;
    }
}

