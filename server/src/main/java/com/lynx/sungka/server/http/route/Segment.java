package com.lynx.sungka.server.http.route;

import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.util.Tuple2;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class Segment implements Route {

    private Route next;
    private String segment;


    @Override
    public Tuple2<RequestResponse, ParseState> matchRequest(ParseState state, ServerContext ctx, int idx) {
        if(state.getPath().length<idx && state.getPath()[idx].equals(segment)){
            state.addArg(state.getPath()[idx]);
            state.push(new Tuple2<>(++idx,next));
        }
        return new Tuple2<>(null,state);
    }
}
