package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class SelectCountryToAdapter {
    private Activity activity;
    private ArrayList<String> countriesArray;

    /* Metodo constructor de la clase */
    public SelectCountryToAdapter(Activity activity) {
        this.activity = activity;
        this.countriesArray = new ArrayList<>();
    }

    /* Metodo que se encarga de agregar un pais al arreglo
     * de paises que se adjuntara en el ListView actual.
     */
    private void addSelectedCountryArray( String country ){
        this.countriesArray.add( country );
    }

    /* Metodo que se encarga de eliminar un pais del arreglo
     * de paises que se encuentra en el ListView actual.
     */
    private void removeSelectedCountryArray( String country ){
        this.countriesArray.remove( country );
    }

    /* Metodo que se encarga de configurar el adapatador. */
    private ArrayAdapter<CharSequence> getSetupAdapter(){

        return new ArrayAdapter(this.activity,
                android.R.layout.simple_list_item_1,
                this.countriesArray);
    }

    /* Metodo que obtiene el adaptador de paises. */
    public ArrayAdapter<CharSequence> getAdapter( String country, boolean isAdd ){

        if( isAdd ){
            addSelectedCountryArray( country );
        }else{
            removeSelectedCountryArray( country );
        }

        return getSetupAdapter();
    }

    /* Metodo que se encarga de obtener la lista
     * actual de paises seleccionados.
     */
    public ArrayList<String> getCountriesArray() {
        return this.countriesArray;
    }

}// Fin de la clase

