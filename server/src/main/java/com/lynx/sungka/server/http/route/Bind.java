package com.lynx.sungka.server.http.route;

import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.util.Tuple2;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Part of routing on the server.
 * This denotes an end point for handling a request, must be at the end of each route.
 */
public abstract class Bind implements Route {

    public abstract RequestResponse run(ServerContext context,DBObject body,List<String> args);

    @Override
    public Tuple2<RequestResponse, ParseState> matchRequest(ParseState state, ServerContext ctx, int idx) {
        DBObject object = new BasicDBObject();
        RequestResponse response = null;
        if (idx >= state.getPath().length)
            response = run(ctx, object, state.getArgs());
        return new Tuple2<>(response,state);
    }
}
