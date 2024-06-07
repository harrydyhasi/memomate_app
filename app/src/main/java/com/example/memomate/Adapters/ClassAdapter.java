package com.example.memomate.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Activities.ClassDetailActivity;
import com.example.memomate.Models.Class;
import com.example.memomate.R;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {
    Context context;
    ArrayList<Class> classes;

    public ClassAdapter(ArrayList<Class> classes) {
        this.classes = classes;
    }

    @NonNull
    @Override
    public ClassAdapter.ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ClassAdapter.ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        Class aClass = classes.get(position);
        if (aClass == null)
        {
            return;
        }
        holder.title.setText(aClass.getTitle());
        holder.term.setText(String.valueOf(aClass.getStudySetList().size()) + " terms");
        holder.member.setText(String.valueOf(aClass.getMemberList().size()) + " members");

        holder.cardClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ClassDetailActivity.class);
//                i.putExtra("getTitle", aClass.getTitle());
//                i.putExtra("getSet_quantity", aClass.getSet_quantity());
//                i.putExtra("getMember_quantity", aClass.getMember_quantity());
//                i.putExtra("position", holder.getAdapterPosition());
                Bundle b = new Bundle();
                b.putSerializable("Class",aClass);
                i.putExtras(b);
                ((Activity)context).startActivityForResult(i, 12);
                //context.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        if (classes!=null)
        {
            return classes.size();
        }
        return 0;
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView title, term, member;
        CardView cardClass;
        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitleClass);
            term = itemView.findViewById(R.id.term_quantity);
            member = itemView.findViewById(R.id.member_quantity);
            cardClass = itemView.findViewById(R.id.cardClass);
        }
    }
}
