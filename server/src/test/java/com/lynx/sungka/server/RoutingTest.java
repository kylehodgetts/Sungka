package com.lynx.sungka.server;

import com.lynx.sungka.server.http.MethodType;
import com.lynx.sungka.server.http.Request;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.http.header.Header;
import com.lynx.sungka.server.http.route.Alternative;
import com.lynx.sungka.server.http.route.Bind;
import com.lynx.sungka.server.http.route.Method;
import com.lynx.sungka.server.http.route.ParseState;
import com.lynx.sungka.server.http.route.Read;
import com.lynx.sungka.server.http.route.Route;
import com.lynx.sungka.server.http.route.Segment;
import com.lynx.sungka.server.util.Tuple2;
import com.mongodb.DBObject;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Testing the routing algorithm
 */
public class RoutingTest {


    /**
     * Test whether the segment type of route works
     */
    @Test
    public void testSegment(){
        Request r = new Request(MethodType.GET,new HashMap<String,Header>(),"/test");
        ParseState state = new ParseState(r,null);
        final RequestResponse wantedResp = new RequestResponse(new ArrayList<Header>(), new byte[]{}, RequestResponse.ResponseCode.OK);
        Route route = new Segment("test", new Bind() {
            @Override
            public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
                return wantedResp;
            }
        });

        RequestResponse resp = simulateMatching(route,state);

        assertEquals(wantedResp,resp);

    }

    /**
     * Test whether we can default to no response found
     */
    @Test
    public void testNotFound(){
        Request r = new Request(MethodType.GET,new HashMap<String,Header>(),"/test/next");
        ParseState state = new ParseState(r,null);

        Route route = new Segment("another", new Bind() {
            @Override
            public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
                return new RequestResponse(new ArrayList<Header>(), new byte[]{}, RequestResponse.ResponseCode.OK);
            }
        });

        RequestResponse resp = simulateMatching(route,state);

        assertEquals(null,resp);

    }

    /**
     * Test whether the method type of route works
     */
    @Test
    public void testMethod(){
        Request r = new Request(MethodType.GET,new HashMap<String,Header>(),"");
        ParseState state = new ParseState(r,null);
        final RequestResponse wantedResp = new RequestResponse(new ArrayList<Header>(), new byte[]{}, RequestResponse.ResponseCode.OK);

        Route route = new Method(MethodType.GET, new Bind() {
            @Override
            public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
                return wantedResp;
            }
        });
        RequestResponse resp = simulateMatching(route,state);

        state = new ParseState(new Request(MethodType.POST,new HashMap<String,Header>(),""),null);
        RequestResponse noResp = simulateMatching(route,state);

        assertEquals(wantedResp,resp);
        assertEquals(null,noResp);
    }

    /**
     * Test whether the alternative type of route works
     */
    @Test
    public void testAlternative(){
        final RequestResponse wantedResp1 = new RequestResponse(new ArrayList<Header>(), new byte[]{}, RequestResponse.ResponseCode.OK);
        final RequestResponse wantedResp2 = new RequestResponse(new ArrayList<Header>(), new byte[]{}, RequestResponse.ResponseCode.OK);

        Route route = new Alternative(
            new Segment("test", new Bind() {
                @Override
                public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
                    return wantedResp1;
                }
            })

            ,new Method(MethodType.GET, new Bind() {
                @Override
                public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
                    return wantedResp2;
                }
            })
        );

        ParseState state = new ParseState(new Request(MethodType.GET,new HashMap<String,Header>(),"test"),null);
        RequestResponse resp1 = simulateMatching(route,state);

        state = new ParseState(new Request(MethodType.GET,new HashMap<String,Header>(),""),null);
        RequestResponse resp2 = simulateMatching(route,state);

        assertEquals(wantedResp1,resp1);
        assertEquals(wantedResp2,resp2);
    }

    /**
     * Test whether the read type of route works
     */
    @Test
    public void testRead(){
        Request r = new Request(MethodType.GET,new HashMap<String,Header>(),"helo");
        ParseState state = new ParseState(r,null);
        final RequestResponse wantedResp = new RequestResponse(new ArrayList<Header>(), new byte[]{}, RequestResponse.ResponseCode.OK);

        Route route = new Read("hel?lo", new Bind() {
            @Override
            public RequestResponse run(ServerContext context, DBObject body, List<String> args) {
                assertEquals("helo",args.get(0));
                return wantedResp;
            }
        });
        RequestResponse resp = simulateMatching(route,state);

        assertEquals(wantedResp,resp);
    }

    private RequestResponse simulateMatching(Route route, ParseState state){
        state.push(new Tuple2<>(0, route));
        while (!state.isEmpty()){
            Tuple2<Integer,Route> popped = state.pop();

            Tuple2<RequestResponse,ParseState> res = popped.getY().matchRequest(state,null,popped.getX());
            if (res.getX() != null){
                return res.getX();
            }
            state = res.getY();

        }
        return null;
    }
}
