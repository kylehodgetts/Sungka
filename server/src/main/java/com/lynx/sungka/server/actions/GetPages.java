package com.lynx.sungka.server.actions;

import com.lynx.sungka.server.Server;
import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.http.header.ContentLength;
import com.lynx.sungka.server.http.header.Header;
import com.lynx.sungka.server.http.route.Bind;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * An api end point for getting the number of pages in the statistics
 */
public class GetPages extends Bind {

    /**
     * An end point to a call to the server, the response is build from the resources on the server and
     * the arguments given in the path.
     * @param context   The server context, access to server resources
     * @param body      The body of the request
     * @param args      Arguments passed in path
     * @return          A server response to the request with string that contains the number of pages of high score statistics
     */
    @Override
    public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
        byte[] resp = ((Math.round(context.getStatisticsCollection().getCount()/ (float)Server.BATCH_SIZE))+"").getBytes();
        List<Header> headers = new ArrayList<>();
        headers.add(new ContentLength(resp.length));
        return new RequestResponse(headers, resp, RequestResponse.ResponseCode.OK);
    }
}
