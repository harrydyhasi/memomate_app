package com.example.memomate.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Activities.MatchModeActivity;
import com.example.memomate.Adapters.MatchAdapter;
import com.example.memomate.Models.FlashCard;
import com.example.memomate.R;

import java.util.ArrayList;
import java.util.List;

public class BoardMatchModeFragment extends Fragment {

    private View mview;
    private List<String> mList;
    private MatchAdapter matchAdapter;
    private RecyclerView rcvMatch;
    private TextView txtTimer;

    private MatchModeActivity matchModeActivity;

    private int milliseconds = 0;
    Handler handler;


    public BoardMatchModeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mview =  inflater.inflate(R.layout.fragment_board_match_mode, container, false);
        matchModeActivity = (MatchModeActivity) getActivity();
        initView();
        return mview;
    }

    private void initView()
    {
        rcvMatch = mview.findViewById(R.id.rcvBoard);
        matchAdapter = new MatchAdapter(matchModeActivity.getListFromStudySetDetail(), new MatchAdapter.IMatchMode() {
            @Override
            public void increaseSecond() {
                milliseconds += 100;
            }

            @Override
            public void finished() {
                handler.removeCallbacks(runnable);
                matchModeActivity.goToFinishedMatchMode();
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(matchModeActivity, 3);
        rcvMatch.setLayoutManager(gridLayoutManager);
        rcvMatch.setAdapter(matchAdapter);
        txtTimer = matchModeActivity.findViewById(R.id.txtClock);
        handler = new Handler();
        startTimer();
    }

    private void startTimer()
    {
       handler.postDelayed(runnable,10);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            milliseconds += 1;
            updateTimer();
            handler.postDelayed(this,10);
        }
    };

    private void updateTimer()
    {
        int minutes = milliseconds / 6000;
        int seconds = (milliseconds % 6000) / 100;
        int millis = milliseconds % 100;

        String timerText = String.format("%02d:%02d:%02d", minutes, seconds, millis);
        txtTimer.setText(timerText);
    }


    private List<FlashCard> getListFlashCard()
    {
        List<FlashCard> flashCards = new ArrayList<>();
        flashCards.add(new FlashCard("given that", "với việc"));
        flashCards.add(new FlashCard("which is", "cái mà"));
        flashCards.add(new FlashCard("so that", "để"));
        flashCards.add(new FlashCard("as long as", "miễn là"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        return flashCards;
    }



}