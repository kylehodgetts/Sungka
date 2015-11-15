package com.kylehodgetts.sunka.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Adam Chlupacek
 * @version 1.0
 * Utils to extract values from json
 */
public class JsonUtil {

    /**
     * Extract the value with given key from the JSON object, in case its not found uses the default value
     * @param object        The object that has the value stored
     * @param key           The key for retrieving the value from the object
     * @param defaultValue  The default value for this value of the object
     * @return              The retrieved value or default
     */
    public static int getInt(JSONObject object,String key, int defaultValue){
        try {
            return object.getInt(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    /**
     * Extract the value with given key from the JSON object, in case its not found uses the default value
     * @param object        The object that has the value stored
     * @param key           The key for retrieving the value from the object
     * @param defaultValue  The default value for this value of the object
     * @return The retrieved value or default
     */
    public static double getDouble(JSONObject object,String key, double defaultValue){
        try {
            return object.getDouble(key);
        } catch (JSONException e) {
            return defaultValue;
        }
    }
}
