package com.example.memomate.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.memomate.Adapters.StudySetSelectAdapter;
import com.example.memomate.FBModel.StudySetModel;
import com.example.memomate.Models.StudySet;
import com.example.memomate.R;
import com.example.memomate.Utils.DataFireBase;
import com.example.memomate.viewmodel.FlashCardViewModel;
import com.example.memomate.viewmodel.FolderViewModel;
import com.example.memomate.viewmodel.StudySetViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class AddSetActivity extends AppCompatActivity {
    private RecyclerView studySetRecyclerView;
    private StudySetSelectAdapter studySetAdapter;
    private LinearLayoutManager layoutManager;
    private ImageButton btnBack;
    private TextView btnDone, btnCreateANewSet;
    ArrayList<StudySet> studySets = new ArrayList<>();
    private DataFireBase dataFireBase = new DataFireBase();
    private String username = new String();
    private StudySetViewModel studySetViewModel;
    private FlashCardViewModel flashCardViewModel;
    String id;
    FolderViewModel folderViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_set);
        studySetRecyclerView = findViewById(R.id.recyclerview_study_set);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        folderViewModel = new FolderViewModel(firebaseUser.getUid());
        getFoderId();
        initView();
        addEvent();
        getListFromFB();

    }
    private void initView(){
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        studySetRecyclerView.setLayoutManager(layoutManager);
        studySetRecyclerView.setAdapter(studySetAdapter);
    }
    private String getUserId() {
        Intent intent = getIntent();
        if (intent!= null) {
            return intent.getStringExtra("userName");
        }
        return null;
    }
    public void addEvent(){
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> studySetModelsId = new ArrayList<>();
                for (int i=0; i<studySetAdapter.getSelectedStudySets().size();i++)
                {
                    studySetModelsId.add(studySetAdapter.getSelectedStudySets().get(i).getId());
                }
                folderViewModel.addListStudySet(id,studySetModelsId);

//                Intent i = new Intent(AddSetActivity.this, FolderActivity.class);
//                i.putExtra("getStudySets", studySetAdapter.getSelectedStudySets());
//
//                ((Activity)AddSetActivity.this).startActivityForResult(i, 50);
                finish();

            }
        });
        btnCreateANewSet = findViewById(R.id.btnCreateANewSet);
        btnCreateANewSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddSetActivity.this, CreateSetActivity.class);
                //i.putExtra("getSelectedStudySets", studySetAdapter.getSelectedStudySets());
//                i.putExtra("getAvatar", folder.getAvatar());
//                i.putExtra("getUserName", folder.getAuthor());
//                i.putExtra("position", holder.getAdapterPosition());
                ((Activity)AddSetActivity.this).startActivityForResult(i, 12);
            }
        });
    }

    private void getListFromFB()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        studySetViewModel = new StudySetViewModel(firebaseUser.getUid());
        studySetViewModel.getListStudySetLiveData().observe(this, studySetModels -> {
            studySetAdapter = new StudySetSelectAdapter(convertList(studySetModels));
            studySetRecyclerView.setAdapter(studySetAdapter);
        });
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

    private void getFoderId()
    {
        Intent A = getIntent();
        id = A.getStringExtra("FolderID");
    }
}