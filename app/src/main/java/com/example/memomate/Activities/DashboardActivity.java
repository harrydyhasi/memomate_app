package com.example.memomate.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.memomate.Adapters.NotificationAdapter;
import com.example.memomate.Fragments.ClassFragment;
import com.example.memomate.Fragments.HomeFragment;
import com.example.memomate.Fragments.LibraryFragment;
import com.example.memomate.Fragments.ProfileFragment;
import com.example.memomate.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DashboardActivity extends AppCompatActivity {
    private HomeFragment homeFragment = new HomeFragment();
    private ClassFragment classFragment = new ClassFragment();
    private LibraryFragment libraryFragment = new LibraryFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private BottomNavigationView bottomNavigationView;
    private ImageButton btnCreate;
    private NotificationAdapter notificationAdapter;
    FirebaseUser firebaseUser;
    private String username;
//    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        username = firebaseUser.getUid();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        getFormWidget();
        addEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getFormWidget();
        addEvents();
    }

    private void getFormWidget(){
        homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Bundle homeFragmentBundle = new Bundle();

        homeFragmentBundle.putString("userName", username);
        homeFragment.setArguments(homeFragmentBundle);
        fragmentTransaction.replace(R.id.container, new HomeFragment()).commit();

//        classFragment = new ClassFragment();
//        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction1.replace(R.id.container, classFragment).commit();
//
//        libraryFragment = new LibraryFragment();
//        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction2.replace(R.id.container, libraryFragment).commit();
//
//        profileFragment = new ProfileFragment();
//        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction3.replace(R.id.container, profileFragment).commit();
    }
    private void addEvents(){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int i = menuItem.getItemId();
                if(i== R.id.nav_home){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container,  new HomeFragment()).commit();
                } else if (i == R.id.nav_class) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new ClassFragment()).commit();
                }else if (i == R.id.nav_library) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new LibraryFragment()).commit();
                }else if (i == R.id.nav_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
                }
                return true;
            }
        });
        ImageButton btnCreate = findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateBottomSheetDialog();
            }
        });
    }
    public void getIntentData(){
        Intent intent = getIntent();
        if(intent != null){
            username = intent.getStringExtra("UserId");
            Log.d("USERNAME DASHBOARD", username);
        }

    }
    private void showCreateBottomSheetDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_bottom_sheet);

        LinearLayout btnCreateStudySet = dialog.findViewById(R.id.btn_create_studyset);
        LinearLayout btnCreateFolder = dialog.findViewById(R.id.btn_create_folder);
        LinearLayout btnCreateClass = dialog.findViewById(R.id.btn_create_class);

        btnCreateStudySet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(DashboardActivity.this, CreateSetActivity.class);
                startActivity(i);
            }
        });

        btnCreateFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(DashboardActivity.this, CreateFolderActivity.class);
                String t = "New folder";
                i.putExtra("title", t);
                startActivity(i);
            }
        });
        btnCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(DashboardActivity.this, CreateClassActivity.class);
                startActivity(i);
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.google.android.material.R.style.Animation_Design_BottomSheetDialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}