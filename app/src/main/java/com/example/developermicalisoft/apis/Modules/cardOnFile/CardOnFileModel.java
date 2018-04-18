package com.example.developermicalisoft.apis.Modules.cardOnFile;

import org.json.JSONException;
import org.json.JSONObject;

public class CardOnFileModel {


    String mrchName,
            lastMrchTranDt,
            acctNumOld4Digit,
            iconWarnin,
            iconCheck;


    public CardOnFileModel(JSONObject thisRec) {

        try {

            this.mrchName = thisRec.get("mrchName").toString();
            this.lastMrchTranDt = thisRec.get("lastMrchTranDt").toString();
            this.iconWarnin = thisRec.get("iconWarnin").toString();
            this.iconCheck = thisRec.get("iconCheck").toString();

        } catch (JSONException e) {

            this.mrchName = "Error";
            this.lastMrchTranDt = e.getMessage();
            this.iconWarnin = "iconWarnin";
            this.iconCheck = "iconCheck";
        }
    }// Fin constructor CardOnFile_Model
}// Fin clase principal CardOnFileModel
