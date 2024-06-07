package com.example.memomate.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ChoiceViewHolder> {
    private List<String> mList;
    IOnClickListener mIOnClickListener;

    public interface IOnClickListener{
        void onClick(String cont);
    };

    public ChoiceAdapter(List<String> mList, IOnClickListener listener) {
        this.mList = mList;
        this.mIOnClickListener = listener;
    }

    @NonNull
    @Override
    public ChoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new ChoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceViewHolder holder, int position) {
        String cont = mList.get(position);
        holder.btnChoice.setText(cont);
        holder.btnChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIOnClickListener.onClick(cont);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    public class ChoiceViewHolder extends RecyclerView.ViewHolder {

        MaterialButton btnChoice;

        public ChoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            btnChoice = itemView.findViewById(R.id.btnChoice);
        }
    }
}
