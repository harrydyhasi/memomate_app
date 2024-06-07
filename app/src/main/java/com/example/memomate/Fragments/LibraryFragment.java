package com.example.memomate.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.memomate.Activities.CreateFolderActivity;
import com.example.memomate.Activities.CreateSetActivity;
import com.example.memomate.Activities.CreateClassActivity;
import com.example.memomate.Activities.CreateFolderActivity;
import com.example.memomate.Activities.CreateSetActivity;
import com.example.memomate.Activities.DashboardActivity;
import com.example.memomate.Adapters.TabPagerAdapter;
import com.example.memomate.Models.StudySet;
import com.example.memomate.R;
import com.google.android.material.tabs.TabLayout;

public class LibraryFragment extends Fragment {
    private Context context;
    private ViewPager viewPager;
    public TabLayout tabLayout;
    private TabPagerAdapter tabPagerAdapter;
    private ImageButton btnCreate;
    int currTab = 0;

    public LibraryFragment(Context context) {
        this.context = context;
    }
    public LibraryFragment(){

    }


//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//
//    }

    private void setUpViewPager(ViewPager viewPager) {
        tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager());
        tabPagerAdapter.addFragment(new TabStudySetsFragment());
        tabPagerAdapter.addFragment(new TabFoldersFragment());

        viewPager.setAdapter(tabPagerAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.view_pager);


        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.view_pager);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currTab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        btnCreate = view.findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currTab == 0)
                {
                    Intent A = new Intent(view.getContext(), CreateSetActivity.class);
                    startActivity(A);
                }
                else {
                    Intent A = new Intent(view.getContext(), CreateFolderActivity.class);
                    startActivity(A);
                }

            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            int tabIndex = bundle.getInt("tab_index", 0);
            switchToTab(tabIndex);
        }
        return view;
    }
    public void switchToTab(int position) {
        viewPager.setCurrentItem(position);
    }
}