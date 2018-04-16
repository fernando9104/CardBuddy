package com.example.developermicalisoft.apis.Services;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;

import com.example.developermicalisoft.apis.R;

public class AlertDialogs {

    /**
     * <p><b>Es: </b>Las funciones reciven como parametros el context, iconName, stringTittle, StringMessage
     * Esta funcion recive como parametros el context, iconName, stringTittle, StringMessage
     * si el parametro titulo se envia vacio se pondra por defecto el nombre de la app como titulo de la ventana
     * como parametro opcional se puede enviar el texto a mostrar en el botton positivo.</p>
     */
    private static final int MESSAGE_ALERT      = 1;
    private static final int CONFIRM_ALERT      = 2;
    private static final int ERROR_ALERT        = 3;
    private static final int CONFIRM_USER_ACTION= 4;
    private static String labelButtonNegative   = "";
    private static Boolean negativeButton       = true;
    private static String textPositiveButton    = "Ok";

    public static void messageAlert(Context context, String icon, String title, String message, DialogInterface.OnClickListener posCallback) {

        show(ERROR_ALERT, context, icon, title, message, posCallback, context.getString(R.string.ok), null);
    }

    public static void confirmationAlert( Context context, String icon, String title, String message, DialogInterface.OnClickListener posCallback, String labelPositiveButton) {
        if( labelPositiveButton == null || labelPositiveButton.equals("") ){
            labelPositiveButton = context.getString(R.string.yes);
        }
        show(CONFIRM_ALERT, context, icon, title, message, posCallback, labelPositiveButton, null);
    }

    public static void confirmUserAlert( Context context, String icon, String title, String message, String labelPositiveButton, DialogInterface.OnClickListener posCallback, DialogInterface.OnClickListener negCallback) {
        if( labelPositiveButton == null || labelPositiveButton.equals("") ){
            labelPositiveButton = context.getString(R.string.yes);
        }
        show(CONFIRM_USER_ACTION, context, icon, title, message, posCallback, labelPositiveButton, negCallback);
    }

    public static void errorMessage(Context context, String icon, String title, String message, DialogInterface.OnClickListener posCallback) {

        show(ERROR_ALERT, context, icon, title, message, posCallback, context.getString(R.string.ok), null);
    }// Fin error Messages

    /**
     * <p><b>En: </b>Function responsible for creating windows to show messages to the user.</p><br>
     * <p><b>Es: </b>Funcion encargada de crear las ventanas para mostrar mensajes al usuario.</p>
     * @param alertType
     * @param context
     * @param icon
     * @param title
     * @param message
     * @param posCallback
     * @param labelPositiveButton
     */
    public static void show(int alertType, Context context, String icon, String title, String message, DialogInterface.OnClickListener posCallback, String labelPositiveButton, DialogInterface.OnClickListener negCallback) {


        try{

            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            /* SET TITTLE */
            if ( title == null ){

                builder.setTitle( context.getResources().getString(R.string.app_name) );
            } else if( title.equals("") ){

                builder.setTitle( "" );
            }else{

                builder.setTitle( title );
            }

            /* END SET TITTLE  */

            /* SET ICON */
            if (icon != null) {

                int res = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
                builder.setIcon(res);
            }
            /* END SET ICON */

            labelButtonNegative = context.getString(R.string.cancel);

            builder.setCancelable(false);

            switch (alertType) {

                case MESSAGE_ALERT:
                    builder.setMessage(Html.fromHtml(message));
                    builder.setPositiveButton( labelPositiveButton, posCallback);
                    negativeButton = false;
                    break;

                case CONFIRM_ALERT:
                    builder.setMessage(Html.fromHtml(message));
                    builder.setPositiveButton( labelPositiveButton, posCallback);
                    negativeButton = true;
                    labelButtonNegative = context.getString(R.string.no);
                    break;

                case ERROR_ALERT:
                    builder.setPositiveButton( labelPositiveButton, posCallback);
                    builder.setMessage(Html.fromHtml(message));
                    negativeButton = false;
                    break;

                case CONFIRM_USER_ACTION:
                    builder.setMessage(Html.fromHtml(message));
                    builder.setPositiveButton( labelPositiveButton, posCallback);
                    negativeButton = true;
                    labelButtonNegative = context.getString(R.string.no);
                    break;
            }// Fin if

            if( alertType == CONFIRM_USER_ACTION ){
                builder.setNegativeButton( labelButtonNegative, negCallback );
            }else if ( negativeButton ) {
                builder.setNegativeButton(labelButtonNegative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });

            }// Fin if

            builder.show();

        }catch ( Exception e ){

            Log.d("Print AlertDialogs", "Error: " + e.getMessage() );
        }

    }// Fin show

}// Fin alertsDialogs
