package com.lynx.sungka.server.actions;

import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.http.header.Header;
import com.lynx.sungka.server.http.route.Bind;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * A test endpoint, resets the database to its original, empty, state
 */
public class ResetDB extends Bind {
    @Override
    public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
        context.getDatabase().dropDatabase();
        return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.OK);
    }
}
