package com.example.memomate.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Models.FlashCard;
import com.example.memomate.Models.StudySet;
import com.example.memomate.R;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.CustomHolder> {
    private ArrayList<String> searchResults, filteredResults = new ArrayList<>();
    private RecyclerView recyclerViewSearchResult, recyclerViewStudySet;
    private StudySetRecyclerViewAdapter studySetRecyclerViewAdapter;
    private SearchResultAdapter searchResultAdapter;
    private LinearLayoutManager layoutManager;



    private String[] names = {"Alan", "James", "Guy", "Chet", "Joshua", "Brian", "Mark", "David", "Kirk", "Susan", "Edward", "Michael", "Emily", "John", "Steve"};

    public SearchResultAdapter(ArrayList<String> searchResults, RecyclerView recyclerViewSearchResult, RecyclerView recyclerViewStudySet, LinearLayoutManager layoutManager) {
        this.searchResults = searchResults;
        this.recyclerViewSearchResult = recyclerViewSearchResult;
        this.recyclerViewStudySet = recyclerViewStudySet;
        this.layoutManager = layoutManager;
    }

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        parent.findViewById(R.id.recyclerview_search_result);
        parent.findViewById(R.id.recyclerview_searched_study_set);
        return new CustomHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        holder.name.setText(searchResults.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewSearchResult.setVisibility(View.GONE);
                recyclerViewStudySet.setVisibility(View.VISIBLE);
                filteredResults.clear();

                    for (String name : names) {
                        if (name.contains(holder.name.getText())) {
                            filteredResults.add(name);
                        }
                    }
                Log.d("FilteredResultsSize", String.valueOf(filteredResults.size()) + holder.name.getText());
                ArrayList<StudySet> studySets = new ArrayList<>();
                for (int i = 0; i < filteredResults.size(); i++) {
                    StudySet studySet = new StudySet(filteredResults.get(i), "https://firebasestorage.googleapis.com/v0/b/memomatedb.appspot.com/o/1.jpg?alt=media&token=c2ae010d-432f-49e6-bb4a-cdf9a5a608a9", "username" + String.valueOf(i), new ArrayList<FlashCard>());
                    studySets.add(studySet);
                }
                recyclerViewStudySet.setLayoutManager(layoutManager);
                studySetRecyclerViewAdapter = new StudySetRecyclerViewAdapter(studySets);
                recyclerViewStudySet.setAdapter(studySetRecyclerViewAdapter);

            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    class CustomHolder extends RecyclerView.ViewHolder{
        TextView name;
        public CustomHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.study_set_name);
        }
    }
}
