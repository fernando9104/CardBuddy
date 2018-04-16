package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionAsyncTask {

    private static ProgressBar circularProgBar;
    private static String addrServer;
    private static URL urlServer;
    private static int timeOut;
    private static String keyValue;
    private static JSONObject respRequest;

    private static class RunTask extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ForeignExchange.setClikeableProgBar(true);
            circularProgBar = ForeignExchange.getCircularProgBar();
            circularProgBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String respJson = execRequestToData();
            return respJson;
        }

        @Override
        protected void onPostExecute(String respJson ) {
            super.onPostExecute(respJson);

            try {

                ForeignExchange.setClikeableProgBar(false);
                circularProgBar.setVisibility(View.GONE);
                respRequest = new JSONObject(respJson);
                FragmentAmountEntry.goToFragmentDialog(respRequest);

            } catch (JSONException e) {
                e.printStackTrace();
                FragmentAmountEntry.goToFragmentDialog(respRequest);
            }
        }

    }// Fin de la clase RunTask

    // Metodo que ejecuta la peticion
    private static String execRequestToData() {

        String resultJson = null;

        try{
            // Creo objeto URL
            urlServer = new URL(addrServer);

            // Creo la conexion al servidor
            HttpURLConnection connServer = (HttpURLConnection) urlServer.openConnection();

            // Configuro la conexion para la peticion
            connServer.setRequestMethod("POST");
            connServer.setRequestProperty("content-type", "application/json");
            connServer.setConnectTimeout(timeOut);  // tiempo limite para la conexion
            connServer.setReadTimeout(timeOut);     // tiempo para limite para la lectura
            connServer.connect();                   // realiza conexion

            // Obtengo la entrada resultante en la peticion
            InputStream inputConnetStream = connServer.getInputStream();

            // Valido que exista la entrada
            if( inputConnetStream == null ){
                return null;
            }

            // Instancio Buffer de lectura para tratar la entrada.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputConnetStream));

            // Instancio Buffer para el resultado de la lectura de la entrada.
            StringBuffer stringBuffer = new StringBuffer();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            // Valido que haya un resultado de la lectura
            if (stringBuffer.length() == 0) {
                return null;
            }

            resultJson = stringBuffer.toString();

        }catch( MalformedURLException e ){
            e.printStackTrace();
        }catch( IOException e ){
            e.printStackTrace();
        }

        return resultJson;
    }

    /* Metodo publico que ejecuta la tarea */
    public static void request( JSONObject params ){

        respRequest = null;

        try {
            keyValue    = "pais=" + params.getString("country") + "&precio=" + params.getString("amount");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        timeOut     = 8000;
        addrServer  = "http://200.116.176.216/eMergeVisa/samplecode-PHP-771c26/vdp-php/tests/foreignexchange/ForeignExchange.php?";
        addrServer  = addrServer + keyValue;

        new RunTask().execute();
    }

}// Fin de la clase
