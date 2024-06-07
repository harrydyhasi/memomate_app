package com.example.memomate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Models.FlashCard;
import com.example.memomate.R;

import java.util.List;

public class FlashCardAdapter extends RecyclerView.Adapter<FlashCardAdapter.FlashCardViewHolder> {
    List<FlashCard> mList;
    private Context context;

    public FlashCardAdapter(List<FlashCard> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public FlashCardAdapter.FlashCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flash_card_item,parent,false);
        return new FlashCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashCardAdapter.FlashCardViewHolder holder, int position) {
        FlashCard flashCard = mList.get(position);
        holder.txtTerm.setText(flashCard.getTerm());
        holder.txtDef.setText(flashCard.getDefinition());
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public class FlashCardViewHolder extends RecyclerView.ViewHolder{
        TextView txtTerm, txtDef;
        public FlashCardViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTerm = itemView.findViewById(R.id.txtTerm);
            txtDef = itemView.findViewById(R.id.txtDefinition);
        }
    }

    public void addCard() {
        mList.add(new FlashCard());
        notifyItemInserted(mList.size());
    }
}
