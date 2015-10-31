package com.lynx.sungka.server.http;

import com.lynx.sungka.server.http.header.Header;

import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class RequestResponse {

    private List<Header> headers;
    private byte[] content;
    private ResponseCode code;

    public RequestResponse(List<Header> headers, byte[] content, ResponseCode code) {
        this.headers = headers;
        this.content = content;
        this.code = code;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public byte[] getContent() {
        return content;
    }

    public ResponseCode getCode() {
        return code;
    }

    public enum ResponseCode{
        OK(200,"OK"),
        BAD_REQUEST(400,"Bad Request"),
        NOT_FOUND(404,"Not Found");

        private final int code;
        private final String vocal;
        ResponseCode(int code, String vocal) {
            this.code = code;
            this.vocal = vocal;
        }
        public int getValue() { return code; }

        public String getVocal() {
            return vocal;
        }
    }
}

