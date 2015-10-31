package com.lynx.sungka.server.http.route;

import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.Request;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.util.Tuple2;
import com.lynx.sungka.server.util.Tuple3;

import java.util.List;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public interface Route {
    
    Tuple2<RequestResponse,ParseState> matchRequest(ParseState state, ServerContext ctx, int idx);
}
