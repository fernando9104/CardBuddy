package com.example.developermicalisoft.apis.Services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SortJsonSvc {

    private static String stage = "Init SortJsonSvc";
    JSONArray sortJsonSvc;

    public static JSONArray SortJsonSvc( JSONArray array, String nameKey, String firstFilter, String secondFilter ) {
        return setSortJSON( array, nameKey, firstFilter, secondFilter );
    }

    /**
     * <p><b>Es: </b>Funcion para ordenar de manera ascendete el la informacion que llega desde el servidor</p>
     */
    private static JSONArray setSortJSON(JSONArray merchantsArray, String nameKey, String firstFilter, String secondFilter){

        JSONArray dataSort = null;

        try{

            stage = "Init publicData";
            int indexArray      = 0;
            int containtY        = merchantsArray.length() ;
            int containtN       = 1;
            JSONArray sortJSON  = new JSONArray();

            stage = "Iter data";
            while (indexArray <= merchantsArray.length() - 1) {

                JSONObject thisRec = merchantsArray.getJSONObject(indexArray);
                Iterator<String> iterThisRec = thisRec.keys();

                if (iterThisRec.hasNext()) {

                    if( thisRec.getString( nameKey).toString().equals( firstFilter ) ){
                        stage = "Add position N";
                        thisRec.put("position", containtN );
                        containtN ++;
                        sortJSON.put( thisRec );
                    }else if( thisRec.getString( nameKey).toString().equals( secondFilter ) ){
                        stage = "Add position Y";
                        thisRec.put("position", containtY );
                        containtY --;
                        sortJSON.put( thisRec );
                    }// Fin if/else
                }// Fin iterThisRec

                indexArray++;

            }// Fin while

            return dataSort = sortJsonArray( sortJSON );

        }catch ( JSONException e ){

            return dataSort = merchantsArray;
        }

    }// Fin sortJSON

    /**
     * <p><b>Es: </b>Funcion encargada de ordenar el json Ascendentemente ordenando por el key "position"</p>
     * @param array
     * @return
     */
    private static JSONArray sortJsonArray( JSONArray array ){

        List<JSONObject> jsons = new ArrayList<JSONObject>();

        try{

            for (int i = 0; i < array.length(); i++) {
                jsons.add( array.getJSONObject(i) );
            }

            Collections.sort(jsons, new Comparator<JSONObject>() {

                @Override
                public int compare(JSONObject lhs, JSONObject rhs) {

                    String lid = null;
                    String rid = null;

                    try {
                        lid = lhs.getString("position");
                        rid = rhs.getString("position");
                    } catch (JSONException e) {

                    }
                    // Here you could parse string id to integer and then compare.
                    return lid.compareTo(rid);
                }
            });// Fin sort

        }catch ( JSONException e ){

            return array;
        }// Fin if try/catch

        return new JSONArray( jsons );
    }// Fin sortJsonArray

}
