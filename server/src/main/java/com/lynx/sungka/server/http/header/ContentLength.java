package com.lynx.sungka.server.http.header;

import com.lynx.sungka.server.http.RequestResponse;

import java.util.ArrayList;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class ContentLength extends Header{

    private int length;

    public ContentLength(String name, String value) throws MalformedHeader{
        super(name, value);
        try{
            length = Integer.parseInt(value);
        }catch (Exception e){
            throw new MalformedHeader(new RequestResponse(new ArrayList<>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST));
        }
    }

    public int getLength() {
        return length;
    }
}
