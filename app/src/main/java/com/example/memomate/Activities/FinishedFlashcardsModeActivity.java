package com.example.memomate.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.memomate.Models.FlashcardsMode;
import com.example.memomate.R;

public class FinishedFlashcardsModeActivity extends AppCompatActivity {

    Button btnClose;
    ProgressBar progressBar;
    TextView txtKnow, txtDontKnow, txtProgress, txtPer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finised_flashcards_mode);

        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressBar = findViewById(R.id.progress_result);
        int max = FlashcardsMode.getInstance().getSizeDontKnow() + FlashcardsMode.getInstance().getSizeKnowList();
        float per = (float) FlashcardsMode.getInstance().getSizeKnowList() / max;

        progressBar.setMax(100);
        int progress = (int) (per*100);
        progressBar.setProgress(progress);

        txtKnow = findViewById(R.id.txtSizeKnowList);
        txtDontKnow = findViewById(R.id.txtSizeDontKnowList);

        txtKnow.setText(String.valueOf(FlashcardsMode.getInstance().getSizeKnowList()));
        txtDontKnow.setText(String.valueOf(FlashcardsMode.getInstance().getSizeDontKnow()));

        txtProgress = findViewById(R.id.txtProgress);
        txtProgress.setText(String.valueOf(max) + "/" + String.valueOf(max));

        txtPer = findViewById(R.id.txtPer);
        txtPer.setText(Math.round(per*100) + "%");


    }
}