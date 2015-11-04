package com.lynx.sungka.server.http.route;

import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.util.Tuple2;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Part of routing on the server.
 * This denotes a exact given string that is supposed to be there, then continues on with the path
 */
public class Segment implements Route {

    private Route next;
    private String segment;

    public Segment(String segment,Route next) {
        this.next = next;
        this.segment = segment;
    }

    @Override
    public Tuple2<RequestResponse, ParseState> matchRequest(ParseState state, ServerContext ctx, int idx) {
        if(state.getPath().length>idx && state.getPath()[idx].equals(segment)){
            state.push(new Tuple2<>(++idx,next));
        }
        return new Tuple2<>(null,state);
    }
}
