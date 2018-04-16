package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.developermicalisoft.apis.R;

import java.util.ArrayList;

public class FragmentWelcome  extends Fragment{

    private View welcomeLayout;
    private ArrayList<String> data;

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
        welcomeLayout  = inflater.inflate(R.layout.fragment_welcome, container, false);

        // Configura titulo del toolbar
        ForeignExchange.setupToolbarText(R.string.welcome_title);

        // Ejecuta y verifica el progress Bar
        ForeignExchange.runProgBar();
        ForeignExchange.setupVerifyProgBar( this.data );

        return welcomeLayout;
    }

}
