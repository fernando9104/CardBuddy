package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.example.developermicalisoft.apis.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CountriesToAdapter {

    private Activity activity;
    private ArrayAdapter<CharSequence> countriesAdapter;
    private ArrayList<String> countriesArray;
    private HashMap<String,String> isoCountries;

    /* Metodo constructor de la clase */
    public CountriesToAdapter(Activity activity) {
        this.activity = activity;
        this.countriesArray = getValueOfArray();
        this.countriesAdapter = getSetupAdapter();
    }

    /* Metodo que se encarga de configurar el adapatador. */
    private ArrayAdapter<CharSequence> getSetupAdapter(){

        return new ArrayAdapter(this.activity,
                android.R.layout.select_dialog_item,
                this.countriesArray);
    }

    /* Metodo que se encarga de obtener la lista de opciones
     * de paises para el combo countries.
     */
    private ArrayList<String> getValueOfArray(){

        String[] optionsArray  = this.activity.getResources().getStringArray(R.array.countries);
        countriesArray  = new ArrayList<>();
        isoCountries    = new HashMap<>();

        // Itera los valores de la array
        for( String obj: optionsArray ){
            String[] keyValue = obj.split(",");
            if( keyValue.length == 1 ){
                countriesArray.add(keyValue[0]);
            }else{
                countriesArray.add(keyValue[1]);
                isoCountries.put(keyValue[1], keyValue[0]);
            }
        }

        return countriesArray;
    }

    /* Metodo que obtiene un objeto clave-valor, con el
     * codigo ISO y el pais correspondiente.
     */
    public HashMap<String,String> getISOCountries(){
        return isoCountries;
    }

    /* Metodo que elimina un elemento del arreglo
     * de paises que estaran en el spinner.
     */
    public void removeCountry( String country ){
        this.countriesArray.remove(country);
    }

    /* Metodo que obtiene el arreglo de paises que se
     * encuentran en el combo del diseño.
     */
    public ArrayList<String> getCountriesArray(){
        return this.countriesArray;
    }

    /* Metodo que obtiene el adaptador de paises. */
    public ArrayAdapter<CharSequence> getAdapter(){
        return this.countriesAdapter;
    }

}// Fin de la clase
