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
 * An end point for getting id and storing the user name into database
 */
public class GetId extends Bind {

    /**
     * An end point to a call to the server, the response is build from the resources on the server and
     * the arguments given in the path.
     *
     * This end point requires to have the username in request body. In form of JSON
     *
     * @param context   The server context, access to server resources
     * @param body      The body of the request
     * @param args      Arguments passed in path
     * @return          A server response to the request with userID in its body in form of json
     */
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
