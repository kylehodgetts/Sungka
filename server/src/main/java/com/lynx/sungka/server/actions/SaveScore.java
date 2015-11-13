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
 *          <-INPUT DESC->
 */
public class SaveScore extends Bind {
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
