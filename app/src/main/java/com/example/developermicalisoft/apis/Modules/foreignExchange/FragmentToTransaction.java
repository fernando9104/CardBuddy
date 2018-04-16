package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.developermicalisoft.apis.R;

public class FragmentToTransaction extends FragmentActivity {

    public static void commit (Activity activity, Fragment layout ){

        FragmentManager fragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction fragTransaction = fragmentManager.beginTransaction();
        fragTransaction.replace(R.id.fragm_main_container, layout).addToBackStack(null);
        fragTransaction.commit();
    }
}// fin de la clase
