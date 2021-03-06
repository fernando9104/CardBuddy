package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.developermicalisoft.apis.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FragDialogAmountCalculated extends DialogFragment{

    private View amountCalculatedLayout;
    private static final String TAG = "FragDialogAmountCalculated";
    private ArrayList<String> data;
    private Button calculateAmountButton;
    private TextView exchangeCountryTextView, conversionRateTextView, sourceCountryTextView, messageCreditTextView;
    private EditText totalAmountEditText, conversionAmountEditText, availCreditEditText, conversionAmount2EditText, remainingCreditEditText;
    private String textValueContainer;
    private HashMap<String,String> ISOcoins;
    private FragDialogAmountCalculated fragDialog;

    // Obtengo parametros pasados al fragment.
    @Override
    public void onCreate( Bundle bundle ) {
        super.onCreate(bundle);
        if( getArguments() != null ){
            this.data = getArguments().getStringArrayList("data");
        }
    }

    // Metodo principal o constructor
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Obtiene items del diseño
        amountCalculatedLayout  = inflater.inflate(R.layout.fragment_amount_calculated, container, false);
        fragDialog = this;

        // Obtiene items del diseño
        calculateAmountButton       = amountCalculatedLayout.findViewById(R.id.calculate_another_Button);
        exchangeCountryTextView     = amountCalculatedLayout.findViewById(R.id.exchange_country_Text);
        conversionRateTextView      = amountCalculatedLayout.findViewById(R.id.conversion_rate_Text);
        sourceCountryTextView       = amountCalculatedLayout.findViewById(R.id.source_country_Text);
        totalAmountEditText         = amountCalculatedLayout.findViewById(R.id.total_amount_Edit);
        conversionAmountEditText    = amountCalculatedLayout.findViewById(R.id.conversion_amount_Edit);
        availCreditEditText         = amountCalculatedLayout.findViewById(R.id.avail_credit_Edit);
        conversionAmount2EditText   = amountCalculatedLayout.findViewById(R.id.conversion_amount_2_Edit);
        remainingCreditEditText     = amountCalculatedLayout.findViewById(R.id.remaining_credit_Edit);
        messageCreditTextView       = amountCalculatedLayout.findViewById(R.id.message_credit_Text);
        ISOcoins                    = getISOcoinsHashMap();

        // Configura los textos con la informacion suministrada.
        textValueContainer = exchangeCountryTextView.getText().toString().replace("#country#", this.data.get(1));
        exchangeCountryTextView.setText(textValueContainer);
        conversionRateTextView.setText(this.data.get(3));

        textValueContainer = sourceCountryTextView.getText().toString().replace("#country#", this.data.get(1));
        textValueContainer = textValueContainer.replace("#coin#", ISOcoins.get(this.data.get(0)));
        sourceCountryTextView.setText(textValueContainer);

        totalAmountEditText.setText(this.data.get(2));
        conversionAmountEditText.setText(this.data.get(4));
        availCreditEditText.setText(this.data.get(5));
        conversionAmount2EditText.setText(this.data.get(4));
        remainingCreditEditText.setText(this.data.get(6));

        // Identifica el color
        switch(this.data.get(8)){
            case "red":
                messageCreditTextView.setText(R.string.msg_not_available_credit_text);
                messageCreditTextView.setTextColor(getResources().getColor(R.color.red_text));
                remainingCreditEditText.setTextColor(getResources().getColor(R.color.red_text));
                break;
            case "green":
                messageCreditTextView.setText(R.string.msg_available_credit_text);
                messageCreditTextView.setTextColor(getResources().getColor(R.color.green_text));
                remainingCreditEditText.setTextColor(getResources().getColor(R.color.green_text));
                break;
        }// fin del switch

        // Configura los eventos de escucha
        listenerEventsSetup();

        return amountCalculatedLayout;
    }

    // Metodo que ajusta el tamaño del diseño al contenedor tipo Dialogo.
    @Override
    public void onResume(){
        super.onResume();
        int widthLayout   = getResources().getDisplayMetrics().widthPixels;
        int heightLayout  = getResources().getDisplayMetrics().heightPixels-50;
        getDialog().getWindow().setLayout( widthLayout, heightLayout );
    }

    /* Metodo que configura los eventos de escucha de los
     * items del diseño.
     */
    private void listenerEventsSetup(){

        // Declara el evento de escucha del item calculate_Button
        calculateAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentAmountEntry.clearAmountsListView();
                fragDialog.getDialog().dismiss();
            }
        });
    }

    /* Metodo que obtiene un arreglo clave-valor con los
     * ISO de cada pais y la moneda correspondiente de
     * de cada uno de ellos.
     */
    private HashMap<String,String> getISOcoinsHashMap(){

        String [] objArray = getResources().getStringArray(R.array.ISO_coins);
        HashMap<String,String> hashMap = new HashMap<>();

        for( String obj: objArray ){
            String[] keyValue = obj.split(",");
            hashMap.put(keyValue[0], keyValue[1]);
        }

        return hashMap;
    }

}// Fin de la clase