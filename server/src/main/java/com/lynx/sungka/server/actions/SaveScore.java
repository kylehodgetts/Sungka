package com.lynx.sungka.server.actions;

import com.lynx.sungka.server.Server;
import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.http.header.Header;
import com.lynx.sungka.server.http.route.Bind;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * An api end point that saves a supplied scores the to the database
 */
public class SaveScore extends Bind {
    /**
     * An end point to a call to the server, the response is build from the resources on the server and
     * the arguments given in the path.
     *
     * This request requires to have games won, games lost, max score and server id
     * in the json body of the request
     *
     * @param context   The server context, access to server resources
     * @param body      The body of the request
     * @param args      Arguments passed in path
     * @return          A server response to the request with no content code if success otherwise bad request
     */
    @Override
    public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
        Object won = body.get(Server.GAMES_WON);
        if (!(won instanceof Number))
            return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST);
        Integer wonp = ((Number) won).intValue();

        Object lost = body.get(Server.GAMES_LOST);
        if (!(lost instanceof Number))
            return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST);
        Integer lostp = ((Number) lost).intValue();

        Object score = body.get(Server.MAX_SCORE);
        if (!(score instanceof Number))
            return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST);
        Integer scorep = ((Number) score).intValue();

        Object id = body.get(Server.SERVER_ID);
        if (!(id instanceof String))
            return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST);
        String idp = (String) id;

        BasicDBObject record = new BasicDBObject(Server.SERVER_ID,idp);
        record.put(Server.MAX_SCORE,scorep);
        record.put(Server.GAMES_WON,wonp);
        record.put(Server.GAMES_LOST,lostp);

        context.getStatisticsCollection().update(new BasicDBObject(Server.SERVER_ID, idp), record, true, false);

        return new RequestResponse(new ArrayList<Header>(), new byte[]{}, RequestResponse.ResponseCode.NO_CONTENT);

    }
}
