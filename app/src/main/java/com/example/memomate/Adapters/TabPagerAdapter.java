package com.example.memomate.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.memomate.Fragments.TabFoldersFragment;

import java.util.ArrayList;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentList;
    public TabPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Study sets";
            case 1:
                return "Folders";
            default:
                return "Study sets";
        }
    }
    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }
}
