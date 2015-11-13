package com.lynx.sungka.server.http;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * The Request was made with a method that is not supported
 */
public class NotSupportedMethod extends HTTPError {
    public NotSupportedMethod(RequestResponse response) {
        super(response);
    }
}
