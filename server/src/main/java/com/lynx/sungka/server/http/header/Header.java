package com.lynx.sungka.server.http.header;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * A header of a http request
 */
public class Header {

    private String name;
    private String value;

    /**
     * Constructor for the header
     * @param name  The name of the header
     * @param value The value of the header
     */
    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Getter for the name of the header
     * @return String, the name of the header
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the value of the header
     * @return String, the value of the header
     */
    public String getValue() {
        return value;
    }
}
