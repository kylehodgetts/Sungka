package com.kylehodgetts.sunka.util;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * A json request that is properly typed with headers
 */
public class RequestWithHeaders extends JsonObjectRequest {
    public RequestWithHeaders(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        headers.put("Content-Length",getBody().length+"");
        return headers;
    }
}
