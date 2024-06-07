package com.example.memomate.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.example.memomate.R;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminateDrawable(new ThreeBounce());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
//                    intent.putExtra("UserId", user.getUid());
                    startActivity(intent);
                } else {
                    Intent i = new Intent(SplashActivity.this, IntroActivity.class);
                    startActivity(i);
                }


            }
        }, 2000);
    }
}