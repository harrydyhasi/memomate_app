package com.example.memomate.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.memomate.Models.FlashCard;
import com.example.memomate.repository.FlashCardRepository;

import java.util.ArrayList;
import java.util.List;

public class FlashCardViewModel extends ViewModel {
    private FlashCardRepository flashCardRepository;

    private List<FlashCard> mFlashCard;

    public FlashCardViewModel(String uid, String studySetId)
    {
        flashCardRepository = new FlashCardRepository(uid,studySetId);
        //flashCardRepository.loadList();
//        mListFlashCardLiveData = flashCardRepository.get();
    }


    public void addFlashCardList(List<FlashCard> flashCards)
    {
        flashCardRepository.addListFlashCard(flashCards);
    }

    public ArrayList<FlashCard> getFlashCardList()
    {
        return flashCardRepository.getListFlashCard();
    }


    public ArrayList<FlashCard> getFlashCardS()
    {
        return flashCardRepository.getmListFlashCard();
    }


    public void remove()
    {
        flashCardRepository.removeFlashCardList();
    }
}
