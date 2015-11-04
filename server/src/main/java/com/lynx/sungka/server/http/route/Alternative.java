package com.lynx.sungka.server.http.route;

import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.util.Tuple2;

import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Part of routing on the server.
 * This class denotes an option of multiple routing continuations.
 */
public class Alternative implements Route {

    private Route[] routes;

    public Alternative(Route... routes) {
        this.routes = routes;
    }

    @Override
    public Tuple2<RequestResponse, ParseState> matchRequest(ParseState state, ServerContext ctx, int idx) {
        for (int i = routes.length-1; i >= 0; i--) {
            state.push(new Tuple2<>(idx,routes[i]));
        }
        return new Tuple2<>(null,state);
    }
}
