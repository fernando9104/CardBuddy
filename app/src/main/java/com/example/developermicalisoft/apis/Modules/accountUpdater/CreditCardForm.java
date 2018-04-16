package com.example.developermicalisoft.apis.Modules.accountUpdater;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.developermicalisoft.apis.R;
import com.example.developermicalisoft.apis.Services.BuildUrl;
import com.example.developermicalisoft.apis.Services.Constants;
import com.example.developermicalisoft.apis.Services.UrlConection;
import com.example.developermicalisoft.apis.Services.UserInterfaceSvc;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class CreditCardForm extends Fragment {

    private String TAG_LOG = "Print CreditCardForm";
    View creditCardLayout;
    TextInputLayout layout_pan, layout_expDate;
    TextInputEditText pan, expDate;
    Button buttonSend;
    private BroadcastReceiver creditCardDP;
    private String returnAction;
    private Map<String, String> respServer;
    private FrameLayout progressOn;
    private String stage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        stage = "get Components";
        creditCardLayout = inflater.inflate(R.layout.credit_card_form, container, false);

        pan = creditCardLayout.findViewById(R.id.pan);
        expDate = creditCardLayout.findViewById(R.id.expDate);
        buttonSend = creditCardLayout.findViewById(R.id.buttonSend);
        progressOn = creditCardLayout.findViewById(R.id.layout_progressOn_creditCardForm);

        layout_pan = creditCardLayout.findViewById(R.id.layout_pan);
        layout_expDate = creditCardLayout.findViewById(R.id.layout_expDate);

        // Add Events
        buttonSend.setOnClickListener(onClickButton);

        return creditCardLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stage = "onViewCreated";
        getResponse();
    }// Fin onViewCreated


    View.OnClickListener onClickButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            stage = "onClickButton";

            boolean isValid = attemptSubmit();
            if (isValid) {
                layout_pan.setError(null);
                layout_expDate.setError(null);
                layout_pan.setErrorEnabled( false );
                layout_expDate.setErrorEnabled( false );
                progressOn.setVisibility(View.VISIBLE);
                submitForm();
            }


        }
    };// Fin onClickButton

    private boolean attemptSubmit() {

        boolean isValid = true;

        try {

            String panField = pan.getText().toString();
            String expDateField = expDate.getText().toString();

            if (TextUtils.isEmpty(panField)) {
                layout_pan.setError("The field cannot be empty");
                isValid = false;
            }

            if (TextUtils.isEmpty(expDateField)) {
                layout_expDate.setError("The field cannot be empty");
                isValid = false;
            }

        } catch (Exception e) {

            UserInterfaceSvc.showMsgError(getContext(), null, e.getMessage());
        }

        return isValid;
    }//attemptSubmit

    private void submitForm() {

        try {

            String url = BuildUrl.getStringUrl(Constants.API_VAU);

            JSONObject stringParameters = new JSONObject();

            stringParameters.put("pan", pan.getText().toString());
            stringParameters.put("expDate", expDate.getText().toString());

            UrlConection.request(getActivity(), url, "POST", stringParameters);

        } catch (Exception e) {

            UserInterfaceSvc.showMsgError(getContext(), null, stage + " : " + e.getMessage());
        }
    }// Fin submitForm

    private void getResponse() {

        creditCardDP = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {

                    returnAction = intent.getAction();
                    stage = "action " + returnAction.toString();
                    switch (returnAction) {

                        case Constants.ACTION_SUCCESS:
                            respServer = (Map<String, String>) intent.getSerializableExtra(Constants.DATA_FROM_SERVER);
                            getData(respServer);
                            break;
                        case Constants.ACTION_ERROR:
                            respServer = (Map<String, String>) intent.getSerializableExtra(Constants.DATA_FROM_SERVER);
                            UserInterfaceSvc.showMsgError(getContext(), getString(R.string.title_home), respServer.get("result"));
                            break;
                        case Constants.ACTION_FAIL:
                            UserInterfaceSvc.showMsgError(getContext(), getString(R.string.title_home), intent.getStringExtra("responseRequest") );
                            break;
                    }// Fin switch

                } catch (Exception e) {

                    UserInterfaceSvc.showMsgError(getContext(), null, stage + " : " + e.getMessage());
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
        getContext().registerReceiver(creditCardDP, defineActionDp);
    }// Fin getResponse

    private void getData(Map<String, String> respServer) {

        try {

            JSONObject response = new JSONObject(respServer.get("result"));

            String requestMessageId = response.getString("requestMessageId");
            String messageDateTime = response.getString("messageDateTime");
            String numRecordsReturned = response.getString("numRecordsReturned");
            String responseMessageId = response.getString("responseMessageId");
            String statusDescription = response.getString("statusDescription");
            String statusCode = response.getString("statusCode");

            startActivity(new Intent(getContext(), Resume.class)
                    .putExtra("requestMessageId", requestMessageId)
                    .putExtra("messageDateTime", messageDateTime)
                    .putExtra("numRecordsReturned", numRecordsReturned)
                    .putExtra("responseMessageId", responseMessageId)
                    .putExtra("statusDescription", statusDescription)
                    .putExtra("statusCode", statusCode)
            );

        } catch (JSONException e) {

            UserInterfaceSvc.showMsgError(getContext(), null, stage + " : " + e.getMessage());
        }// Fin try/catch

    }// Fin getData


}
