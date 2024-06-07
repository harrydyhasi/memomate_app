package com.example.memomate.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.memomate.Activities.MemberLibraryActivity;
import com.example.memomate.Models.Member;
import com.example.memomate.Models.User;
import com.example.memomate.R;

import java.util.ArrayList;


public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder>
{
    Context context;
    ArrayList<User> listMember;

    public MemberAdapter(Context context) {
        this.context = context;
    }

    public MemberAdapter(ArrayList<User> listMember) {
        this.listMember = listMember;
    }

    public void setData(ArrayList<User> list)
    {
        this.listMember = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_members, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        User member = listMember.get(position);
        if (member == null)
        {
            return;
        }
        Glide.with(holder.cardView).load(member.getAvatar()).into(holder.avatar);
        holder.userName.setText(member.getUserName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(v.getContext(), MemberLibraryActivity.class);
//                i.putExtra("userName", member.getUserName());
//                i.putExtra("avatar", member.getAvatar());
////                i.putExtra("position", holder.getAdapterPosition());
//                ((Activity)v.getContext()).startActivityForResult(i, 12);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (listMember != null) {
            return listMember.size();
        }
        return 0;
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView userName;
        CardView cardView;
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            userName = itemView.findViewById(R.id.userName);
            cardView = itemView.findViewById(R.id.cardMember);
        }
    }
}