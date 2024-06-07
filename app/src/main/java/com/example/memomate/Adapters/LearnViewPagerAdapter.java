package com.example.memomate.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.memomate.Fragments.LearnModeFragment;
import com.example.memomate.Models.Question;

import java.util.List;

public class LearnViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Question> mList;

    public LearnViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Question> list) {
        super(fm, behavior);
        this.mList = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (mList == null || mList.isEmpty())
            return null;
        Question question = mList.get(position);
        LearnModeFragment learnModeFragment = new LearnModeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Question", question);
        learnModeFragment.setArguments(bundle);

        return learnModeFragment;
    }

    @Override
    public int getCount() {
        if (mList != null) return mList.size();
        return 0;
    }
}
