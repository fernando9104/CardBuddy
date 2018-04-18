package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.developermicalisoft.apis.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class FragmentAmountEntry  extends Fragment{

    private View amountEntryLayout;
    private ArrayList<String> data;
    private Spinner countriesSpinner;
    private FloatingActionButton amountAddButton, amountRemoveButton;
    private EditText amountEdit;
    private Integer entryAmount;
    private int selectedAmountList;
    private Button calculateButton;
    private String defaultValueSpinner;
    private InputMethodManager inputMethodManager;

    private static CountriesToAdapter countriesAdapter;
    private static TextView conversionRateTextView;
    private static ArrayList<Integer> selectedAmountsArray;
    private static AmountEntryToAdapter amountsEntryAdapter;
    private static ListView amountsListView;
    private static HashMap<String,String> iSOCountries, minimumAmounts;
    private static String selectedCountry;
    private static EditText totalAmountEdit;
    private static FragmentManager fragmentManager;
    private static Toast messageToast;
    private static Boolean sendMinimumAmount;
    private static String convRateTextContainer;

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
        amountEntryLayout  = inflater.inflate(R.layout.fragment_amount_entry, container, false);
        ForeignExchange.setCheckPointApp("F");

        // Configura titulo del toolbar
        ForeignExchange.setupToolbarText(R.string.foreingExchange_title);

        // Obtiene items del diseño
        amountEdit              = amountEntryLayout.findViewById(R.id.amount_Edit);
        countriesSpinner        = amountEntryLayout.findViewById(R.id.countries_spinner);
        amountAddButton         = amountEntryLayout.findViewById(R.id.amount_add_Button);
        amountRemoveButton      = amountEntryLayout.findViewById(R.id.amount_remove_Button);
        amountsListView         = amountEntryLayout.findViewById(R.id.amounts_List);
        totalAmountEdit         = amountEntryLayout.findViewById(R.id.total_amount_Edit);
        conversionRateTextView  = amountEntryLayout.findViewById(R.id.conversion_rate_Text);
        calculateButton         = amountEntryLayout.findViewById(R.id.calculate_Button);
        convRateTextContainer   = getString(R.string.convertion_rate_label);
        defaultValueSpinner     = getResources().getString(R.string.country_select_prompt);

        // Instancia los objetos necesarios para la correcta interaccion con el usuario
        countriesAdapter      = new CountriesToAdapter(getActivity());
        amountsEntryAdapter   = new AmountEntryToAdapter(getActivity());
        messageToast          = Toast.makeText(getActivity(),"", Toast.LENGTH_SHORT );
        fragmentManager       = getFragmentManager();
        inputMethodManager    = (InputMethodManager)amountEntryLayout.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        selectedAmountList    = -1;
        conversionRateTextView.setVisibility(View.GONE);

        // Configura Pais
        countriesSpinner.setAdapter(countriesAdapter.getAdapter());

        listenerEventsSetup();  // Configura los eventos de escucha

        return amountEntryLayout;
    }

    /* Soluciona que los items del diseño se puedan refrescar
     * de forma correcta con el nuevo valor.
     */
    @Override
    public void onResume(){
        super.onResume();
        countriesSpinner.setSelection(getCountryPosition("Colombia"));
        calculateTotalAmount(); // Calcula monto total actual
    }

    /* Metodo que configura los eventos de escucha de los
     * items del diseño.
     */
    private void listenerEventsSetup(){

        // Declara el evento de escucha del item countries_spinner
        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedCountry = adapterView.getItemAtPosition(i).toString();

                sendMinimumAmount = true;
                executeRequest( sendMinimumAmount ); // Ejecuta la peticion, enviar el valor minimo
            }
        });

        // Declara el evento de escucha del item amounts_List
        amountsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //selectedAmountList = Integer.parseInt(adapterView.getItemAtPosition(i).toString());
                selectedAmountList = i;
            }
        });

        // Declara el evento de escucha del item amount_add_Button
        amountAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amountEmpty = amountEdit.getText().toString();

                if( amountEmpty.equals("") ){

                    // Mensaje de aviso al usuario
                    setupMessageToast(R.string.amount_empty_Text);
                }else{

                    amountEdit.setText("");
                    entryAmount = Integer.parseInt(amountEmpty);
                    amountsListView.setAdapter(amountsEntryAdapter.getAdapter(entryAmount,true));
                    inputMethodManager.hideSoftInputFromWindow(amountEdit.getWindowToken(), 0);

                    // Calcula o recalcula el monto total
                    calculateTotalAmount();
                }
            }
        });

        // Declara el evento de escucha del item amount_remove_Button
        amountRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( selectedAmountList == -1 ){
                    // Mensaje de aviso al usuario
                    setupMessageToast(R.string.amount_no_selected_list_Text);
                }else{
                    amountsListView.setAdapter(amountsEntryAdapter.getAdapter(selectedAmountList,false));
                    selectedAmountList = -1;

                    // Calcula o recalcula el monto total
                    calculateTotalAmount();
                }
            }
        });

        // Declara el evento de escucha del item Calculate_Button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean  calculateForeign  = true;
                selectedAmountsArray = amountsEntryAdapter.getAmountsArray();

                if( selectedCountry.equals(defaultValueSpinner) ){
                    calculateForeign = false;
                }else
                if( selectedAmountsArray.isEmpty() ){
                    calculateForeign = false;
                }

                if( calculateForeign ){
                    // Ejecuta la peticion
                    sendMinimumAmount = false;
                    executeRequest( sendMinimumAmount );
                }else{
                    // Mensaje de aviso al usuario
                    setupMessageToast(R.string.data_required_Text);
                }
            }
        });
    }

    /* Metodo que obtiene la posicion actual en el arreglo
     * perteneciente al spinner la ciudad que es requerida.
     */
    private int getCountryPosition( String country ){

        ArrayList<String> countriesArray = countriesAdapter.getCountriesArray();
        int position = 0;

        for ( int i = 0; i < countriesArray.size(); i++ ) {
            if( country.equals(countriesArray.get(i)) ){
                position = i;
                break;
            }
        }
        return position;
    }

    /* Metodo que se encarga de calcular el monto
     * total para la conversion.
     */
    private static void calculateTotalAmount(){

        selectedAmountsArray = amountsEntryAdapter.getAmountsArray();
        Integer totalAmount  = 0;

        for( Integer amount: selectedAmountsArray ){
            totalAmount += amount;
        }

        totalAmountEdit.setText(totalAmount.toString());
    }

    /* Metodo que se encarga de ejecutar la peticion
     * de cambio de divisas.
     */
    private void executeRequest( Boolean sendMinimumAmt ){

        iSOCountries    = countriesAdapter.getISOCountries();
        minimumAmounts  = countriesAdapter.getMinimunAmounts();

        try {

            JSONObject params = new JSONObject();

            if( sendMinimumAmt ){
                params.put("amount", minimumAmounts.get(selectedCountry));
            }else{
                params.put("amount", totalAmountEdit.getText().toString());
            }

            params.put("country", iSOCountries.get(selectedCountry));
            ConnectionAsyncTask.request( params );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* Configura los mensajes de aviso o alerta
     * para el usuario.
     */
    public static void setupMessageToast( int idString ){

        if( messageToast.getView().isShown() ){
            messageToast.cancel();
        }
        messageToast.setText(idString);
        messageToast.show();
    }

    /* Metodo que se encarga de limpiar toda la lista de montos */
    public static void clearAmountsListView(){
        amountsListView.setAdapter(amountsEntryAdapter.getAdapter( null,false));
        calculateTotalAmount();
    }

    /* Metodo que activa el fragmentDialog con los resultados
     * de la peticion (Divisa).
     * NOTA esta funcion es invocada en el metodo onPostExecute
     * de la calse Async Task sirve como Callback.
     */
    public static void goToFragmentDialog( JSONObject respRequest ){

        try {

            String conversionRate = respRequest.getString("conversionRate");
            String result = respRequest.getString("result");

            if( sendMinimumAmount ){

                HashMap<String,String> ISOcoins = countriesAdapter.getISOcoins();
                String ISOcountry  = iSOCountries.get(selectedCountry);

                String convRateNewText = convRateTextContainer.replace("#coin#", ISOcoins.get(ISOcountry));
                convRateNewText = convRateNewText.replace("#amount#", conversionRate);
                conversionRateTextView.setText(convRateNewText);
                conversionRateTextView.setVisibility(View.VISIBLE);
            }else{
                // Crea el arreglo con los datos ingresados
                ArrayList<String> data = new ArrayList<>();
                data.add(iSOCountries.get(selectedCountry));            //ISO
                data.add(selectedCountry);                              //Ciudad
                data.add(totalAmountEdit.getText().toString());         //Cantidad
                data.add(conversionRateTextView.getText().toString());  //ConversionRate
                data.add(result);                                       //Cantidad resultante

                // Crea objeto clave-valor para argumentos
                Bundle args = new Bundle();
                args.putStringArrayList( "data",data );

                FragDialogAmountCalculated fragDialog = new FragDialogAmountCalculated();
                fragDialog.setArguments(args);
                fragDialog.show(fragmentManager,"FragDialogAmountCalculated");
            }

        }catch (JSONException e) {
            setupMessageToast(R.string.error_request_Text);
        }
    }

}// Fin de la clase
