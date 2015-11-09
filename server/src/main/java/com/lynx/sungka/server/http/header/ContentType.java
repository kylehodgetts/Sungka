package com.lynx.sungka.server.http.header;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * The type of content in this request/response
 */
public class ContentType extends Header {
    /**
     * Constructs a content type header
     * @param value the type of content
     */
    public ContentType(String value) {
        super("Content-Type", value);
    }

    /**
     * Checks whether the content of this request is a json
     * @return boolean whether the content is json
     */
    public boolean isJson(){
        return getValue().equals("application/json");
    }
}
