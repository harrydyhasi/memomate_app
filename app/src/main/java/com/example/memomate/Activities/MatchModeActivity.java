package com.example.memomate.Activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.memomate.Fragments.BoardMatchModeFragment;
import com.example.memomate.Fragments.FinishedMatchModeFragment;
import com.example.memomate.Fragments.InitMatchModeFragment;
import com.example.memomate.Models.FlashCard;
import com.example.memomate.R;

import java.util.List;

public class MatchModeActivity extends AppCompatActivity {

    ImageView btnClose;
    TextView txtTimer;
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_mode);


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_frame, new InitMatchModeFragment());
        fragmentTransaction.commit();

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
                finish();

            }
        });

        txtTimer = findViewById(R.id.txtClock);

        player = MediaPlayer.create(this, R.raw.start_menu);
        player.setLooping(true);
        player.start();
    }

    public void goToStart()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        BoardMatchModeFragment boardMatchModeFragment = new BoardMatchModeFragment();
        fragmentTransaction.replace(R.id.content_frame, boardMatchModeFragment);
        fragmentTransaction.commit();
    }

    public void goToFinishedMatchMode()
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FinishedMatchModeFragment finishedMatchModeFragment = new FinishedMatchModeFragment();
        fragmentTransaction.replace(R.id.content_frame, finishedMatchModeFragment);
        fragmentTransaction.commit();
    }

    public List<FlashCard> getListFromStudySetDetail()
    {
        Bundle b = getIntent().getExtras();
        return (List<FlashCard>) b.getSerializable("FlashCardList");
    }


}