package com.example.developermicalisoft.apis.Services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class UrlConection {
    private static String method;
    private static String urlAddress;
    private static JSONObject[] params;
    private static String line;
    private static String nameActivity;
    private static Context activityContext;
    private static URL urlServer;
    private static String TAG_LOG = "Print UrlConection";
    private static String returnActionCode;
    private static String msgResponse;
    private static int statusCode;
    private static SharedPreferences Prefs;
    private static Map<String, String> respJson;
    private static int timeOut = 8000;


    /**
     * <p><b>En: </b>Function responsible for triggering the MakeRequest method and configuring the preferences to make requests to the server.</En:></p><br />
     * <p><b>Es: </b>Funcion encargada de disparar el metodo MakeRequest y de configurar los preferences
     * para realizar las peticiones al servidor</p>
     *
     * @param activity      contexto de la applicacion
     * @param url           direccion url a la que se realizar al request
     * @param requestMethod tipo de peticion a realizar
     * @param jsonObjects   parametros o datos a enviar
     * @see MakeRequest ver MakeRequest.
     */
    public static void request(Context activity, String url, String requestMethod, JSONObject... jsonObjects) {

        activityContext = activity;
        urlAddress = url;
        method = requestMethod;
        params = jsonObjects;

        Prefs = activity.getSharedPreferences(Constants.PREFERENCES_APP, activity.MODE_PRIVATE);    // initiating the datastore
        nameActivity = activity.getClass().getSimpleName();                              // Convierte el nombre del activity en un string

        new MakeRequest().execute();

    }// Fin request

    /**
     * <p><b>En: </b>Function responsible for making requests to the server and calling the functions
     * who prepare the data to be returned</p><br />
     * <p><b>Es: </b>Funcion encargada de realizar las peticiones al servidor y de llamar las funciones
     * que preparan los datos para ser retornados.</p>
     */
    private static class MakeRequest extends AsyncTask<String, String, String> {

        String responseServer = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }// Fin onPreExecute

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if( response.equals( "" ) ){
                statusCode = 0;
            }else{
                statusCode = ParseJsonSvc.getStatusCode(response);
            }


            switch (statusCode) {

                case 0:

                    returnActionCode = Constants.ACTION_FAIL;
                    sendResponseFail( "No response from the server" );
                    break;

                case 426:

                    returnActionCode = Constants.ACTION_UPGRADE;
                    respJson = ParseJsonSvc.parseJSON(response);
                    sendResponse(respJson);
                    break;

                case 200:

                    respJson = ParseJsonSvc.parseJSON(response);
                    if (respJson.get("ERROR") != null) {

                        returnActionCode = Constants.ACTION_FAIL;
                        sendResponseFail(respJson.get("ERROR"));

                    } else {

                        returnActionCode = Constants.ACTION_SUCCESS;
                        sendResponse(respJson);

                    }// fin if/else
                    break;

                case 408:

                    break;

                default:

                    returnActionCode = Constants.ACTION_ERROR;
                    respJson = ParseJsonSvc.parseJSON(response);
                    sendResponse(respJson);
                    break;

            }// Fin switch

        }// Fin onPostExecute

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }// fin onProgressUpdate

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected String doInBackground(String... strings) {

            if (method == "POST") {
                responseServer = postData();
            }

            if (method == "GET") {
                responseServer = getData();
            }

            return responseServer;
        }// Fin doInBackground

    }// Fin MakeRequest

    /**
     * <p><b>En: </b>Function responsible for loading data from the server<p/><br />
     * <p><b>Es: </b>Funcion encargada de realizar la carga de datos desde el servidor<p/><br />
     *
     * @return <p>devuelve los datos desde el servidor.</p>
     */
    private static String getData() {

        try {

            urlServer = new URL(urlAddress);
            HttpURLConnection conectionServer = (HttpURLConnection) urlServer.openConnection();
            conectionServer.setRequestMethod(method);
            conectionServer.setRequestProperty("content-type", "application/json");
            // Se establece el tiempo maximo de espera para realizar la conexion.
            conectionServer.setConnectTimeout(timeOut);
            // Se establece el tiempo maximo para hacer la lectura.
            conectionServer.setReadTimeout(timeOut);
            conectionServer.connect();
            InputStream inputStream = conectionServer.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();

            if (inputStream == null) {
                return null;
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            if (stringBuffer.length() == 0) {
                return null;
            }

            String forecastJsonStr = stringBuffer.toString();
            // Se cierra la conexion.
            conectionServer.disconnect();
            return forecastJsonStr;

        } catch (MalformedURLException e) {

            sendResponseFail(e.getMessage());

        } catch (IOException e) {

            sendResponseFail(e.getMessage());
        }
        return null;
    }// fin get

    /**
     * <p><b>En: </b>Function responsible for sending data to the server and returning the response</p><br/>
     * <p><b>Es: </b>Funcion encargada de enviar datos al servidor y de retornar las respuesta</p><br/>
     *
     * @return retorna el estatusCode y los datos enviados desde el servidor.
     */
    static private String postData() {

        String response = "";
        int responseCode;

        try {

            String dataToServer = params[0].toString();
            urlServer = new URL(urlAddress);
            // Se abre la conexion con el servidor.
            HttpURLConnection conectionServer = (HttpURLConnection) urlServer.openConnection();
            conectionServer.setRequestProperty("content-type", "application/json");
            // Se establece el tiempo maximo de espera para realizar la conexion.
            conectionServer.setConnectTimeout(timeOut);
            // Se establece el tiempo maximo para hacer la lectura.
            conectionServer.setReadTimeout(timeOut);
            // Activa el metodo post
            conectionServer.setDoOutput(true);
            // Se establece el metodo a usar.
            conectionServer.setRequestMethod(method);

            conectionServer.connect();

            OutputStream outputStream = conectionServer.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
            writer.write(URLEncoder.encode(dataToServer, "UTF-8"));
            outputStream.flush();
            writer.close();
            outputStream.close();

            responseCode = conectionServer.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(conectionServer.getInputStream())); // gets response String
                while ((line = br.readLine()) != null) {
                    response += line;
                }

            } else {
                BufferedReader brError = new BufferedReader(new InputStreamReader(conectionServer.getErrorStream())); //gets error response if different from 200
                while ((line = brError.readLine()) != null) {
                    response += line;
                }
            }// Fin responseCode

            if (responseCode == 503) {
                response = "";
            }// Fin if == 503

        } catch (IOException e) {

            sendResponseFail(e.getMessage());

        }// Fin try/catch

        return response;

    }// Fin postData

    /**
     * <p><b>En: </b>Function that is responsible for sending server responses to the class that requests it</p><br />
     * <p><b>Es: </b>Funcion que se encarga de enviar las respuestas del servidor a la clase que lo solicite</p>
     *
     * @param respJson
     */
    public static void sendResponse( Map respJson ) {

        Intent intent = new Intent(returnActionCode)
                .putExtra("responseRequest", (Serializable) respJson);

        activityContext.sendBroadcast(intent);

    }// Fin getFireBaseToken

    public static void sendResponseJson(String respJson) {
//        progressOn.dismiss();

        Intent intent = new Intent(returnActionCode).putExtra("responseRequest", respJson);
        activityContext.sendBroadcast(intent);

    }// Fin getFireBaseToken

    /**
     * <p><b>Es: </b>Responsible function to answer or return fail to the activity that requests it.</p><br />
     * <p><b>Es: </b>Funcion encargada de responder o retornar fail a la activiti que lo solicita.</p>
     */
    public static void sendResponseFail(String message) {
        Intent intent = new Intent(returnActionCode).putExtra("responseRequest", message);
        activityContext.sendBroadcast(intent);
    }// Fin getFireBaseToken

}
