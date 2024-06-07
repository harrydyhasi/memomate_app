package com.example.memomate.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.memomate.Adapters.IntroAdapter;
import com.example.memomate.Models.Intro;
import com.example.memomate.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class IntroActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    CircleIndicator3 circleIndicator3;
    MaterialButton buttonLogin;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initView();
        slider();
    }

    private void initView()
    {
        viewPager2 = findViewById(R.id.viewPager);
        circleIndicator3 = findViewById(R.id.circle_indicator);
        buttonLogin = findViewById(R.id.btnLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void slider()
    {
        IntroAdapter sliderAdapter = new IntroAdapter(getListSlider());
        viewPager2.setAdapter(sliderAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(viewPager2.OVER_SCROLL_NEVER);

        circleIndicator3.setViewPager(viewPager2);
        sliderAdapter.registerAdapterDataObserver(circleIndicator3.getAdapterDataObserver());

    }

    private ArrayList<Intro> getListSlider()
    {
        ArrayList<Intro> introArrayList = new ArrayList<>();
        introArrayList.add(new Intro(R.drawable.b1, R.drawable.s1, R.string.desc_slider1));
        introArrayList.add(new Intro(R.drawable.b2, R.drawable.s2, R.string.desc_slider2));
        introArrayList.add(new Intro(R.drawable.b3, R.drawable.s3, R.string.desc_slider3));
        introArrayList.add(new Intro(R.drawable.b4, R.drawable.s4, R.string.desc_slider4));
        return introArrayList;
    }
}