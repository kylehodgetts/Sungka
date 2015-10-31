package com.lynx.sungka.server.http.route;

import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.MethodType;
import com.lynx.sungka.server.http.Request;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.util.Tuple2;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class Method implements Route {
    private MethodType type;
    private Route next;

    @Override
    public Tuple2<RequestResponse, ParseState> matchRequest(ParseState state, ServerContext ctx, int idx) {
        if (state.getRequest().getType().equals(type)){
            state.push(new Tuple2<>(idx,next));
        }
        return new Tuple2<>(null,state);
    }
}
