package com.example.memomate.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.memomate.Activities.FolderActivity;
import com.example.memomate.Models.Folder;
import com.example.memomate.Models.StudySet;
import com.example.memomate.Models.User;
import com.example.memomate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class FolderRecyclerViewAdapter extends RecyclerView.Adapter<FolderRecyclerViewAdapter.FolderViewHolder> {
    Context context;
    ArrayList<Folder> listFolder;
    StudySet studySet;

    public FolderRecyclerViewAdapter(Context context)
    {
        this.context = context;
    }

    public FolderRecyclerViewAdapter(ArrayList<Folder> folders) {
        this.listFolder = folders;
    }

    public void setData(ArrayList<Folder> list) {
        this.listFolder = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public FolderRecyclerViewAdapter.FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder_tab_folder, parent, false);
        return new FolderRecyclerViewAdapter.FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        Folder folder = listFolder.get(position);
        if (folder == null)
        {
            return;
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage.getInstance().getReference().child("profile_pic").child(firebaseUser.getUid()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful())
                {
                    Uri uri = task.getResult();
                    setProfilePic(((Activity)context), uri, holder.avatar);
                }
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    holder.userName.setText(user.getUserName());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText((Activity)context, "ERROR TO GET PROFILE", Toast.LENGTH_SHORT).show();
            }
        });
        //Glide.with(holder.cardFolder).load(user.getAvatar()).into(holder.avatar);
        //holder.userName.setText(folder.getAuthor());

        holder.term.setText(String.valueOf(folder.getQuantity()) + " sets");
        holder.txtTitleFolder.setText(folder.getTitle());

        holder.cardFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, FolderActivity.class);
                i.putExtra("id_title_adapter", folder.getId());
                ((Activity)context).startActivityForResult(i, 12);
                //context.startActivity(i);
            }
        });

//        if (folder.getId()!= null)
//        {
//            databaseReference.child(firebaseUser.getUid()).child("list_folder").child(folder.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Folder folder = snapshot.getValue(Folder.class);
//                    if (folder != null) {
//                        holder.txtTitleFolder.setText(folder.getTitle());
//                        Log.d("title folder", folder.getTitle());
//
//                    }
//                    else Toast.makeText((Activity)context, "Error to load title", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Toast.makeText((Activity)context, "Error to load title", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//        else Toast.makeText((Activity)context, "error to get title", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        if (listFolder!=null)
        {
            return listFolder.size();
        }
        return 0;
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView txtTitleFolder, term, userName;
        CardView cardFolder;
        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            txtTitleFolder = itemView.findViewById(R.id.txtTitleFolder);
            term = itemView.findViewById(R.id.term);
            userName = itemView.findViewById(R.id.userName);
            cardFolder = itemView.findViewById(R.id.cardFolder);
        }
    }

    public void setProfilePic(Context context, Uri imageUri, ImageView avatar)
    {
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(avatar);
    }
}
