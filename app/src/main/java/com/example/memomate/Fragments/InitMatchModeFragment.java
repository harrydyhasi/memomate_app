package com.example.memomate.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.memomate.Activities.MatchModeActivity;
import com.example.memomate.R;

public class InitMatchModeFragment extends Fragment {
    private View mView;
    private MatchModeActivity matchModeActivity;
    public InitMatchModeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_init_match_mode, container, false);
        matchModeActivity = (MatchModeActivity) getActivity();
        Button btnStart = mView.findViewById(R.id.btnStartGame);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchModeActivity.goToStart();
            }
        });
        return mView;
    }
}