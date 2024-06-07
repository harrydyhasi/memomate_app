package com.example.memomate.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.memomate.Activities.MatchModeActivity;
import com.example.memomate.R;

public class FinishedMatchModeFragment extends Fragment {
    private View mview;
    Button btnPlayAgain;
    TextView txtResult;
    private MatchModeActivity matchModeActivity;
    public FinishedMatchModeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mview =  inflater.inflate(R.layout.fragment_finished_match_mode, container, false);
        matchModeActivity = (MatchModeActivity) getActivity();
        initView();
        return mview;
    }

    private void initView()
    {
        txtResult = mview.findViewById(R.id.txtResult);
        TextView txtTimer = matchModeActivity.findViewById(R.id.txtClock);
        txtResult.setText(txtTimer.getText() + " seconds");
        txtTimer.setText("Match");;
        btnPlayAgain = mview.findViewById(R.id.btnPlayAgain);
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchModeActivity.goToStart();
            }
        });
    }
}