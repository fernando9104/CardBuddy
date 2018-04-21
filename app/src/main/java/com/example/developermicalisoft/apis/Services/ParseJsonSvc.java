package com.example.developermicalisoft.apis.Services;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ParseJsonSvc {

    private static int statusCode = 0;
    private static JSONObject response = null;
    private static String TAG_LOG = "Print PerseJsonSvc";

    /**
     * @param json response with format json from server.
     * @return status code
     */
    public static int getStatusCode(String json) {

        try {

            response = new JSONObject(json);
            Integer statusJSON = response.getInt("code");

            List<Integer> getRequest = new ArrayList<Integer>();

            getRequest.add(statusJSON);
            statusCode = getRequest.get(0);

        } catch (JSONException e) {

            statusCode = 2;
        }

        return statusCode;

    }// Fin getStatus

    /**
     * Funcion encargada de convertir el JSON en un array tipo kay - value
     *
     * @param json
     * @return
     */
    public static Map<String, String> parseJSON(String json) {

        JSONObject response = null;
        Map<String, String> resp = new HashMap<String, String>();

        String step = "parseJSONTest Init";

        try {

            response = new JSONObject(json);
            Iterator<String> iter = response.keys();

            while (iter.hasNext()) {

                step = "while return";
                String key = iter.next();
                Object value = response.get(key);

                step = "while else";
                resp.put(key.toString(), value.toString());

            }// Fin while

            step = "while finish while";

        } catch (Exception e) {

            resp.put("ERROR", step + " : " + e.getMessage().toString());

        }// try/catch.
        //Log.d(TAG_LOG, "resp: " + resp);
        return resp;
    }// Fin parseJSON


}// Fin ParseJsonSvc
