package com.example.memomate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Models.Folder;
import com.example.memomate.R;

import java.util.ArrayList;

public class FolderPagerAdapter extends RecyclerView.Adapter<FolderPagerAdapter.FolderViewHolder> {
    Context context;
    ArrayList<Folder> folders;

    public FolderPagerAdapter(ArrayList<Folder> folders) {
        this.folders = folders;
    }

    @NonNull
    @Override
    public FolderPagerAdapter.FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_studyset, parent, false);
        return new FolderPagerAdapter.FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class FolderViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView txtUserName, txtTitle, txtTerm;
        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtTerm = itemView.findViewById(R.id.txtTerm);
            txtTitle = itemView.findViewById(R.id.txtTitle);

        }
    }
}
