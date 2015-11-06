package com.lynx.sungka.server.http.route;

import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.util.Tuple2;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Part of routing on the server.
 * This class denotes a type of routing that matches its regex against current part of the URL
 * if it matches then its added to arguments that can be later on accessed when processing the
 * request
 */
public class Read implements Route {
    
    private String regex;
    private Route next;

    public Read(String regex, Route next) {
        this.regex = regex;
        this.next = next;
    }

    @Override
    public Tuple2<RequestResponse, ParseState> matchRequest(ParseState state, ServerContext ctx, int idx) {
        if(state.getPath().length>idx && state.getPath()[idx].matches(regex)){
            state.addArg(state.getPath()[idx],idx);
            state.push(new Tuple2<>(++idx,next));
        }
        return new Tuple2<>(null,state);
    }
}
