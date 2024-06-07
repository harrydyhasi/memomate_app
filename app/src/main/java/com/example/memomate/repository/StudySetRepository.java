package com.example.memomate.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.memomate.FBModel.StudySetModel;
import com.example.memomate.Models.Folder;
import com.example.memomate.Models.StudySet;
import com.example.memomate.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudySetRepository {
    public DatabaseReference databaseReference;

    public StudySetRepository(String uid)
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(uid).child("studyset_list");

    }

    public void addStudySet(StudySetModel studySet)
    {
        String studySetId = databaseReference.push().getKey();
        studySet.setId(studySetId);
        databaseReference.child(studySetId).setValue(studySet);
    }


    public LiveData<List<StudySetModel>> getTest()
    {
        MutableLiveData<List<StudySetModel>> listMutableLiveData = new MutableLiveData<>();
        List<StudySetModel> studySetModels = new ArrayList<>();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                StudySetModel studySetModel = snapshot.getValue(StudySetModel.class);
                if (studySetModel != null)
                {
                    studySetModels.add(studySetModel);
                    listMutableLiveData.setValue(studySetModels);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                StudySetModel studySetModel = snapshot.getValue(StudySetModel.class);
                if (studySetModel == null || studySetModels == null || studySetModels.isEmpty()) return;
                for (int i=0; i<studySetModels.size();i++)
                {
                    if (Objects.equals(studySetModel.getId(), studySetModels.get(i).getId()))
                        studySetModels.set(i, studySetModel);
                }
                listMutableLiveData.setValue(studySetModels);
                Log.d("CHANGED", "done");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                StudySetModel studySetModel = snapshot.getValue(StudySetModel.class);

                if (studySetModel == null || studySetModels == null || studySetModels.isEmpty()) return;
                for (int i=0; i<studySetModels.size();i++)
                {
                    if (studySetModel.getId() == studySetModels.get(i).getId()) {
                        studySetModels.remove(studySetModels.get(i));
                        break;
                    }
                }
                listMutableLiveData.setValue(studySetModels);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listMutableLiveData;
    }



    public LiveData<List<StudySetModel>> getAllUser()
    {
        MutableLiveData<List<StudySetModel>> listMutableLiveData = new MutableLiveData<>();
        List<StudySetModel> studySetModels = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                    if (userSnapShot.getKey().equals("studyset_list")) {
                        for (DataSnapshot setSnapShot : userSnapShot.getChildren()) {
//
                            StudySetModel studySet = setSnapShot.getValue(StudySetModel.class);
                            if (studySet != null) {
                                studySetModels.add(studySet);
//                                studySetRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                listMutableLiveData.setValue(studySetModels);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listMutableLiveData;
    }

    public void update(StudySetModel A)
    {
        databaseReference.child(A.getId()).updateChildren(A.toMap());
    }

    public void delete(String id)
    {
        databaseReference.child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("XOA", databaseReference.child(id).toString());
            }
        });
    }

    public List<StudySetModel> getStudySetFromFolder(List<String> studyIds)
    {
        List<StudySetModel> result = new ArrayList<>();
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    for (String id : studyIds) {
                        if (id.equals(snapshot.getKey())){
                            StudySetModel A = snapshot.getValue(StudySetModel.class);
                            result.add(A);
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        return result;
    }



}
