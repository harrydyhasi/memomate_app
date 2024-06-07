package com.example.memomate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Models.FlashCard;
import com.example.memomate.R;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    List<FlashCard> mList;
    private Context context;



    public CardStackAdapter(List<FlashCard> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public CardStackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_stack_item,parent,false);
        return new CardStackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FlashCard flashCard = mList.get(position);
        holder.tF.setText(flashCard.getTerm());
        holder.tB.setText(flashCard.getDefinition());
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tF, tB;
        EasyFlipView easyFlipView;
        ImageView btnBackExpand, btnFrontExpand;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tF = itemView.findViewById(R.id.txtCardFront);
            tB = itemView.findViewById(R.id.txtCardBack);
            btnBackExpand = itemView.findViewById(R.id.btnBackExpand);
            btnFrontExpand = itemView.findViewById(R.id.btnFrontExpand);
            btnBackExpand.setVisibility(View.GONE);
            btnFrontExpand.setVisibility(View.GONE);
            easyFlipView = itemView.findViewById(R.id.flipCard);
            easyFlipView.setToHorizontalType();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    easyFlipView.flipTheView();
                }
            });
        }

    }
}
