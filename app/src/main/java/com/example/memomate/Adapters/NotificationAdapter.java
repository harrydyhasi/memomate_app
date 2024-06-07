package com.example.memomate.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Models.Notification;
import com.example.memomate.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.CustomHolder>{
    ArrayList<Notification> notifications;
    public NotificationAdapter(ArrayList<Notification> notifications){
        this.notifications = notifications;
    }
    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificaion, parent, false);
        return new CustomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        holder.icon.setImageResource(notifications.get(position).getImage());
        holder.content.setText(notifications.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class CustomHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView content;
        public CustomHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            content = itemView.findViewById(R.id.content);
        }
    }
}
