package com.example.developermicalisoft.apis.Modules.foreignExchange;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.developermicalisoft.apis.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EmbeddedDatepicker {

    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private Activity activity;
    private EditText editText;
    private DatePickerDialog datePickerDialog;

    /* Metodo Constructor de la clase */
    public EmbeddedDatepicker( Activity activity, EditText editText ) {

        this.calendar           = Calendar.getInstance();
        this.activity           = activity;
        this.editText           = editText;
        this.simpleDateFormat   = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK );
        embedded();
    }

    /* Metodo que instancia un Datepicker para configurarlo
     * con la actividad y el evento escucha del datepicker.
     */
    private void embedded(){

        this.datePickerDialog =
                new DatePickerDialog(
                        this.activity,
                        R.style.datepicker,
                        listenerSetup(),
                        this.calendar.get(Calendar.YEAR),
                        this.calendar.get(Calendar.MONTH),
                        this.calendar.get(Calendar.DAY_OF_MONTH) );
    }

    /* Metodo que configura el evento escucha del datepicker.
     */
    private DatePickerDialog.OnDateSetListener listenerSetup(){

        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editText.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
    }

    /* Metodo que muestra el datepicker correspondiente
     */
    public void show(){
        this.datePickerDialog.show();
    }

}// Fin de la clase

