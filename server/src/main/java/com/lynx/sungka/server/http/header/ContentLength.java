package com.lynx.sungka.server.http.header;

import com.lynx.sungka.server.http.RequestResponse;

import java.util.ArrayList;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * A header that denotes the length of the content of the request/response, it denotes the number of octets
 */
public class ContentLength extends Header{

    private int length;

    /**
     * Constructs the Content length header
     * @param value the value of the header
     * @throws MalformedHeader
     */
    public ContentLength(String value) throws MalformedHeader{
        super("Content-Length", value);
        try{
            length = Integer.parseInt(value);
        }catch (Exception e){
            throw new MalformedHeader(new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST));
        }
    }

    public ContentLength(int value){
        super("Content-Length",value+"");
        length = value;
    }

    /**
     * Gets the length that is in this content length header
     * @return
     */
    public int getLength() {
        return length;
    }
}
