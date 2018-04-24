package com.example.developermicalisoft.apis.Modules.cardOnFile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.developermicalisoft.apis.Main;
import com.example.developermicalisoft.apis.R;
import com.example.developermicalisoft.apis.Services.AlertDialogs;
import com.example.developermicalisoft.apis.Services.BuildUrl;
import com.example.developermicalisoft.apis.Services.Constants;
import com.example.developermicalisoft.apis.Services.UrlConection;
import com.example.developermicalisoft.apis.Services.UserInterfaceSvc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CardOnFile extends Fragment {

    private String stage, returnAction;
    private BroadcastReceiver cofDP;
    private Map<String, String> respServer;
    private String LOG_COF = "Print COF";
    private List<CardOnFileModel> cardOnfileValues;
    private CardOnFileAdapter cardOnFileAdapter;
    private FrameLayout progressOn, noDataLayout;
    private Context context;
    private String nameRequest = "loadCOF";
    private TextView issuerName, numberCreditCard, msg_updCreditCardInfo;
    private Button button_cancelRecurring;
    CardView oldCardView = null;    // Guarda el ultimo cardView seleccionado.
    CardOnFileAdapter.CardOnFileHolder cardViewSelected = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View cardOnFileView = inflater.inflate(R.layout.card_on_file, container, false);
        context = getContext();
        progressOn = cardOnFileView.findViewById(R.id.layout_progressOnCardOnFile);
        noDataLayout = cardOnFileView.findViewById(R.id.noData);
        issuerName = cardOnFileView.findViewById(R.id.issuerName);
        numberCreditCard = cardOnFileView.findViewById(R.id.numberCreditCard);
        button_cancelRecurring = cardOnFileView.findViewById(R.id.button_cancel_recurring);
        msg_updCreditCardInfo = cardOnFileView.findViewById(R.id.msg_state_credit_card);
        // Se obtiene el recycler
        RecyclerView cardOnFileRecycler = cardOnFileView.findViewById(R.id.cardOnFileRecycler);
        cardOnFileRecycler.setHasFixedSize(true);

        // Se configura la manera en que se mostrara la informacion en el recycler.
        RecyclerView.LayoutManager layoutManagerCardOnFile = new LinearLayoutManager(getActivity());
        // Configuro el recicler con el linear layout
        cardOnFileRecycler.setLayoutManager(layoutManagerCardOnFile);

        // Inicializacion del adapater
        cardOnfileValues = new ArrayList<>();
        cardOnFileAdapter = new CardOnFileAdapter(cardOnfileValues);
        cardOnFileAdapter.setOnSelectCardView(onClickItem);
        cardOnFileRecycler.setAdapter(cardOnFileAdapter);

        // Add Events
        button_cancelRecurring.setOnClickListener(onButtonClick);

        return cardOnFileView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Main.setupToolbarTitle(R.string.title_credit_charges);
        listenerResponse();
    }

    @Override
    public void onStop() {
        super.onStop();
        context.unregisterReceiver(cofDP);
    }// Fin onStop

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressOn.setVisibility(View.VISIBLE);
        loadData();
    }// Fin onViewCreated

    public void disableRegister(){
        if( cofDP != null ){
            context.unregisterReceiver(cofDP);
        }

    }

    public void unRegisterReceiver(){
        disableRegister();
    }

    /* INIT EVENTS */

    /**
     * <p><b>Es:</b>Evento onTouch o onclik del cardView</p>
     */
    private final CardOnFileAdapter.OnSelectCardView onClickItem = new CardOnFileAdapter.OnSelectCardView() {
        @Override
        public void onClickWarningButton(CardOnFileAdapter.CardOnFileHolder viewHolder, CardView itemCard) {
            //Toast.makeText(context, "Toco en warning", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClickChechButton(CardOnFileAdapter.CardOnFileHolder viewHolder, CardView itemCard) {
            //Toast.makeText(context, "Toco en check", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSelctCardView(CardOnFileAdapter.CardOnFileHolder viewHolder, CardView itemCard) {

            cardViewSelected = viewHolder;

            if (viewHolder.iconCheck.isShown()) {
                msg_updCreditCardInfo.setTextColor(getResources().getColor(R.color.green_text));
                msg_updCreditCardInfo.setText(getString(R.string.updated_credit_card_information));
                msg_updCreditCardInfo.setVisibility(View.VISIBLE);
                button_cancelRecurring.setVisibility(View.VISIBLE);
            }// Fin if iconCheck.isShown

            if (viewHolder.iconWarnin.isShown()) {
                msg_updCreditCardInfo.setTextColor(getResources().getColor(R.color.red_text));
                msg_updCreditCardInfo.setText(getString(R.string.outdate_credit_card_information));
                msg_updCreditCardInfo.setVisibility(View.VISIBLE);
                button_cancelRecurring.setVisibility(View.VISIBLE);
            }// fin if iconWarnin.isShown

            if (viewHolder.iconNoRecurringCharge.isShown()) {
                button_cancelRecurring.setVisibility(View.GONE);
                msg_updCreditCardInfo.setVisibility(View.GONE);
            }// Fin if iconNoRecurringCharge.isShown

            // Se configura el background color para marcar como seleccioando el cargView
            viewHolder.cardOnfileCardView.setCardBackgroundColor(getResources().getColor(R.color.colorListSelector));

            if ( oldCardView == null ) {
                oldCardView = itemCard;
            }// Se configura el cardView

            if ( oldCardView != itemCard ) {
                oldCardView.setCardBackgroundColor(getResources().getColor(R.color.cardview_light_background));
                oldCardView = itemCard;
            }

        }// Fin onSelctCardView

    };// Fin onClickItem

    /**
     * <p><b>Es: </b>Evento onclick del boton</p>
     */
    View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            cancelMerchant();
        }
    };// Fin onButtonClick

    /* END EVENTS */

    /**
     * <p><b>Es: </b>Funcion encarga de hacer la solicitud de los datos con la API COF</p>
     */
    private void loadData() {

        try {

            stage = "Create Params";
            nameRequest = "loadCOF";

            String url = BuildUrl.getStringUrl(Constants.API_COF);

            JSONObject stringParameters = new JSONObject();
            stringParameters.put("pan", "4000004298044051");
            UrlConection.request(getActivity(), url, "POST", stringParameters);

        } catch (Exception e) {

            UserInterfaceSvc.showMsgError(context, null, stage + " : " + e.getMessage());
        }
    }// Fin loadData

    /**
     * <p><b>Es: </b>Funcion encarga de hacer la solicitud de los datos con la API GENERAL INQUIRY </p>
     */
    private void loadGeneralInquiry() {

        try {

            stage = "Create Params loadGeneralInquiry";
            nameRequest = "loadGeneral";
            String url = BuildUrl.getStringUrl(Constants.API_GENERAL_INQUIRY);

            JSONObject stringParameters = new JSONObject();
            stringParameters.put("primaryAccountNumber", "4815070000000000");
            UrlConection.request(getActivity(), url, "POST", stringParameters);

        } catch (Exception e) {
            UserInterfaceSvc.showMsgError(context, null, stage + " : " + e.getMessage());
        }// Fin try/catch
    }// Fin loadGeneralInquiry

    /**
     * <p><b>Es: </b>Funcion que actua como listener o callback. Se encarga de recibir los datos
     * enviados desde el servidor.</p>
     */
    private void listenerResponse() {

        cofDP = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {

                    returnAction = intent.getAction();
                    stage = "action " + returnAction.toString();
                    switch (returnAction) {

                        case Constants.ACTION_SUCCESS:

                            switch (nameRequest) {

                                case "loadCOF":
                                    stage += stage + " " + nameRequest;
                                    respServer = (Map<String, String>) intent.getSerializableExtra(Constants.DATA_FROM_SERVER);
                                    getData(respServer);
                                    loadGeneralInquiry();
                                    break;
                                case "loadGeneral":
                                    stage += stage + " " + nameRequest;
                                    respServer = (Map<String, String>) intent.getSerializableExtra(Constants.DATA_FROM_SERVER);
                                    publicLoadGeneral(respServer);
                                    break;
                            }// Fin switch
                            break;

                        case Constants.ACTION_ERROR:
                            respServer = (Map<String, String>) intent.getSerializableExtra(Constants.DATA_FROM_SERVER);
                            if (noDataLayout.getVisibility() == View.GONE) {
                                noDataLayout.setVisibility(View.VISIBLE);
                                UserInterfaceSvc.showMsgError(context, getString(R.string.title_credit_charges), respServer.get("result"));
                            }
                            break;

                        case Constants.ACTION_FAIL:
                            if (noDataLayout.getVisibility() == View.GONE) {
                                noDataLayout.setVisibility(View.VISIBLE);
                                UserInterfaceSvc.showMsgError(context, context.getResources().getString(R.string.title_credit_charges), intent.getStringExtra("responseRequest").toString());
                            }
                            break;
                    }// Fin switch

                } catch (Exception e) {

                    UserInterfaceSvc.showMsgError(context, null, stage + " : " + e.getMessage());
                }// Fin try/catch

                progressOn.setVisibility(View.GONE);
            }// Fin onReceive
        }; // fin BroadcastReceiver

        // En: Creation of the Filter intent or medium to catch the actions of the BroadcastReceiver
        // Es: Creacion del inten Filter o medio para atrapar las acciones del BroadcastReceiver
        IntentFilter defineActionDp = new IntentFilter();
        // Se definen las acciones a recibir.
        defineActionDp.addAction(Constants.ACTION_SUCCESS);
        defineActionDp.addAction(Constants.ACTION_FAIL);
        defineActionDp.addAction(Constants.ACTION_ERROR);
        context.registerReceiver(cofDP, defineActionDp);

    }// Fin listenerResponse

    /**
     * <p><b>Es: </b>Funcion para obtener los datos enviados desde el servidor</p>
     *
     * @param respServer
     */
    private void getData(Map<String, String> respServer) {

        try {

            String data = respServer.get("data");

            if (!data.equals("null")) {

                stage = "get merchants";
                JSONArray merchantsArray = new JSONArray(respServer.get("data"));

                stage = "publicData";
                publicData(merchantsArray);

            } else {

                noDataLayout.setVisibility(View.VISIBLE);
            }// Fin if

        } catch (Exception e) {

            UserInterfaceSvc.showMsgError(context, null, stage + " : " + e.getMessage());
        }// Fin try/catch

    }// Fin getData

    /**
     * <p><b>Es: </b>Funcion para poblar el cardView con los datos recibidos desde el servidor.
     * desde la API COF</p>
     *
     * @param merchantsArray
     */
    private void publicData(JSONArray merchantsArray) {

        try {

            int indexArray = 0;
            cardOnfileValues.clear();
            while (indexArray <= merchantsArray.length() - 1) {

                JSONObject thisRec = merchantsArray.getJSONObject(indexArray);
                Iterator<String> iterThisRec = thisRec.keys();

                if (iterThisRec.hasNext()) {
                    cardOnfileValues.add(new CardOnFileModel(thisRec));
                }// Fin iterThisRec

                indexArray++;
            }// Fin while
            cardOnFileAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            UserInterfaceSvc.showMsgError(context, null, stage + " : " + e.getMessage());
        }// FIn try/catch

    }// Fin publicData

    /**
     * <p><b>Es: </b>Funcion para poblar el cardView con los datos recibidos desde el servidor.
     * desde la API GENERAL INQUIRY</p>
     *
     * @param respServer
     */
    private void publicLoadGeneral(Map<String, String> respServer) {

        try {

            JSONObject jsonObject = new JSONObject(respServer.get("result"));
            issuerName.setText(jsonObject.get("issuerName").toString());
            numberCreditCard.setText(context.getResources().getString(R.string.credit_ending) + " 0000");

        } catch (JSONException e) {

            UserInterfaceSvc.showMsgError(context, null, stage + " : " + e.getMessage());
        }
        //cardProductName
    }// Fin publicLoadGeneral

    /**
     * <p><b>Es: </b>Funcion para mostrar el mensaje de advertencia para cancelar el marchant</p>
     */
    private void cancelMerchant() {

        AlertDialogs.confirmationAlert(getContext(), "ic_warning_two", getString(R.string.msg_title_cancel_merchant), getString(R.string.msg_cancel_merchan), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cancelRecurringCharge();
            }
        }, "Yes");

    }// Fin cancelMerchant

    /**
     * <p><b>Es: </b>Funcion que muestra el mensade de abvertencia para cancelar el pago recurrente</p>
     */
    private void cancelRecurringCharge() {

        AlertDialogs.confirmationAlert(getContext(), "ic_warning_two", getString(R.string.msg_cancel_recurring_charge), getString(R.string.msg_delete_recurring_charge), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                cardViewSelected.iconWarnin.setVisibility(View.GONE);
                cardViewSelected.iconCheck.setVisibility(View.GONE);
                cardViewSelected.iconNoRecurringCharge.setVisibility(View.VISIBLE);
                button_cancelRecurring.setVisibility(View.GONE);
                msg_updCreditCardInfo.setVisibility(View.GONE);
            }
        }, null);

    }// Fin cancelRecurringCharge


}// Fin clase principal.
