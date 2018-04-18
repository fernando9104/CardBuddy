package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.example.developermicalisoft.apis.R;
import com.example.developermicalisoft.apis.Services.UserInterfaceSvc;

import java.util.ArrayList;

public class ForeignExchange extends Fragment {

    private static Fragment welcomeFragment, travelItineraryFragment, amountEntryFragment;
    private static ProgressBar circularProgBar;
    private static Handler checkProgBarHandler;
    private static Activity foreignExchangeActivity;
    private static ConstraintLayout layoutProgBar;
    private static String currentCheckPoint;
    private Toolbar toolbar;
    private View foreignChangeView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        foreignChangeView = inflater.inflate(R.layout.foreign_exchange, container, false);

        // Obtiene la actividad principal
        foreignExchangeActivity = getActivity();  // Actividad principal
        currentCheckPoint       = "A";   // Punto de control inicial.

        // Obtiene los items del diseño
        toolbar         = foreignChangeView.findViewById(R.id.toolbar);
        layoutProgBar   = foreignChangeView.findViewById(R.id.layout_progBar);
        circularProgBar = foreignChangeView.findViewById(R.id.welcome_progBar);

        // Obitiene Fragmentos
        welcomeFragment         = new FragmentWelcome();            // Welcome
        travelItineraryFragment = new FragmentTravelItinerary();    // Travel Itinerary
        amountEntryFragment     = new FragmentAmountEntry();        // Amount Entry

        // Incrustar Fragmento
        FragmentToTransaction.commit( getActivity(), welcomeFragment );

        return foreignChangeView;

    }// Fin onCreateView

    /* Configura el texto del toolbar de la applicacion. */

    public static void setupToolbarText( int id ){
        UserInterfaceSvc.setToolbar(foreignExchangeActivity, foreignExchangeActivity.getString(id), true);
    }

    /* Obtiene la actividad principal de la App. */
    public static Activity getActivityMain(){
        return foreignExchangeActivity;
    }

    /* Obtiene el fragmento del diseño Welcome */
    public static Fragment getWelcomeFragment(){
        return welcomeFragment;
    }

    /* Obtiene el fragmento del diseño Amount Entry */
    public static Fragment getAmountEntryFragment(){
        return amountEntryFragment;
    }

    /* Obtiene el fragmento del diseño Travel Itinerary */
    public static Fragment getTravelItineraryFragment(){
        return travelItineraryFragment;
    }

    /* Obtiene el objeto Progress Bar de la actividad principal. */
    public static ProgressBar getCircularProgBar(){
        return circularProgBar;
    }

    /* Se encarga de bloquear y desbloquear el diseño mientra esta en carga. */
    public static void setClikeableProgBar( Boolean clickear ){
        layoutProgBar.setClickable(clickear);
    }

    /* Configura el punto de control actual de la aplicacion.*/
    public static void setCheckPointApp( String checkPoint ){
        currentCheckPoint = checkPoint;
    }

    /* Obtiene el punto de control actual de la aplicacion.*/
    public static String getCheckPointApp(){
        return currentCheckPoint;
    }

    /* Funcion que se encarga de ejecutar el progress
     * bar por un tiempo de 5 segundos.
     *
     * NOTA:
     * Este progreso es no esta ligado a ninguna
     * carga o proceso que este haciendo la aplicacion.
     */
    public static void runProgBar(){

        setClikeableProgBar(true);
        circularProgBar.setVisibility( View.VISIBLE );
        // Duerme la aplicacion durante 5 segundos
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                setClikeableProgBar(false);
                circularProgBar.setVisibility( View.GONE );
            }
        }, 3000);
    }

    /* Funcion que se encarga de configurar la verificacion
     * del estado del progress bar.
     */
    public static void setupVerifyProgBar( final ArrayList<String> data  ){

        checkProgBarHandler = new Handler();
        checkProgBarHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if( circularProgBar.getVisibility() == View.GONE ){
                    // Este paso no requiere dialogo
                    if( data.get(0).equals("F") ){
                        Bundle args = new Bundle();
                        args.putStringArrayList("data",data);
                        Fragment amountEntryFragment = ForeignExchange.getAmountEntryFragment();
                        amountEntryFragment.setArguments(args);
                        FragmentToTransaction.commit( ForeignExchange.getActivityMain(), amountEntryFragment );
                    }else{
                        TravelItineraryDialog.createDialog( foreignExchangeActivity, data );
                    }
                }else{
                    checkProgBarHandler.postDelayed(this, 1000);
                }
            }
        },1000);
    }

}// Fin Clase principal