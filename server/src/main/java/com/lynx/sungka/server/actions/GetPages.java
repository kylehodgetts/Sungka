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
 *          <-INPUT DESC->
 */
public class GetPages extends Bind {
    @Override
    public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
        byte[] resp = ((Math.round(context.getStatisticsCollection().getCount()/ Server.BATCH_SIZE))+"").getBytes();
        List<Header> headers = new ArrayList<>();
        headers.add(new ContentLength(resp.length));
        return new RequestResponse(headers, resp, RequestResponse.ResponseCode.OK);
    }
}
