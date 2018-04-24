package com.example.developermicalisoft.apis;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.developermicalisoft.apis.Services.UserInterfaceSvc;

public class Splash extends AppCompatActivity {

    private int waitTime = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN );

        Handler handler     = new Handler();
        Runnable runnable;

        UserInterfaceSvc.CreateShortcut( getApplicationContext() );

        handler.postDelayed( runnable = new Runnable() {
            @Override
            public void run() {
                startActivity( new Intent( Splash.this, Main.class)
                .addFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }, waitTime);

    }

}
