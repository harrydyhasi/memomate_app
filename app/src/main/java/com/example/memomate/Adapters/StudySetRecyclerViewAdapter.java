package com.example.memomate.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.memomate.Activities.StudySetDetailActivity;
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
import java.util.List;

public class StudySetRecyclerViewAdapter extends RecyclerView.Adapter<StudySetRecyclerViewAdapter.CustomHolder> {
    Context context;
    private ArrayList<StudySet> studySets;

    public StudySetRecyclerViewAdapter(ArrayList<StudySet> studySets) {
        this.studySets = studySets;
    }


    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_study_set_tab_study_set, parent, false);
        return new CustomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        StudySet studySet = studySets.get(position);
        if (studySet == null)
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
                    holder.txtUserName.setText(user.getUserName());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText((Activity)context, "ERROR TO GET PROFILE", Toast.LENGTH_SHORT).show();
            }
        });
        //holder.avatar.setImageResource(studySet.getAvatar());
        //holder.txtUserName.setText(studySet.getUserName());
        holder.txtTitle.setText(studySet.getName());
//        holder.txtTerm.setText(String.valueOf(studySet.getQuantity()) + " terms");
//        holder.txtTerm.setText(String.valueOf(studySet.getQuantity()) + " terms");
        holder.cardStudySet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, StudySetDetailActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("S", studySet);
                i.putExtras(b);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (studySets != null) {
            return studySets.size();
        }
        return 0;
    }
    public void searchStudySetList(ArrayList<StudySet> searchList){
        studySets = searchList;
        notifyDataSetChanged();
    }

    class CustomHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView txtUserName, txtTitle;
        CardView cardStudySet;
        public CustomHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            //txtTerm = itemView.findViewById(R.id.txtTerm);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            cardStudySet = itemView.findViewById(R.id.cardStudySet);
        }

    }

    public void setProfilePic(Context context, Uri imageUri, ImageView avatar)
    {
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(avatar);
    }
}
