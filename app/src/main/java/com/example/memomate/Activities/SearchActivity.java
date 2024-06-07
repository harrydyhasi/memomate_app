package com.example.memomate.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.memomate.Adapters.SearchResultAdapter;
import com.example.memomate.Adapters.StudySetRecyclerViewAdapter;
import com.example.memomate.FBModel.StudySetModel;
import com.example.memomate.Models.StudySet;
import com.example.memomate.R;
import com.example.memomate.Utils.DataFireBase;
import com.example.memomate.viewmodel.FlashCardViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView listResultRcv;
    private ArrayAdapter<String> adapterResult;
    private TextView btnCancel;
    private SearchView searchView;
    private SearchResultAdapter studySetNameAdapter;
    private RecyclerView studySetRcv;
    private StudySetRecyclerViewAdapter studySetRecyclerViewAdapter;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManager1;
    private ArrayList<String> filteredResults = new ArrayList<String>();
    private ArrayList<StudySetModel> studySets = new ArrayList<>();
    private ArrayList<String> studySetNames;
    private FlashCardViewModel flashCardViewModel;

    private DataFireBase dataFireBase = new DataFireBase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();
        addEvents();
    }
    public void initView(){
        searchView = findViewById(R.id.search_view);
        searchView.requestFocus();

        btnCancel = findViewById(R.id.btnCancel);

        studySetRcv = findViewById(R.id.rcv_searched_study_set);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        studySets = new ArrayList<>();
        getListStudySetFromFireBase();
        studySetRecyclerViewAdapter = new StudySetRecyclerViewAdapter(convertList(studySets));
//        studySetRecyclerViewAdapter = new StudySetRecyclerViewAdapter(studySets);
        studySetRcv.setLayoutManager(layoutManager);
        studySetRcv.setAdapter(studySetRecyclerViewAdapter);


    }
    private ArrayList<StudySet> convertList(List<StudySetModel> studySetModels)
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<StudySet> lst = new ArrayList<>();
        for (int i=0; i<studySetModels.size();i++)
        {
            flashCardViewModel = new FlashCardViewModel(firebaseUser.getUid(),studySetModels.get(i).getId());
            StudySet A = new StudySet(studySetModels.get(i));
            A.setFlashCardList(flashCardViewModel.getFlashCardList());
            lst.add(A);
        }

        return lst;
    }
    public void addEvents(){
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });

    }
    private ArrayList<String> getStudySet(String text){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("User");
        ArrayList<String> studySetNames = new ArrayList<>();

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataSnapshot studySetSnapShot = dataSnapshot.child("studyset_list");
                    for (DataSnapshot studySet : studySetSnapShot.getChildren()){
                        String studySetName = studySet.child("name").getValue(String.class);
                        if (text != null && !text.isEmpty()) {
                            for (String name : studySetNames) {
                                if (name.toLowerCase().contains(text.toLowerCase())) {
                                    filteredResults.add(name);
                                }
                            }
                        }
                        studySetNames.add(studySetName);
                    }
                }
                Log.d("SO LUONG", String.valueOf(studySetNames.size()));
                studySetNameAdapter.notifyDataSetChanged();
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

        return studySetNames;
    }
    private void search(String text){
        ArrayList<StudySet> searchList  = new ArrayList<>();
        for (StudySet studySet : convertList(studySets)){
            if(studySet.getName().toLowerCase().contains(text.toLowerCase())){
                searchList.add(studySet);
            }
        }
        studySetRecyclerViewAdapter.searchStudySetList(searchList);
    }
    public void getListStudySetFromFireBase()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for (DataSnapshot userSnapShot : snapshot.getChildren()) {
                    if (userSnapShot.getKey().equals("studyset_list")) {
                        for (DataSnapshot setSnapShot : userSnapShot.getChildren()) {
                            StudySetModel studySet = setSnapShot.getValue(StudySetModel.class);
                            if (studySet != null) {
                                studySets.add(studySet);
                                studySetRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        }
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

    }
}