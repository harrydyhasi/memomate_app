package com.example.memomate.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.memomate.FBModel.StudySetModel;
import com.example.memomate.Models.Folder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FolderRepository {

    public DatabaseReference databaseReference;
    public FolderRepository(String uid)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(uid).child("list_folder");

    }
    public List<StudySetModel> getStudySetInFolder(String idForder)
    {
        List<StudySetModel> result = new ArrayList<>();
        databaseReference.child(idForder).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot A : snapshot.getChildren())
                {
                    Folder tmp = snapshot.getValue(Folder.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return result;
    }

    public void addStudySetToFolder(String idForder, String studySetID)
    {
//        databaseReference.child(idForder).child("studyset_list").child(studySetID).child("id").setValue(studySetID);
        databaseReference.child(idForder).child("studyset_list").child(studySetID).setValue(studySetID);

    }

    public List<String> getStudySet(String idForder)
    {
        List<String> result = new ArrayList<>();
        databaseReference.child(idForder).child("studyset_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String tmp = dataSnapshot.child("id").getValue(String.class);
                    Log.d("HAHA", tmp);
                    result.add(tmp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return result;
    }
}
