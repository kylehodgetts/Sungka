package com.lynx.sungka.server.http.route;

import com.lynx.sungka.server.ServerContext;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.util.Tuple2;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * One step in the parsing of the request route
 */
public interface Route {

    /**
     * Matches a request and current path progression, from there it decides how to modify the parse state
     * and or whether there is a response to be returned to the request
     * @param state Current parse state
     * @param ctx   Current context of the server
     * @param idx   Current depth of the path of the request
     * @return      A tuple with the response from this route segment and modified parse state
     *              if the response is present that stops further matching
     */
    Tuple2<RequestResponse,ParseState> matchRequest(ParseState state, ServerContext ctx, int idx);
}
