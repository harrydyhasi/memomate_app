package com.example.memomate.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.memomate.Adapters.LearnViewPagerAdapter;
import com.example.memomate.Models.FlashCard;
import com.example.memomate.Models.LearnMode;
import com.example.memomate.Models.Question;
import com.example.memomate.R;

import java.util.ArrayList;
import java.util.List;

public class LearnModeActivity extends AppCompatActivity {

    ImageButton btnClose;
    ProgressBar progressBar;
    ViewPager viewPager;
    private List<Question> mListQuestions;
    LearnMode learnMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_mode);
        learnMode = new LearnMode(getListFromStudySetDetail());
        mListQuestions = learnMode.init();
        initView();
        LearnViewPagerAdapter learnViewPagerAdapter = new LearnViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mListQuestions);
        viewPager.setAdapter(learnViewPagerAdapter);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private void initView()
    {
        btnClose = findViewById(R.id.btnClose);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(mListQuestions.size());
        viewPager = findViewById(R.id.viewPagerContainer);
    }


    public List<FlashCard> getListFromStudySetDetail()
    {
        Bundle b = getIntent().getExtras();
        return (List<FlashCard>) b.getSerializable("FlashCardList");
    }

    public void nextQuestion()
    {
        int cur = viewPager.getCurrentItem();
        if (cur == mListQuestions.size() - 1)
        {
            finish();
        }

        else
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    public void increaseProgressBar()
    {
        progressBar.setProgress(progressBar.getProgress() + 1);
    }

}