package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.example.developermicalisoft.apis.R;

import java.util.ArrayList;

public class TravelItineraryDialog {


    public static void createDialog(final Activity activity , final ArrayList<String> data ){

        int titleId             = 0;
        int textId              = 0;
        final String callDialog = data.get(0);
//        int azafataIconId       = activity.getResources().getIdentifier("azafata", "drawable", activity.getPackageName());


        // Configura punto de control actual de la App
        ForeignExchange.setCheckPointApp(callDialog);

        // Identifica el dialogo a crear.
        switch(callDialog){
            case "A":
                titleId = R.string.noTravelItinerary_title;
                textId  = R.string.noTravelItinerary_text;
                break;
            case "B":
                titleId = R.string.countryNoTravelItinerary_title;
                textId  = R.string.countryNoTravelItinerary_text;
                break;
        }// Fin del switch

        // Instancia el objeto dialogo
        AlertDialog.Builder travelItineraryDialog = new AlertDialog.Builder(activity);

        // Configura el objeto dialogo
        travelItineraryDialog
                .setTitle(titleId)
                .setMessage(textId)
                .setCancelable(false)
                .setIcon(activity.getResources().getDrawable(R.drawable.azafata))
                .setPositiveButton(R.string.yes_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Bundle args = new Bundle();
                        Fragment travelItineraryFragment = ForeignExchange.getTravelItineraryFragment();

                        // Identifica el dialogo a crear.
                        switch(callDialog){
                            case "A":
                                args.putStringArrayList("data", data );
                                break;
                            case "B":
                                args.putStringArrayList("data", data );
                                break;
                        }// Fin del switch

                        travelItineraryFragment.setArguments(args);
                        FragmentToTransaction.commit(activity, travelItineraryFragment);
                    }
                })
                .setNegativeButton(R.string.no_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Identifica el dialogo a crear.
                        switch(callDialog){
                            case "A":
                                data.set(0,"B");
                                data.set(1,"update");
                                createDialog( activity, data );
                                break;
                            case "B":
                                data.set(0,"F");
                                data.set(1,"ForeignExchange");

                                Bundle args = new Bundle();
                                args.putStringArrayList("data", data );

                                Fragment emountEntryFragment = ForeignExchange.getAmountEntryFragment();
                                FragmentToTransaction.commit(activity, emountEntryFragment);
                                break;
                        }// Fin del switch
                    }
                });

        // Muestra el Dialogo
        travelItineraryDialog.show();

    }// Fin funcion createDialog

}// fin de la clase