package com.example.developermicalisoft.apis.Services;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.developermicalisoft.apis.R;
import com.example.developermicalisoft.apis.Splash;

public class UserInterfaceSvc {

    /**
     * <p><b>En: </b>Function responsible for configuring the toolbar or appBar for the application.</p>
     * <p><b>Es: </b>Funcion encargada de configurar el toolbar o appBar para la aplicacion.</p>
     * @param activity
     * @param tittle
     * @param upbutton
     */
    public static void setToolbar(final Activity activity, String tittle, boolean upbutton, String... icon ){

        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);

        if( toolbar != null ){

            ( (AppCompatActivity) ( activity ) ).setSupportActionBar( toolbar );

            if( upbutton ){
                ( (AppCompatActivity) activity ).getSupportActionBar().setDisplayHomeAsUpEnabled( upbutton );
            }// Fin if

            if( tittle != "" && tittle != null ){
                ( (AppCompatActivity) activity ).getSupportActionBar().setTitle( tittle );
            }// Fin if tittle

            if ( icon.length == 1 ) {

                int res = activity.getResources().getIdentifier(icon[0], "drawable", activity.getPackageName());
                ( (AppCompatActivity) activity ).getSupportActionBar().setHomeAsUpIndicator( res );

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                });
            }

        }// Fin if

    }// Fin setToolba

    public static void showMsgError(Context context, String title, String msg) {

        AlertDialogs.errorMessage(context, null, title, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }// Fin onClick
        });// Fin errorMessage

    }// Fin showMsgError

    // Constructor
    public static void CreateShortcut(Context context) {

        SharedPreferences sharedShortCut;
        boolean isCreateShortcut = Boolean.FALSE;

        sharedShortCut      = PreferenceManager.getDefaultSharedPreferences( context );
        isCreateShortcut    = sharedShortCut.getBoolean("existShortcut", Boolean.FALSE);

        if( ! isCreateShortcut ){

            Intent shortcutIntent = new Intent(context, Splash.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher));
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            context.sendBroadcast(intent);

            //Indico que ya se ha creado el acceso directo para que no se vuelva a crear mas
            SharedPreferences.Editor editor = sharedShortCut.edit();
            editor.putBoolean("existShortcut", true);
            editor.commit();

        }// Fin if

    }// FIn Constructor CreateShortcut

}// Fin UserInterfaceSvc
