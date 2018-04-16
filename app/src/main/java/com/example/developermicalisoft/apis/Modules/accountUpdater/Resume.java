package com.example.developermicalisoft.apis.Modules.accountUpdater;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;

import com.example.developermicalisoft.apis.R;
import com.example.developermicalisoft.apis.Services.UserInterfaceSvc;

public class Resume extends AppCompatActivity {

    TextInputEditText numRecordsReturned, statusDescription,statusCode;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume);

        UserInterfaceSvc.setToolbar(Resume.this, getString(R.string.title_activity_resume ), true, "ic_close");

        numRecordsReturned = findViewById(R.id.numRecordsReturned);
        statusDescription = findViewById(R.id.statusDescription);
        statusCode = findViewById(R.id.statusCode);

        // Se obtienen los datos enviados desde la vista o actividad padre.
        Intent paramsIntent = getIntent();

        numRecordsReturned.setText( paramsIntent.getStringExtra("numRecordsReturned") );
        statusDescription.setText( paramsIntent.getStringExtra("statusDescription") );
        statusCode.setText( paramsIntent.getStringExtra("statusCode") );

    }// Fin if onCreate

}// Fin Resume
