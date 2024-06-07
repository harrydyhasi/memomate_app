package com.example.memomate.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Activities.CreateClassActivity;
import com.example.memomate.Adapters.ClassRecyclerViewAdapter;
import com.example.memomate.Models.Class;
import com.example.memomate.R;
import com.example.memomate.Utils.DataFireBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class ClassFragment extends Fragment {
    private Context context;
    private RecyclerView classRecyclerView;
    private ClassRecyclerViewAdapter classRcvAdapter;
    private LinearLayoutManager layoutManager;
    private ImageButton btnCreate;
    private ArrayList<Class> classes;
    private DataFireBase dataFireBase = new DataFireBase();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = firebaseUser.getUid();

    public ClassFragment(Context context) {
        this.context = context;
    }
    public ClassFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataFireBase = new DataFireBase();

//        classes = dataFireBase.getAllClassesByUserName(username);
        classes = dataFireBase.getClassesByUid(uid);
        classRcvAdapter = new ClassRecyclerViewAdapter(classes);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

    }
    public void initView(View view){
        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

        classRecyclerView = view.findViewById(R.id.recyclerview_class);

        classRcvAdapter = new ClassRecyclerViewAdapter(classes);
        dataFireBase.setClassRcvAdapter(classRcvAdapter);
//        classes = dataFireBase.getAllClassesByUserName(username);
        classes = dataFireBase.getClassesByUid(uid);
        classRecyclerView.setLayoutManager(layoutManager);
        classRecyclerView.setAdapter(classRcvAdapter);
        btnCreate = view.findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view.getContext(), CreateClassActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class, container, false);
    }
}