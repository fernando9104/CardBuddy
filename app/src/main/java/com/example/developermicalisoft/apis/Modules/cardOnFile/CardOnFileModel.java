package com.example.developermicalisoft.apis.Modules.cardOnFile;

import org.json.JSONException;
import org.json.JSONObject;

public class CardOnFileModel {


    String vAULastUpdateDate, tokenPANReplacementStatus, tokenPANReplacementDate,
            acctNumOld4Digit, mCC, mrchName,
            tokenReqstrId, totalTranCount, lastMrchTranDt,
            vAUUpdateStatus;

    public CardOnFileModel(JSONObject thisRec) {

        try {

            this.vAULastUpdateDate = thisRec.get("vAULastUpdateDate").toString();
            this.tokenPANReplacementStatus = thisRec.get("tokenPANReplacementStatus").toString();
            this.tokenPANReplacementDate = thisRec.get("tokenPANReplacementDate").toString();
            this.acctNumOld4Digit = thisRec.get("acctNumOld4Digit").toString();
            this.mCC = thisRec.get("mCC").toString();
            this.mrchName = thisRec.get("mrchName").toString();
            this.tokenReqstrId = thisRec.get("tokenReqstrId").toString();
            this.totalTranCount = thisRec.get("totalTranCount").toString();
            this.lastMrchTranDt = thisRec.get("lastMrchTranDt").toString();
            this.vAUUpdateStatus = thisRec.get("vAUUpdateStatus").toString();

        } catch (JSONException e) {

            this.vAULastUpdateDate = vAULastUpdateDate;
            this.tokenPANReplacementStatus = tokenPANReplacementStatus;
            this.tokenPANReplacementDate = tokenPANReplacementDate;
            this.acctNumOld4Digit = acctNumOld4Digit;
            this.mCC = mCC;
            this.mrchName = mrchName;
            this.tokenReqstrId = tokenReqstrId;
            this.totalTranCount = totalTranCount;
            this.lastMrchTranDt = lastMrchTranDt;
            this.vAUUpdateStatus = vAUUpdateStatus;
        }
    }// Fin constructor CardOnFile_Model
}// Fin clase principal CardOnFileModel
