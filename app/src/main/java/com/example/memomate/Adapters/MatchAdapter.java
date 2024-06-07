package com.example.memomate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Models.FlashCard;
import com.example.memomate.Models.MatchMode;
import com.example.memomate.R;
import com.example.memomate.Utils.Utils;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {

    private List<String> mList;
    private List<FlashCard> flashCards;
    private Context context;
    private MatchViewHolder lastHolder;

    IMatchMode mIMatchMode;

    public interface IMatchMode{
        void increaseSecond();
        void finished();
    }

    public MatchAdapter(List<FlashCard> flashCards, IMatchMode listener) {
        this.flashCards = flashCards;
        matchMode = new MatchMode(flashCards);
        this.mIMatchMode = listener;
        mList = matchMode.init();
    }

    int clicked = 0;
    int lastClicked = -1;

    int correct = 0;
    MatchMode matchMode;

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        String content = mList.get(position);
        holder.txtContent.setText(content);

        getItemViewType(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clicked == 0)
                {
                    lastClicked = holder.getAdapterPosition();
                    lastHolder = holder;
                    setState(holder,1);
                }
                clicked++;
                if (clicked == 2)
                {
                    clicked = 0;
                    if (position == lastClicked)
                    {
                        setState(holder,0);
                    }
                    else {
                        String s1=  mList.get(holder.getAdapterPosition());
                        String s2 = mList.get(lastClicked);

                        if (matchMode.isMatch(s1,s2)){
                            correct++;
                            holder.itemView.setVisibility(View.GONE);
                            lastHolder.itemView.setVisibility(View.GONE);
                        }
                        else {
                            setState(holder,3);
                            setState(lastHolder,3);

                            Utils.shake(holder.itemView);
                            Utils.shake(lastHolder.itemView);

                            Utils.delay(200, new Utils.DelayCallBack() {
                                @Override
                                public void afterDelay() {
                                    setState(holder,0);
                                    setState(lastHolder,0);
                                }
                            });
                            mIMatchMode.increaseSecond();

                        }
                    }

                }

                if (correct == matchMode.getSize()){
                    mIMatchMode.finished();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public void setState(MatchViewHolder holder, int state)
    {
        if (state == 0)
        {
            holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.txtContent.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        else if (state == 1)
        {
            holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.gray));
            holder.txtContent.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        else {
            holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red));
            holder.txtContent.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
    }

    public class MatchViewHolder extends RecyclerView.ViewHolder{
        TextView txtContent;
        CardView card;
        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtContent);
            card = itemView.findViewById(R.id.cardMatch);
        }
    }
}
