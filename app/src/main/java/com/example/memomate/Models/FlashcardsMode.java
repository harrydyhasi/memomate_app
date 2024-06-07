package com.example.memomate.Models;

import java.util.ArrayList;
import java.util.List;

public class FlashcardsMode {
    private List<FlashCard> listDontKnow;
    private List<FlashCard> listKnow;
    public static FlashcardsMode instance;

    private FlashcardsMode()
    {
        listKnow = new ArrayList<>();
        listDontKnow = new ArrayList<>();
    }

    public static FlashcardsMode getInstance()
    {
        if (instance == null)
        {
            instance = new FlashcardsMode();
        }
        return instance;
    }

    public void newPlay()
    {
        listKnow = new ArrayList<>();
        listDontKnow = new ArrayList<>();
    }

    public int getSizeKnowList()
    {
        return listKnow.size();
    }

    public int getSizeDontKnow()
    {
        return listDontKnow.size();
    }

    public void incrementKnow(FlashCard A)
    {
        listKnow.add(A);
    }

    public void incrementDontKnow(FlashCard A)
    {
        listDontKnow.add(A);
    }


}
