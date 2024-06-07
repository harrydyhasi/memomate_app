package com.example.memomate.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.memomate.Models.FlashCard;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FlashCardRepository {
    private DatabaseReference databaseReference;
    ArrayList<FlashCard> mListFlashCard;

    public FlashCardRepository(String udi, String studySetId) {

        this.databaseReference = FirebaseDatabase.getInstance().getReference("User").child(udi).child("studyset_list").child(studySetId).child("flashcards");
    }



    public void addListFlashCard(List<FlashCard> flashCards)
    {
        for (int i=0; i<flashCards.size(); i++) {
            String flashCardId = databaseReference.push().getKey();
            flashCards.get(i).setId(flashCardId);
            databaseReference.child(flashCardId).setValue(flashCards.get(i));
        }
    }

    public ArrayList<FlashCard> getListFlashCard()
    {
        ArrayList<FlashCard> flashCards = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    FlashCard flashCard = dataSnapshot.getValue(FlashCard.class);
                    flashCards.add(flashCard);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return flashCards;
    }


    public void removeFlashCardList()
    {
        databaseReference.removeValue();
    }


    public ArrayList<FlashCard> getmListFlashCard()
    {
        Log.d("LISTF", this.mListFlashCard.toString());
        return mListFlashCard;
    }


}
