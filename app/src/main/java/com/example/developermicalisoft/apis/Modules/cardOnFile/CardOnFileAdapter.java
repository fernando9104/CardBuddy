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

import java.util.ArrayList;
import java.util.List;

public class CardOnFileAdapter extends Adapter<CardOnFileAdapter.CardOnFileHolder> {

    private List<CardOnFileModel> cardOnFileValues = new ArrayList<>();
    public CardOnFileAdapter(List<CardOnFileModel> cardOnFileValues) {
        this.cardOnFileValues = cardOnFileValues;
    }

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

        View cardOnFileView = LayoutInflater.from( parent.getContext()).inflate(R.layout.card_on_file_card_view, parent, false);
        CardOnFileHolder cardOnFileHolder = new CardOnFileHolder(cardOnFileView);
        return cardOnFileHolder;
    }// Fin if onCreateViewHolder

    @Override
    public void onBindViewHolder(CardOnFileAdapter.CardOnFileHolder cardHolder, int position) {

        String merchanName = cardOnFileValues.get(position).mrchName;
        cardHolder.labelImg.setText( merchanName.substring(0,1));
        cardHolder.merchaName.setText( merchanName );
        cardHolder.lastTransaction.setText( cardOnFileValues.get(position).lastMrchTranDt );

        String updateStatus     = cardOnFileValues.get( position ).vAUUpdateStatus;
        String acctNumOld4Digit = cardOnFileValues.get( position ).acctNumOld4Digit;
        String mCC              = cardOnFileValues.get( position ).mCC;

        if( acctNumOld4Digit.equals( mCC ) && updateStatus.equals("Y") ){
            cardHolder.iconCheck.setVisibility( View.VISIBLE );
            cardHolder.iconWarnin.setVisibility( View.GONE );
        }else{
            cardHolder.iconWarnin.setVisibility( View.VISIBLE );
            cardHolder.iconCheck.setVisibility( View.GONE );
        }

    }// Fin onBindViewHolder

    @Override
    public int getItemCount() {
        return cardOnFileValues.size();
    }

}// Fin CardOnFileAdapter
