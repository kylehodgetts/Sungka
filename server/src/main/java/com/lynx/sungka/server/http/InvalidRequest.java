package com.lynx.sungka.server.http;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class InvalidRequest extends HTTPError {
    public InvalidRequest(RequestResponse response) {
        super(response);
    }
}
