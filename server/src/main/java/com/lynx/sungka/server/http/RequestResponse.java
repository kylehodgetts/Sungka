package com.lynx.sungka.server.http;

import com.lynx.sungka.server.http.header.Header;

import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * An response to a request to the server, contains all necessary headers and the contents of the
 * response.
 */
public class RequestResponse {

    private List<Header> headers;
    private byte[] content;
    private ResponseCode code;

    /**
     * Constructs a response to the request to the server
     * @param headers   The headers of the response
     * @param content   The content of the response in bytes
     * @param code      The Response code of this response
     */
    public RequestResponse(List<Header> headers, byte[] content, ResponseCode code) {
        this.headers = headers;
        this.content = content;
        this.code = code;
    }

    /**
     * Getter for the headers of this response
     * @return List of all headers
     */
    public List<Header> getHeaders() {
        return headers;
    }

    /**
     * Getter of the content of this response
     * @return array of bytes that contain the content
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * The code of this response
     * @return The ResponseCode that has been gicen to this response
     */
    public ResponseCode getCode() {
        return code;
    }

    public enum ResponseCode{
        OK(200,"OK"),
        NO_CONTENT(204,"No Content"),
        BAD_REQUEST(400,"Bad Request"),
        NOT_FOUND(404,"Not Found");

        private final int code;
        private final String verbal;

        /**
         * Constructs a Response code
         * @param code      The code for this response
         * @param verbal    The description of this response
         */
        ResponseCode(int code, String verbal) {
            this.code = code;
            this.verbal = verbal;
        }

        /**
         * Gets the integer value of this response code
         * @return the integer value of response code
         */
        public int getValue() { return code; }

        /**
         * Gets the short verbal description of the code
         * @return verbal description of the code
         */
        public String getVerbal() {
            return verbal;
        }
    }
}

