package com.example.developermicalisoft.apis.Modules.cardOnFile;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.developermicalisoft.apis.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CardOnFileAdapter extends Adapter<CardOnFileAdapter.CardOnFileHolder> {

    private List<CardOnFileModel> cardOnFileValues = new ArrayList<>();
    public CardOnFileAdapter(List<CardOnFileModel> cardOnFileValues) {
        this.cardOnFileValues = cardOnFileValues;
    }
    private SimpleDateFormat parseDate = new SimpleDateFormat("yyyy-mm-dd");
    private SimpleDateFormat formatDate = new SimpleDateFormat("d-mm-yyyy");

    public class CardOnFileHolder extends RecyclerView.ViewHolder {

        TextView merchaName, lastTransaction, labelImg;
        ImageView iconCheck, iconWarnin;

        public CardOnFileHolder(View itemView) {
            super(itemView);

            // Obtenfo el cardiView.
            CardView cardOnfileCardView = itemView.findViewById(R.id.cardOnFileCardView);
            // Se obtienen los campos del cardView.
            merchaName = itemView.findViewById(R.id.mrchName);
            lastTransaction = itemView.findViewById(R.id.lastMrchTranDt);
            iconCheck   = itemView.findViewById(R.id.icon_check_mark);
            iconWarnin  = itemView.findViewById(R.id.icon_warnin);
            labelImg    = itemView.findViewById(R.id.label_img);

        }// Fin constructor CardOnFileHolder
    }// Fin CardOnFileHolder

    @Override
    public CardOnFileAdapter.CardOnFileHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View cardOnFileView = LayoutInflater.from( parent.getContext()).inflate(R.layout.card_om_file_card_view, parent, false);
        CardOnFileHolder cardOnFileHolder = new CardOnFileHolder(cardOnFileView);
        return cardOnFileHolder;
    }// Fin if onCreateViewHolder

    @Override
    public void onBindViewHolder(CardOnFileAdapter.CardOnFileHolder cardHolder, int position) {

        String merchanName = cardOnFileValues.get(position).mrchName;
        cardHolder.labelImg.setText( merchanName.substring(0,1));
        cardHolder.merchaName.setText( merchanName );

        String lastTransactionDate = cardOnFileValues.get(position).lastMrchTranDt;

        try {
            Date date = parseDate.parse( lastTransactionDate );
            cardHolder.lastTransaction.setText( formatDate.format( date ) );
        } catch (ParseException e) {
            cardHolder.lastTransaction.setText( cardOnFileValues.get(position).lastMrchTranDt );
        }// Fin try/catch

        String iconCheck    = cardOnFileValues.get( position ).iconCheck;
        String iconWarnin   = cardOnFileValues.get( position ).iconWarnin;

        if( iconCheck.equals( "2" ) ){
            cardHolder.iconCheck.setVisibility( View.VISIBLE );
            cardHolder.iconWarnin.setVisibility( View.GONE );
        }else if( iconWarnin.equals( "2" ) ){
            cardHolder.iconWarnin.setVisibility( View.VISIBLE );
            cardHolder.iconCheck.setVisibility( View.GONE );
        }// Fin if/else

    }// Fin onBindViewHolder

    @Override
    public int getItemCount() {
        return cardOnFileValues.size();
    }

}// Fin CardOnFileAdapter
