package com.lynx.sungka.server.http;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class HTTPError extends Throwable {

    private RequestResponse response;

    public HTTPError(RequestResponse response) {
        this.response = response;
    }

    public RequestResponse getResponse() {
        return response;
    }
}
