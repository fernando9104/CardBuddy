package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class AmountEntryToAdapter {

    private Activity activity;
    private ArrayList<Integer> amountsArray;

    /* Metodo constructor de la clase */
    public AmountEntryToAdapter(Activity activity) {
        this.activity = activity;
        this.amountsArray = new ArrayList<>();
    }

    /* Metodo que se encarga de agregar una cantidad ngresada
     * al agreeglo el cual se adjuntara en el ListView actual.
     */
    private void addAmountEntryArray( Integer amount ){
        this.amountsArray.add( amount );
    }

    /* Metodo que se encarga de eliminar una cantidad existente
     * en la lista actualmente.
     */
    private void removeAmountEntryArray( Integer amount  ){
        if( amount == null ){
            this.amountsArray.removeAll( this.amountsArray );   // Remover toda la lista
        }else{
            this.amountsArray.remove( amount.intValue() );      // Remover un item de la lista
        }
    }

    /* Metodo que se encarga de configurar el adapatador. */
    private ArrayAdapter<Integer> getSetupAdapter(){

        return new ArrayAdapter(this.activity,
                android.R.layout.simple_list_item_1,
                this.amountsArray);
    }

    /* Metodo que obtiene el adaptador de cantidades. */
    public ArrayAdapter<Integer> getAdapter(Integer amount, boolean isAdd ){

        if( isAdd ){
            addAmountEntryArray( amount );
        }else{
            removeAmountEntryArray( amount );
        }

        return getSetupAdapter();
    }

    /* Metodo que se encarga de obtener la lista
     * actual cantidades ingresadas.
     */
    public ArrayList<Integer> getAmountsArray() {
        return this.amountsArray;
    }

}// Fin de la clase
