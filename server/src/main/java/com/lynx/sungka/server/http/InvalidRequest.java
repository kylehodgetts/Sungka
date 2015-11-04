package com.lynx.sungka.server.http;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Denotes an error while parsing the request
 */
public class InvalidRequest extends HTTPError {
    public InvalidRequest(RequestResponse response) {
        super(response);
    }
}
