package com.lynx.sungka.server.http.header;

import com.lynx.sungka.server.http.HTTPError;
import com.lynx.sungka.server.http.RequestResponse;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class MalformedHeader extends HTTPError {
    public MalformedHeader(RequestResponse response) {
        super(response);
    }
}
