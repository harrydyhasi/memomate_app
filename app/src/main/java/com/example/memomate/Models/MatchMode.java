package com.example.memomate.Models;

import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MatchMode {
    private List<FlashCard> mFlashCard;

    public MatchMode(List<FlashCard> A) {
        Collections.shuffle(A);
        int size = min(6, A.size());
        this.mFlashCard = A.subList(0,size);
    }

    public List<String> init()
    {
        List<String> terms = getTerms();
        List<String> defs = getDefs();
        List<String> result = new ArrayList<>(terms);
        result.addAll(defs);
        Collections.shuffle(result);
        return result;
    }

    public List<String> getTerms()
    {
        List<String> lst = new ArrayList<>();
        for (int i=0; i<mFlashCard.size();i++)
        {
            lst.add(mFlashCard.get(i).getTerm());
        }
        return  lst;
    }

    public List<String> getDefs()
    {
        List<String> lst = new ArrayList<>();
        for (int i=0; i<mFlashCard.size();i++)
        {
            lst.add(mFlashCard.get(i).getDefinition());
        }
        return  lst;
    }

    public boolean isMatch(String s1, String s2)
    {
        FlashCard flashCard = findFlashCardWithTerm(s1);
        if (flashCard != null)
        {
            return Objects.equals(s2, flashCard.getDefinition());
        }
        else
        {
            flashCard = findFlashCardWithDef(s1);
            return Objects.equals(s2, flashCard.getTerm());
        }

    }

    public FlashCard findFlashCardWithTerm(String term)
    {
        for (int i=0; i<mFlashCard.size(); i++)
        {
            if (Objects.equals(term, mFlashCard.get(i).getTerm())) return mFlashCard.get(i);
        }
        return null;
    }

    public FlashCard findFlashCardWithDef(String def)
    {
        for (int i=0; i<mFlashCard.size(); i++)
        {
            if (Objects.equals(def, mFlashCard.get(i).getDefinition())) return mFlashCard.get(i);
        }
        return null;
    }

    public int getSize()
    {
        return mFlashCard.size();
    }
}
