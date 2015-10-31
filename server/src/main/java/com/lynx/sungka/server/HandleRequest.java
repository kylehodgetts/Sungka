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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Adam Chlupacek
 * @version 1.0
 *          <-INPUT DESC->
 */
public class HandleRequest implements Runnable {

    private Route route;
    private ServerContext context;
    private BufferedReader input;
    private DataOutputStream output;

    public HandleRequest(Route route, BufferedReader input, DataOutputStream output, ServerContext context) {
        this.route = route;
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

    private void sendResponse(RequestResponse response) {
        try {
            String resp = "HTTP/1.1 "+response.getCode().getValue()+" "+response.getCode().getVocal()+"\r\n";
            for (Header header : response.getHeaders()) {
                resp+=header.getName()+": "+header.getValue()+"\r\n";
            }
            output.write(connectArrays(resp.getBytes(),response.getContent()));
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private RequestResponse bindRequest(Request req) {
        String absPath = req.getRoute().trim();
        if (absPath.startsWith("/"))
            absPath = absPath.substring(1);

        ParseState state = new ParseState(req, absPath.split("/"), input);
        state.push(new Tuple2<>(0,route));
        while (!state.isEmpty()){
            Tuple2<RequestResponse,ParseState> res = parseOne(state);
            if (res.getX() != null)
                return res.getX();
            state = res.getY();

        }
        return new RequestResponse(new ArrayList<>(),new byte[]{}, RequestResponse.ResponseCode.NOT_FOUND);
    }


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
//            System.out.println("Stuff here");
//            if (headers.containsKey("Content-Length")) {
//                char[] chars = new char[((ContentLength)headers.get("Content-Length")).getLength()];
//                input.read(chars, 0, 12);
//                System.out.println(new String(chars));
//                output.write("<html><a href='http://www.google.com'>Go here</a></html>".getBytes());
//            }
            return new Request(type,headers,split[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new InvalidRequest(new RequestResponse(new ArrayList<>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST));
    }

    private Header parseHeader(String header) throws MalformedHeader {
        String[] split = header.split(":");
        if (split[0] == null || split[1] == null)
            throw new MalformedHeader(new RequestResponse(new ArrayList<>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST));
        switch (split[0]){
            case "Content-Length": return new ContentLength(split[0],split[1].trim());
            case "Content-Type": return new ContentType(split[0],split[1].trim());
            default: return new Header(split[0],split[1].trim());
        }
    }

    private MethodType getMethod(String method) throws NotSupportedMethod {
        switch (method){
            case "GET": return MethodType.GET;
            case "PUT": return MethodType.PUT;
            case "POST": return MethodType.POST;
            case "DELETE": return MethodType.DELETE;
            default: throw new NotSupportedMethod(new RequestResponse(new ArrayList<>(),new byte[]{}, RequestResponse.ResponseCode.BAD_REQUEST));
        }
    }

    private Tuple2<RequestResponse,ParseState> parseOne(ParseState orig){
        Tuple2<Integer,Route> popped = orig.pop();
        return popped.getY().matchRequest(orig,context,popped.getX());
    }

    private byte[] connectArrays(byte[] ar1, byte[]ar2){
        byte[] res = new byte[ar1.length+ar2.length];
        System.arraycopy(ar1, 0, res, 0, ar1.length);
        System.arraycopy(ar2, 0, res, ar1.length, ar2.length);
        return res;
    }



}
