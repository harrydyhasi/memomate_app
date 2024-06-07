package com.example.memomate.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.memomate.Adapters.CardStackAdapter;
import com.example.memomate.Models.FlashCard;
import com.example.memomate.Models.FlashcardsMode;
import com.example.memomate.R;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.List;

public class StackFlashCardActivity extends AppCompatActivity {
    TextView txtKnow, txtDontKnow, txtProgress;
    CardStackView cardStackView;
    ProgressBar progressBar;
    int currentPos;
    ImageButton btnClose;
    private List<FlashCard> flashCards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_flash_card);

        initView();
        createCardStackView();
        FlashcardsMode.getInstance().newPlay();
    }

    private void initView()
    {
        flashCards = getListFromStudySetDetail();
        txtDontKnow = findViewById(R.id.txtStillLearning);
        txtKnow = findViewById(R.id.txtKnow);
        txtProgress = findViewById(R.id.txtProgress);
        txtProgress.setText("0/" + String.valueOf(flashCards.size()));
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(flashCards.size());
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createCardStackView()
    {
        cardStackView = findViewById(R.id.cardStackView);
        CardStackAdapter cardStackAdapter = new CardStackAdapter(flashCards);
        cardStackView.setAdapter(cardStackAdapter);
        CardStackLayoutManager manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                if (direction == Direction.Right)
                {
                    txtKnow.setBackgroundResource(R.drawable.solid_green_rectangle_top_right_bottom_right_corner);
                    txtKnow.setText("+1");

                    txtDontKnow.setText(String.valueOf(FlashcardsMode.getInstance().getSizeDontKnow()));
                    txtDontKnow.setBackgroundResource(R.drawable.orange_rectangle_top_right_bottom_right_corner);

                }
                else if (direction == Direction.Left)
                {
                    txtDontKnow.setBackgroundResource(R.drawable.solid_orange_rectangle_top_right_bottom_right_corner);
                    txtDontKnow.setText("+1");

                    txtKnow.setText(String.valueOf(FlashcardsMode.getInstance().getSizeKnowList()));
                    txtKnow.setBackgroundResource(R.drawable.green_rectangle_top_right_bottom_right_corner);
                }
            }

            @Override
            public void onCardSwiped(Direction direction) {
                FlashCard A = flashCards.get(currentPos);
                if (direction == Direction.Right)
                {
                    FlashcardsMode.getInstance().incrementKnow(A);
                    txtKnow.setText(String.valueOf(FlashcardsMode.getInstance().getSizeKnowList()));
                    txtKnow.setBackgroundResource(R.drawable.green_rectangle_top_right_bottom_right_corner);

                }
                else{
                    FlashcardsMode.getInstance().incrementDontKnow(A);
                    txtDontKnow.setText(String.valueOf(FlashcardsMode.getInstance().getSizeDontKnow()));
                    txtDontKnow.setBackgroundResource(R.drawable.orange_rectangle_top_right_bottom_right_corner);
                }
                txtProgress.setText(String.valueOf(currentPos + 1) + "/"  + String.valueOf(flashCards.size()));
                progressBar.setProgress(progressBar.getProgress() + 1);

                if (currentPos == flashCards.size() - 1)
                {
                    finish();
                    Intent intent = new Intent(StackFlashCardActivity.this, FinishedFlashcardsModeActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {
                txtKnow.setBackgroundResource(R.drawable.green_rectangle_top_right_bottom_right_corner);
                txtDontKnow.setBackgroundResource(R.drawable.orange_rectangle_top_right_bottom_right_corner);
                txtKnow.setText(String.valueOf(FlashcardsMode.getInstance().getSizeKnowList()));
                txtDontKnow.setText(String.valueOf(FlashcardsMode.getInstance().getSizeDontKnow()));
            }

            @Override
            public void onCardAppeared(View view, int position) {
                currentPos = position;
            }

            @Override
            public void onCardDisappeared(View view, int position) {

            }
        });
        manager.setDirections(Direction.HORIZONTAL);
        cardStackView.setLayoutManager(manager);
    }

    private List<FlashCard> populateDummyFlashcards() {
        List<FlashCard> flashCards = new ArrayList<>();
        flashCards.add(new FlashCard("given that", "với việc"));
        flashCards.add(new FlashCard("which is", "cái mà"));
        flashCards.add(new FlashCard("so that", "để"));
        flashCards.add(new FlashCard("as long as", "miễn là"));
        flashCards.add(new FlashCard("regarding", "về, liên quan đến"));
        return flashCards;
    }

    private List<FlashCard> getListFromStudySetDetail()
    {
        Bundle b = getIntent().getExtras();
        return (List<FlashCard>) b.getSerializable("FlashCardList");
    }
}