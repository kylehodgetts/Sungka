package com.lynx.sungka.server.http.route;

import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.Request;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.util.Tuple2;
import com.lynx.sungka.server.util.Tuple3;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class Read implements Route {
    
    private String regEx;
    private Route next;

    @Override
    public Tuple2<RequestResponse, ParseState> matchRequest(ParseState state, ServerContext ctx, int idx) {
        if(state.getPath().length<idx && state.getPath()[idx].matches(regEx)){
            state.addArg(state.getPath()[idx]);
            state.push(new Tuple2<>(++idx,next));
        }
        return new Tuple2<>(null,state);
    }
}
