package com.lynx.sungka.server.http.route;

import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.util.Tuple2;

import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class Alternative implements Route {

    private List<Route> routes;


    public List<Route> getRoutes() {
        return routes;
    }

    @Override
    public Tuple2<RequestResponse, ParseState> matchRequest(ParseState state, ServerContext ctx, int idx) {
        for (int i = routes.size()-1; i >= 0; i--) {
            state.push(new Tuple2<>(idx,routes.get(i)));
        }
        return new Tuple2<>(null,state);
    }
}
