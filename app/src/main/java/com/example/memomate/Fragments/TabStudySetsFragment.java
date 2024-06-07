package com.example.memomate.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Activities.DashboardActivity;
import com.example.memomate.Adapters.StudySetAdapter;
import com.example.memomate.Adapters.StudySetRecyclerViewAdapter;
import com.example.memomate.FBModel.StudySetModel;
import com.example.memomate.Models.FlashCard;
import com.example.memomate.Models.StudySet;
import com.example.memomate.R;
import com.example.memomate.repository.FlashCardRepository;
import com.example.memomate.repository.StudySetRepository;
import com.example.memomate.viewmodel.FlashCardViewModel;
import com.example.memomate.viewmodel.StudySetViewModel;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TabStudySetsFragment extends Fragment {
    private View mView;
    private RecyclerView studySetRecyclerView;

    private LinearLayoutManager layoutManager;

    private StudySetRecyclerViewAdapter studySetAdapter;

    EditText edtFilter;
    Button btnAdd;
    DashboardActivity mDashActivity;

    private StudySetViewModel studySetViewModel;
    private FlashCardViewModel flashCardViewModel;
    public TabStudySetsFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDashActivity = (DashboardActivity) getActivity();
        mView =  inflater.inflate(R.layout.fragment_tab_study_sets, container, false);

        layoutManager = new LinearLayoutManager(mView.getContext(), LinearLayoutManager.VERTICAL, false);
        studySetRecyclerView = mView.findViewById(R.id.recyclerview_study_set);
        studySetRecyclerView.setLayoutManager(layoutManager);
        getListFromFB();
        return mView;
    }

    private void getListFromFB()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

//        studySetViewModel = new ViewModelProvider(this).get(StudySetViewModel.class);
        studySetViewModel = new StudySetViewModel(firebaseUser.getUid());
        studySetViewModel.getListStudySetLiveData().observe(this.getViewLifecycleOwner(), studySetModels -> {
            studySetAdapter = new StudySetRecyclerViewAdapter(convertList(studySetModels));
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
}