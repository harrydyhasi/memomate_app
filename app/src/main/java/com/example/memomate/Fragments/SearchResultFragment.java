package com.example.memomate.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Adapters.SearchResultAdapter;
import com.example.memomate.Adapters.StudySetRecyclerViewAdapter;
import com.example.memomate.R;
import com.example.memomate.Utils.DataFireBase;

import java.util.ArrayList;

public class SearchResultFragment extends Fragment {
    private RecyclerView listResultRecyclerView;
    private ArrayAdapter<String> adapterResult;
    private TextView btnClose;
    private SearchView searchView;
    private SearchResultAdapter searchResultAdapter;
    private RecyclerView studySetRecyclerView;
    private StudySetRecyclerViewAdapter studySetRecyclerViewAdapter;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManager1;
    private ArrayList<String> filteredResults = new ArrayList<String>();
//    private String[] names = {"Alan", "James", "Guy", "Chet", "Joshua", "Brian", "Mark", "David", "Kirk", "Susan", "Edward", "Michael", "Emily", "John", "Steve"};
    private ArrayList<String> studySetNames;
    private DataFireBase dataFireBase = new DataFireBase();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        addEvents(view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataFireBase = new DataFireBase();
        searchResultAdapter = new SearchResultAdapter(filteredResults, listResultRecyclerView, studySetRecyclerView, layoutManager1);
        dataFireBase.setStudySetLvAdapter(searchResultAdapter);

        studySetNames = dataFireBase.getStudySetNames();

    }
    public void initView(View view){
        searchView = view.findViewById(R.id.search_view);
        searchView.requestFocus();

        btnClose = view.findViewById(R.id.btnCancel);

        listResultRecyclerView = view.findViewById(R.id.recyclerview_search_result);

        studySetRecyclerView = view.findViewById(R.id.recyclerview_searched_study_set);


        searchResultAdapter = new SearchResultAdapter(filteredResults, listResultRecyclerView, studySetRecyclerView, layoutManager1);

        listResultRecyclerView.setLayoutManager(layoutManager);
        studySetNames = dataFireBase.getStudySetNames();
        dataFireBase.setStudySetLvAdapter(searchResultAdapter);
        listResultRecyclerView.setAdapter(searchResultAdapter);
        studySetRecyclerView.setLayoutManager(layoutManager1);
        studySetRecyclerView.setAdapter(searchResultAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        return view;
    }
    public void addEvents(View view){
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(SearchResultFragment.this).commit();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filteredResults.clear();
                studySetNames = dataFireBase.getStudySetNames();
                dataFireBase.setStudySetLvAdapter(searchResultAdapter);

                if (newText != null && !newText.isEmpty()) {
                    for (String name : studySetNames) {
                        if (name.contains(newText)) {
                            filteredResults.add(name);
                        }
                    }
                }
                layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
                layoutManager1 = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

//                studySetNames = dataFireBase.getStudySetNames();
//                searchResultAdapter = new SearchResultAdapter(filteredResults);
                listResultRecyclerView.setLayoutManager(layoutManager);
                searchResultAdapter = new SearchResultAdapter(filteredResults, listResultRecyclerView, studySetRecyclerView, layoutManager1);
                listResultRecyclerView.setAdapter(searchResultAdapter);

                return false;
            }
        });

    }



}