package com.lynx.sungka.server;

import com.lynx.sungka.server.http.HTTPError;
import com.lynx.sungka.server.http.InvalidRequest;
import com.lynx.sungka.server.http.MethodType;
import com.lynx.sungka.server.http.NotSupportedMethod;
import com.lynx.sungka.server.http.Request;
import com.lynx.sungka.server.http.RequestResponse;
import com.lynx.sungka.server.http.header.ContentLength;
import com.lynx.sungka.server.http.header.ContentType;
import com.lynx.sungka.server.http.header.Header;
import com.lynx.sungka.server.http.header.MalformedHeader;
import com.lynx.sungka.server.http.route.ParseState;
import com.lynx.sungka.server.http.route.Route;
import com.lynx.sungka.server.util.Tuple2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Handling of one request to the server, this is to be run on own thread.
 * Matches the incoming requests route with the routing of the server, according to that
 * it generates a response to the request.
 *
 */
public class HandleRequest implements Runnable {

    private Route serverRoutes;
    private ServerContext context;
    private BufferedReader input;
    private DataOutputStream output;

    /**
     * Default constructor for handling of an incoming request
     * @param serverRoutes  The routing of the server
     * @param input         The input for the request, ie the contents of the request
     * @param output        The output for the response to the request
     * @param context       The server Context that allows access to server resources
     */
    public HandleRequest(Route serverRoutes, BufferedReader input, DataOutputStream output, ServerContext context) {
        this.serverRoutes = serverRoutes;
        this.input = input;
        this.output = output;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            Request req = parseRequest();
            RequestResponse res = bindRequest(req);
            sendResponse(res);
        } catch (HTTPError httpError) {
            httpError.printStackTrace();
            sendResponse(httpError.getResponse());
        }

    }

    /**
     * Builds and sends response to the request
     * @param response Response to be send
     */
    private void sendResponse(RequestResponse response) {
        try {
            //Build basic http 1.1 request
            String resp = "HTTP/1.1 "+response.getCode().getValue()+" "+response.getCode().getVerbal()+"\r\n";

            //Adds headers to the response
            for (Header header : response.getHeaders()) {
                resp+=header.getName()+": "+header.getValue()+"\r\n";
            }
            resp += "\r\n";

            //Adds the content of the response to the response
            byte[] res = connectArrays(resp.getBytes(), response.getContent());
            output.write(res);
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Tries to bind the request to a path on the server. If found the path is used to get the response
     * to this request
     * @param req   The request
     * @return
     */
    private RequestResponse bindRequest(Request req) {

        ParseState state = new ParseState(req, input);
        state.push(new Tuple2<>(0, serverRoutes));
        while (!state.isEmpty()){
            Tuple2<RequestResponse,ParseState> res = parseOne(state);
            if (res.getX() != null)
                return res.getX();
            state = res.getY();

        }
        return new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.NOT_FOUND);
    }


    /**
     * Parses the request and prepares for handling it
     * @return
     * @throws HTTPError
     */
    private Request parseRequest() throws HTTPError{
        try {
            String readLine = input.readLine();
            String[] split = readLine.split(" ");
            MethodType type = getMethod(split[0]);
            Map<String,Header> headers = new HashMap<>();
            while ((readLine = input.readLine()).length() != 0){
                Header res = parseHeader(readLine);
                headers.put(res.getName(),res);
            }
            return new Request(type,headers,split[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new InvalidRequest(new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST));
    }

    /**
     * Parses one header from the request
     * @param header The line with header in it
     * @return
     * @throws MalformedHeader
     */
    private Header parseHeader(String header) throws MalformedHeader {
        String[] split = header.split(":");
        if (split[0] == null || split[1] == null)
            throw new MalformedHeader(new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST));
        switch (split[0]){
            case "Content-Length": return new ContentLength(split[1].trim());
            case "Content-Type": return new ContentType(split[1].trim());
            default: return new Header(split[0],split[1].trim());
        }
    }

    /**
     * Gets the method type from the request
     * @param method
     * @return
     * @throws NotSupportedMethod
     */
    private MethodType getMethod(String method) throws NotSupportedMethod {
        switch (method){
            case "GET": return MethodType.GET;
            case "PUT": return MethodType.PUT;
            case "POST": return MethodType.POST;
            case "DELETE": return MethodType.DELETE;
            default: throw new NotSupportedMethod(new RequestResponse(new ArrayList<Header>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST));
        }
    }

    private Tuple2<RequestResponse,ParseState> parseOne(ParseState orig){
        Tuple2<Integer,Route> popped = orig.pop();
        System.out.println("Handlig: " + popped.getY().toString());
        return popped.getY().matchRequest(orig,context,popped.getX());
    }

    private byte[] connectArrays(byte[] ar1, byte[]ar2){
        byte[] res = new byte[ar1.length+ar2.length];
        System.arraycopy(ar1, 0, res, 0, ar1.length);
        System.arraycopy(ar2, 0, res, ar1.length, ar2.length);
        return res;
    }
}
