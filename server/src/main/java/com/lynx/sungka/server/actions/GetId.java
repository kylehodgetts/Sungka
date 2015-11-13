package com.lynx.sungka.server.actions;

import com.lynx.sungka.server.Server;
import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.http.header.ContentLength;
import com.lynx.sungka.server.http.header.ContentType;
import com.lynx.sungka.server.http.header.Header;
import com.lynx.sungka.server.http.route.Bind;
import com.lynx.sungka.server.util.IDGen;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class GetId extends Bind {

    @Override
    public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
        //Save to db
        Object read = body.get(Server.USER_NAME);
        if (!(read instanceof String))
            return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST);

        String userName = (String)body.get(Server.USER_NAME);
        String serverId = IDGen.genId();
        DBObject nameId = new BasicDBObject(Server.USER_NAME,userName);
        nameId.put(Server.SERVER_ID,serverId);
        context.getNameCollection().insert(nameId);

        //Response
        byte[] resp = new BasicDBObject(Server.SERVER_ID, serverId).toString().getBytes();
        List<Header> headers = new ArrayList<>();
        headers.add(new ContentLength(resp.length));
        headers.add(ContentType.json());
        return new RequestResponse(headers, resp, RequestResponse.ResponseCode.OK);
    }
}
