package com.example.developermicalisoft.apis.Services;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.developermicalisoft.apis.R;

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

}// Fin UserInterfaceSvc
