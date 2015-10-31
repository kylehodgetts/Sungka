package com.lynx.sungka.server.http.header;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class ContentType extends Header {
    public ContentType(String name, String value) {
        super(name, value);
    }

    public boolean isJson(){
        return getValue().equals("application/json");
    }
}
