package com.example.developermicalisoft.apis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.developermicalisoft.apis.Modules.cardOnFile.CardOnFile;
import com.example.developermicalisoft.apis.Modules.foreignExchange.ForeignExchange;
import com.example.developermicalisoft.apis.Modules.foreignExchange.FragmentToTransaction;

public class Main extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FragmentManager fragmentManager = null;
    private ActionBar actionBar;

    private static Toolbar toolbar;
    private static TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        toolbar         = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle    = (TextView) findViewById(R.id.toolbar_title);
        drawerLayout    = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView  = (NavigationView) findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        showToolbar();

        setDrawerLayout(navigationView);

        // Se hace la seleccion del primer item del menu.
        showFragment(navigationView.getMenu().getItem(0));

    }// Fin onCreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar PayPalItem clicks here.
        int id = item.getItemId();
        drawerLayout.openDrawer(GravityCompat.START); /*Opens the Right Drawer*/
        return true;
    }

    /* INIT FUNCTIONS*/

    private void setDrawerLayout(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                showFragment(item);
                drawerLayout.closeDrawers();
                return false;
            }
        });
    }// end setDrawerLayout

    public void showFragment(MenuItem item) {

        Fragment newfragment = null;
        fragmentManager = getSupportFragmentManager();

        switch (item.getItemId()) {
            case R.id.cardOnFile:
                newfragment = new CardOnFile();
                break;
            case R.id.foreignExchange:
                newfragment = new ForeignExchange();
                break;
        }

        if (newfragment != null) {
            fragmentManager.beginTransaction().
                    replace(R.id.fragment_container, newfragment)
                    .setTransition(FragmentTransaction.TRANSIT_EXIT_MASK)
                    .commit();
        }

        if( item.isChecked() == false ){
            item.setChecked( true );
        }

    }// FIn showFragment

    // Se configura el toolbar
    private void showToolbar() {
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }// Fin showToolbar

    // Metodo que configura el titulo del toolbar
    public static void setupToolbarText( int id ){
        toolbarTitle.setText( id );
    }

    @Override
    public void onBackPressed() {

        String currentCheckPoint = ForeignExchange.getCheckPointApp();

        // Indentifica el punto de control
        switch( currentCheckPoint ){
            case "A":
            case "B":
                // No realizar ninguna accion
                break;
            case "F":
                // Inicia de nuevo
                Fragment welcomeFragment = ForeignExchange.getWelcomeFragment();
                welcomeFragment.setArguments(null);
                FragmentToTransaction.commit( ForeignExchange.getActivityMain(), welcomeFragment );
                break;
            default:
               super.onBackPressed();  // Invoca al método
                break;
        }// Fin del switch
    }

}
