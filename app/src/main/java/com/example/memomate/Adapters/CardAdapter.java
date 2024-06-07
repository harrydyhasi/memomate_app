package com.example.memomate.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Models.FlashCard;
import com.example.memomate.R;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>{
    Context context;
    ArrayList<FlashCard> listCard;
    boolean isOnTextChanged1 = false;
    boolean isOnTextChanged2 = false;

    public CardAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<FlashCard> list) {
        this.listCard = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_set, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        FlashCard card = listCard.get(position);
        if (card == null) {
            return;
        }
        EditText edtTerm = holder.edtTerm;
        EditText edtDef = holder.edtDifinition;

        edtTerm.setText(card.getTerm());
        edtDef.setText(card.getDefinition());

        if (position == listCard.size() - 1) {
            holder.edtTerm.requestFocus();
        }

        edtTerm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isOnTextChanged1 = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isOnTextChanged1)
                {
                    isOnTextChanged1 = false;
                    String term = edtTerm.getText().toString();
                    String def = edtDef.getText().toString();
                    FlashCard A = new FlashCard(term, def);
                    listCard.set(holder.getAdapterPosition(), A);
                }
            }
        });
        edtDef.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isOnTextChanged2 = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isOnTextChanged2)
                {
                    isOnTextChanged2 = false;
                    String term = edtTerm.getText().toString();
                    String def = edtDef.getText().toString();
                    FlashCard A = new FlashCard(term, def);
                    listCard.set(holder.getAdapterPosition(), A);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listCard != null) {
            return listCard.size();
        }
        return 0;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        EditText edtTerm, edtDifinition;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            edtTerm = itemView.findViewById(R.id.edtTerm);
            edtDifinition = itemView.findViewById(R.id.edtDefinition);

        }
    }
    public void addCard() {
        listCard.add(new FlashCard());
        notifyItemInserted(listCard.size());
    }

}

