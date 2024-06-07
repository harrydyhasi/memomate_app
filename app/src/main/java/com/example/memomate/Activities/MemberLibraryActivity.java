package com.example.memomate.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.memomate.Adapters.TabPagerAdapter;
import com.example.memomate.Fragments.TabFoldersFragment;
import com.example.memomate.Fragments.TabStudySetsFragment;
import com.example.memomate.R;
import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberLibraryActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private ViewPager viewPager;
    public TabLayout tabLayout;
    private CircleImageView imgAvatar;
    private TextView txtUserName;
    private TabPagerAdapter tabPagerAdapter;
    private String userName, avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_library);

        getIntentData();
        initView();
        addEvents();
        setUpViewPager(viewPager);
    }
    private void initView(){
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        tabLayout.setupWithViewPager(viewPager);
        btnBack = findViewById(R.id.btnBack);
        txtUserName = findViewById(R.id.txtUserName);
        imgAvatar = findViewById(R.id.imgAvatar);

        txtUserName.setText(userName);
        Glide.with(this).load(avatar).into(imgAvatar);
    }
    private void addEvents(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setUpViewPager(ViewPager viewPager) {
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
//        tabPagerAdapter.addFragment(new TabStudySetsFragment());
        tabPagerAdapter.addFragment(new TabFoldersFragment());

        viewPager.setAdapter(tabPagerAdapter);
    }
    private void getIntentData(){
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        avatar = intent.getStringExtra("avatar");
    }
}