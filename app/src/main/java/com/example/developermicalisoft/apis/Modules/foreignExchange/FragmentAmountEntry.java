package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
    private CountriesToAdapter countriesAdapter;
    private FloatingActionButton amountAddButton, amountRemoveButton;
    private EditText amountEdit;
    private Integer entryAmount;
    private int selectedAmountList;
    private Button calculateButton;
    private String defaultValueSpinner;

    private static ArrayList<Integer> selectedAmountsArray;
    private static AmountEntryToAdapter amountsEntryAdapter;
    private static ListView amountsListView;
    private static HashMap<String,String> iSOCountries;
    private static String selectedCountry;
    private static EditText totalAmountEdit;
    private static FragmentManager fragmentManager;
    private static Toast messageToast;

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
        amountEdit            = amountEntryLayout.findViewById(R.id.amount_Edit);
        countriesSpinner      = amountEntryLayout.findViewById(R.id.countries_spinner);
        amountAddButton       = amountEntryLayout.findViewById(R.id.amount_add_Button);
        amountRemoveButton    = amountEntryLayout.findViewById(R.id.amount_remove_Button);
        amountsListView       = amountEntryLayout.findViewById(R.id.amounts_List);
        totalAmountEdit       = amountEntryLayout.findViewById(R.id.total_amount_Edit);
        calculateButton       = amountEntryLayout.findViewById(R.id.calculate_Button);
        defaultValueSpinner   = getResources().getString(R.string.country_select_prompt);

        // Instancia los objetos necesarios para la correcta interaccion con el usuario
        countriesAdapter      = new CountriesToAdapter(getActivity());
        amountsEntryAdapter   = new AmountEntryToAdapter(getActivity());
        messageToast          = Toast.makeText(getActivity(),"", Toast.LENGTH_SHORT );
        fragmentManager       = getFragmentManager();
        selectedAmountList    = -1;

        // Configura Pais
        countriesSpinner.setAdapter(countriesAdapter.getAdapter());
        countriesSpinner.setSelection(getCountryPosition("Colombia"));

        // Configura los eventos de escucha
        listenerEventsSetup();
        calculateTotalAmount();

        return amountEntryLayout;
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

                    iSOCountries = countriesAdapter.getISOCountries();

                    try {

                        JSONObject params = new JSONObject();

                        params.put("country", iSOCountries.get(selectedCountry));
                        params.put("amount", totalAmountEdit.getText().toString());
                        ConnectionAsyncTask.request( params );

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    /* Configura los mensajes de aviso o alerta
     * para el usuario.
     */
    private static void setupMessageToast( int idString ){

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

            String statusCode = respRequest.getString("code");

            switch( statusCode ){
                case "200":
                    String conversionRate   = respRequest.getString("conversionRate");
                    String result           = respRequest.getString("result");

                    // Crea el arreglo con los datos ingresados
                    ArrayList<String> data = new ArrayList<>();
                    data.add(iSOCountries.get(selectedCountry));        //ISO
                    data.add(selectedCountry);                          //Ciudad
                    data.add(totalAmountEdit.getText().toString());     //Cantidad
                    data.add(conversionRate);                           //ConversionRate
                    data.add(result);                                   //Cantidad resultante

                    // Crea objeto clave-valor para argumentos
                    Bundle args = new Bundle();
                    args.putStringArrayList( "data",data );

                    FragDialogAmountCalculated fragDialog = new FragDialogAmountCalculated();
                    fragDialog.setArguments(args);
                    fragDialog.show(fragmentManager,"FragDialogAmountCalculated");
                    break;
                default:
                    throw new RuntimeException();
            }// fin del switch

        }catch (JSONException e) {

            e.printStackTrace();
        }catch ( Exception e ){

            // Mensaje de aviso al usuario
            setupMessageToast(R.string.error_request_Text);
        }
    }

}// Fin de la clase
