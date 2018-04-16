package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.example.developermicalisoft.apis.Services.UserInterfaceSvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class FragmentTravelItinerary extends Fragment {


    private View travelItineraryLayout;
    private EditText departureDate, returnDate;
    private EmbeddedDatepicker departureDatepicker, returnDatepicker;
    private FloatingActionButton countryAddButton, countryRemoveButton;
    private Spinner countriesSpinner;
    private Button fileTravelButton, cancelButton;
    private String selectedCountry, selectedCountryList;
    private CountriesToAdapter countriesAdapter;
    private SelectCountryToAdapter selectCountryAdapter;
    private ArrayList<String> selectedCountryArray;
    private Toast messageToast;
    private ListView countriesListView;
    private ArrayList<String> data;
    private String formatDate;

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

        // Infla la vista correspondiente
        travelItineraryLayout = inflater.inflate(R.layout.fragment_travel_itinerary, container, false);

        // Obtiene items del diseño
        departureDate         = travelItineraryLayout.findViewById(R.id.departureDate_Edit);
        returnDate            = travelItineraryLayout.findViewById(R.id.returnDate_Edit);
        countryAddButton      = travelItineraryLayout.findViewById(R.id.country_add_Button);
        countryRemoveButton   = travelItineraryLayout.findViewById(R.id.country_remove_Button);
        countriesSpinner      = travelItineraryLayout.findViewById(R.id.countries_spinner);
        countriesListView     = travelItineraryLayout.findViewById(R.id.countries_List);
        fileTravelButton      = travelItineraryLayout.findViewById(R.id.fileTravelItinerary_Button);
        cancelButton          = travelItineraryLayout.findViewById(R.id.cancel_Button);
        formatDate            = getResources().getString(R.string.formatDate_label);

        // Instancia los objetos necesarios para la correcta interaccion con el usuario
        departureDatepicker  = new EmbeddedDatepicker(getActivity(), departureDate);
        returnDatepicker     = new EmbeddedDatepicker(getActivity(), returnDate);
        selectCountryAdapter = new SelectCountryToAdapter(getActivity());
        countriesAdapter     = new CountriesToAdapter(getActivity());
        messageToast         = Toast.makeText(getActivity(),"", Toast.LENGTH_SHORT );

        // Configura los eventos de escucha
        listenerEventsSetup();

        return travelItineraryLayout;
    }

    /* Soluciona que los items del diseño se puedan refrescar
     * de forma correcta con el nuevo valor.
     */
    @Override
    public void onResume(){
        super.onResume();

        // Configura titulo del toolbar
        switch( this.data.get(1) ){
            case "create":
                ForeignExchange.setupToolbarText(R.string.create_travel_itinerary_title);
                departureDate.setText(formatDate);
                returnDate.setText(formatDate);
                countriesAdapter.removeCountry("Colombia");
                countriesSpinner.setAdapter(countriesAdapter.getAdapter());
                break;
            case "update":
                ForeignExchange.setupToolbarText(R.string.update_travel_itinerary_title);
                if( this.data.size() == 2  ){

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_MONTH, 2);

                    String formattedDate = simpleDateFormat.format(calendar.getTime());
                    departureDate.setText(formattedDate);

                    calendar.add(Calendar.DAY_OF_MONTH, 15);
                    formattedDate = simpleDateFormat.format(calendar.getTime());
                    returnDate.setText(formattedDate);

                    this.data.add("Brazil");
                    this.data.add("Chile");
                }

                fileTravelButton.setText(R.string.updateTravelItinerary_btn_text);
                countriesSpinner.setAdapter(countriesAdapter.getAdapter());

                ArrayList<String> countriesSpinnerArray = countriesAdapter.getCountriesArray();
                for( String country: this.data ){
                    if( countriesSpinnerArray.contains(country) ){
                        countriesListView.setAdapter(selectCountryAdapter.getAdapter(country,true));
                    }
                }
                break;
        }// Fin del switch
    }

    /* Metodo que configura los eventos de escucha de los
     * items del diseño.
     */
    private void listenerEventsSetup(){

        // Declara el evento de escucha del item departureDate_Edit
        departureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                departureDatepicker.show();
            }
        });

        // Declara el evento de escucha del item returnDate_Edit
        returnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnDatepicker.show();
            }
        });

        // Declara el evento de escucha del item countrie_add_Button
        countryAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedCountry = countriesSpinner.getSelectedItem().toString();
                if( selectedCountry.equals(getResources().getString(R.string.country_select_prompt)) ){
                    // Mensaje de aviso al usuario
                    setupMessageToast(R.string.country_select_Text);
                }else{
                    selectedCountryArray = selectCountryAdapter.getCountriesArray();
                    if( selectedCountryArray.contains(selectedCountry) ){
                        // Mensaje de aviso al usuario
                        setupMessageToast(R.string.country_selected_exist_Text);
                    }else{
                        countriesListView.setAdapter(selectCountryAdapter.getAdapter(selectedCountry,true));
                    }
                }
            }
        });

        // Declara el evento de escucha del item country_remove_Button
        countryRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( selectedCountryList == "" || selectedCountryList == null ){
                    // Mensaje de aviso al usuario
                    setupMessageToast(R.string.country_no_selected_list_Text);
                }else{
                    countriesListView.setAdapter(selectCountryAdapter.getAdapter(selectedCountryList,false));
                    selectedCountryList = "";
                }
            }
        });

        // Declara el evento de escucha del item countries_List
        countriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCountryList = adapterView.getItemAtPosition(i).toString();
            }
        });

        // Declara el evento de escucha del item fileTravelItinerary_Button
        fileTravelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  typeMsg      = "A";
                boolean fileCreate   = true;
                selectedCountryArray = selectCountryAdapter.getCountriesArray();

                if( String.valueOf(departureDate.getText()).equals(formatDate) ){
                    fileCreate = false;
                }else
                if( String.valueOf(returnDate.getText()).equals(formatDate)){
                    fileCreate = false;
                }else
                if( selectedCountryArray.isEmpty() ){
                    fileCreate = false;
                }else
                if( data.get(1) == "update" ){
                    if( ! selectedCountryArray.contains("Colombia") ){
                        typeMsg = "B";
                        fileCreate = false;
                    }
                }

                if( fileCreate ){

                    // Configura titulo del toolbar
                    switch( data.get(1) ){
                        case "create":
                            // Mensaje de aviso al usuario
                            setupMessageToast(R.string.travel_itinerary_created);
                            break;
                        case "update":
                            // Mensaje de aviso al usuario
                            setupMessageToast(R.string.travel_itinerary_update);
                            break;
                    }// Fin del switch

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if( messageToast.getView().isShown() ){
                                handler.postDelayed(this, 1000);
                            }else{
                                // Configura titulo del toolbar
                                switch( data.get(1) ){
                                    case "create":
                                        launchTransaction("B");
                                        break;
                                    case "update":
                                        launchTransaction("F");
                                        break;
                                }// Fin del switch
                            }
                        }
                    },1000);
                }else{
                    // Identifica el tipo de mensaje
                    switch (typeMsg){
                        case "A":
                            // Mensaje de aviso al usuario
                            setupMessageToast(R.string.data_required_Text);
                            break;
                        case "B":
                            // Mensaje de aviso al usuario
                            setupMessageToast(R.string.colombia_required_Text);
                            break;
                    }// Fin del switch
                }
            }
        });

        // Declara el evento de escucha del item cancel_Button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Configura titulo del toolbar
                switch( data.get(1) ){
                    case "create":
                        launchTransaction("A");
                        break;
                    case "update":
                        launchTransaction("B");
                        break;
                }// Fin del switch
            }
        });

    }// fin de listenerEventsSetup

    /* Metodo encargdo de lanzar la transaccion Welcome con su
     * correspondiente parametro.
     */
    private void launchTransaction( String dialog ){

        ArrayList<String> data = new ArrayList<>();

        // Identifica el dialogo a crear.
        switch( dialog ){
            case "A":
                data.add(dialog);
                data.add("create");
                break;
            case "B":
                data.add(dialog);
                data.add("update");
                selectedCountryArray = selectCountryAdapter.getCountriesArray();
                for( String value: selectedCountryArray ){
                    data.add(value);
                }
                break;
            case "F":
                data.add(dialog);
                data.add("ForeignExchange");
                break;
        }// Fin del switch

        Bundle args = new Bundle();
        args.putStringArrayList("data",data);

        Fragment welcomeFragment = ForeignExchange.getWelcomeFragment();
        welcomeFragment.setArguments(args);
        FragmentToTransaction.commit( ForeignExchange.getActivityMain(), welcomeFragment );
    }

    /* Configura los mensajes de aviso o alerta
     * para el usuario */
    private void setupMessageToast( int idString ){

        if( messageToast.getView().isShown() ){
            messageToast.cancel();
        }
        messageToast.setText(idString);
        messageToast.show();
    }




}// Fin clase principal
