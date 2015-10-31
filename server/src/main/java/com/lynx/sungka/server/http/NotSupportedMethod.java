package com.lynx.sungka.server.http;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class NotSupportedMethod extends HTTPError {
    public NotSupportedMethod(RequestResponse response) {
        super(response);
    }
}
