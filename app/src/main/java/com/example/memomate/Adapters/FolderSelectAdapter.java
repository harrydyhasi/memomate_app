package com.example.memomate.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.memomate.Models.Folder;
import com.example.memomate.Models.User;
import com.example.memomate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class FolderSelectAdapter extends RecyclerView.Adapter<FolderSelectAdapter.CustomHolder> {
    Context context;
    private ArrayList<Folder> folders;
    private ArrayList<Folder> selectedFolders = new ArrayList<>();
    //Folder folder;
    public FolderSelectAdapter(ArrayList<Folder> folders){
        this.folders = folders;
    }
    @NonNull
    @Override
    public FolderSelectAdapter.CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder_tab_folder, parent, false);
        return new FolderSelectAdapter.CustomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderSelectAdapter.CustomHolder holder, int position) {
        Folder folder = folders.get(position);
        if (folder == null)
        {
            return;
        }
        //holder.avatar.setImageResource(studySet.getAvatar());
//        Glide.with(context).load(folder.getAvatar()).into(holder.avatar);
//        holder.txtUserName.setText(folder.getAuthor());
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
                    holder.txtUserName.setText(user.getUserName());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText((Activity)context, "ERROR TO GET PROFILE", Toast.LENGTH_SHORT).show();
            }
        });
        holder.txtTitle.setText(folder.getTitle());
        holder.txtTerm.setText(String.valueOf(folder.getQuantity()) + " terms");
        holder.cardFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!selectedFolders.contains(folder))
                {
                    selectedFolders.add(folder);
                    holder.cardFolder.setStrokeWidth(3);
                    holder.cardFolder.setStrokeColor(ContextCompat.getColor(context, R.color.blue));
                }
                else {
                    holder.cardFolder.setStrokeWidth(0);
                    selectedFolders.remove(folder);
                }
            }
        });

    }
    public ArrayList<Folder> getSelectedFolders(){
        return selectedFolders;
    }

    @Override
    public int getItemCount() {
        if (folders != null) {
            return folders.size();
        }
        return 0;
    }
    class CustomHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView txtUserName, txtTitle, txtTerm;
        MaterialCardView cardFolder;
        public CustomHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            txtUserName = itemView.findViewById(R.id.userName);
            txtTerm = itemView.findViewById(R.id.term);
            txtTitle = itemView.findViewById(R.id.txtTitleFolder);
            cardFolder = itemView.findViewById(R.id.cardFolder);
        }

    }
    public void setProfilePic(Context context, Uri imageUri, ImageView avatar)
    {
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(avatar);
    }
}