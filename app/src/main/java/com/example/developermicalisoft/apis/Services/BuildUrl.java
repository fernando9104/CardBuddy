package com.example.developermicalisoft.apis.Services;

public class BuildUrl {

    //http://200.116.176.216/eMergeVisa/samplecode-PHP-771c26/vdp-php/tests/
    static String baseUrl = "http://200.116.176.216/eMergeVisa/samplecode-PHP-771c26/vdp-php/tests/";
//    static String baseUrl = "http://192.168.1.180/eMergeVisa/samplecode-PHP-771c26/vdp-php/tests/";

    public static String getStringUrl( String moduleApi ){

        String urlApi;

        urlApi = baseUrl + moduleApi;

        return urlApi;
    } // Fin getStringUrl
}
