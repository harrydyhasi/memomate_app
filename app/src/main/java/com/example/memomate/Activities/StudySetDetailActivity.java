package com.example.memomate.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memomate.Adapters.FlashCardAdapter;
import com.example.memomate.Adapters.FlipCardAdapter;
import com.example.memomate.Models.FlashCard;
import com.example.memomate.Models.StudySet;
import com.example.memomate.R;
import com.example.memomate.viewmodel.FlashCardViewModel;
import com.example.memomate.viewmodel.StudySetViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.relex.circleindicator.CircleIndicator3;

public class StudySetDetailActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    CircleIndicator3 circleIndicator3;
    FlipCardAdapter flipCardAdapter;
    FlashCardAdapter flashCardAdapter;
    RecyclerView rcvCards;
    ImageView btnMenu;
    CircleImageView imgAvatar;
    ImageButton btnReturn;
    CardView btnFlashcards, btnMatch, btnLearn;

    TextView txtStudySetName, txtQuantity, txtUsrname;

    ArrayList<FlashCard> flashCards = new ArrayList<>();

    StudySet currStudySet;
    private Handler slideHandler = new Handler();


    FlashCardViewModel flashCardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_set_detail);
        initView();
        getIntentFromFolderActivity();
        flashCardSlider();
        flashCardList();
    }


    private void flashCardSlider() {
        flipCardAdapter = new FlipCardAdapter(flashCards);
        viewPager2.setAdapter(flipCardAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r*0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                slideHandler.removeCallbacks(slideR);
            }
        });

        circleIndicator3.setViewPager(viewPager2);
        flipCardAdapter.registerAdapterDataObserver(circleIndicator3.getAdapterDataObserver());
    }

    private void flashCardList()
    {
        flashCardAdapter = new FlashCardAdapter(flashCards);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        rcvCards.setLayoutManager(linearLayoutManager);
        rcvCards.setAdapter(flashCardAdapter);
    }

    private void initView() {
        viewPager2 = findViewById(R.id.viewPager);
        circleIndicator3 = findViewById(R.id.circle_indicator);
        rcvCards = findViewById(R.id.rcvCards);
        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogMenu();
            }
        });

        btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnFlashcards = findViewById(R.id.btnFlashcards);
        btnFlashcards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudySetDetailActivity.this, StackFlashCardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("FlashCardList", (Serializable) flashCards);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnMatch = findViewById(R.id.btnMatch);
        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudySetDetailActivity.this, MatchModeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("FlashCardList", (Serializable) flashCards);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnLearn = findViewById(R.id.btnLearn);
        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudySetDetailActivity.this, LearnModeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("FlashCardList", (Serializable) flashCards);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        txtStudySetName = findViewById(R.id.txtSetName);
        imgAvatar = findViewById(R.id.imgAvatar);
        txtUsrname = findViewById(R.id.txtUsername);
        txtQuantity = findViewById(R.id.txtQuantity);
    }


    private Runnable slideR = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    private void showDialogMenu()
    {
        FirebaseUser firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_menu_study_set);
        Window window = dialog.getWindow();
        if (window == null)
        {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        window.setWindowAnimations(R.style.BottomDialogAnimation);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        TextView btnEdit = dialog.findViewById(R.id.btnEdit);
        TextView btnAddToFolder = dialog.findViewById(R.id.btnAddToFolder);
        TextView btnShare = dialog.findViewById(R.id.btnShare);
        TextView btnDelete = dialog.findViewById(R.id.btnDelete);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
                Intent intent = new Intent(StudySetDetailActivity.this, EditStudySetActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("oldStudySet", (Serializable) currStudySet);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
                StudySetViewModel studySetViewModel = new StudySetViewModel(firebaseAuth.getUid());
                studySetViewModel.delete(currStudySet.getId());
                Toast.makeText(StudySetDetailActivity.this, currStudySet.getId(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(StudySetDetailActivity.this, "Xoa ne", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void getIntentFromFolderActivity()
    {
        Bundle b = getIntent().getExtras();
        StudySet S = (StudySet) b.getSerializable("S");
        currStudySet = S;
        flashCards = S.getFlashCardList();
        txtUsrname.setText(S.getUserName());
        txtStudySetName.setText(S.getName());
        //imgAvatar.setImageResource(R.drawable.images);
        txtQuantity.setText(String.valueOf(S.getQuantity()) + " terms");
    }
}