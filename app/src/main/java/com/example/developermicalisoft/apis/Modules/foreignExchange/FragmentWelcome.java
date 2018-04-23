package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.developermicalisoft.apis.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FragmentWelcome  extends Fragment{

    private View welcomeLayout;
    private ArrayList<String> data;
    private TextView welcomeTextView;

    // Obtengo parametros pasados al fragment.
    @Override
    public void onCreate( Bundle bundle ) {
        super.onCreate(bundle);
        if( getArguments() == null ){
            this.data = new ArrayList<>();
            this.data.add("A");
            this.data.add("create");
        }else{
            this.data = getArguments().getStringArrayList("data");
        }
    }

    // Metodo principal o constructor
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Obtiene items del diseño
        welcomeLayout   = inflater.inflate(R.layout.fragment_welcome, container, false);
        welcomeTextView = welcomeLayout.findViewById(R.id.welcome_Text);

        // Configura titulo del toolbar
        ForeignExchange.setupToolbarText(R.string.welcome_title);

        // Obtiene String label
        String welcomeLabel = getString(R.string.welcome_label);

        // Indetifica el caso
        switch( data.get(1) ){
            case "create":
                welcomeLabel = welcomeLabel + " Brazil";
                welcomeTextView.setText(welcomeLabel);
                break;
            case "update":
            case "ForeignExchange":
                welcomeLabel = welcomeLabel + " Colombia";
                welcomeTextView.setText(welcomeLabel);
                break;
        }//Fin del switch

        // Ejecuta y verifica el progress Bar
        ForeignExchange.runProgBar();
        ForeignExchange.setupVerifyProgBar( this.data );

        return welcomeLayout;
    }

}
