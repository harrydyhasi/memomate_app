package com.example.memomate.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LearnMode {
    private List<FlashCard> flashCards;

    public LearnMode() {
    }

    public LearnMode(List<FlashCard> flashCards) {
        Collections.shuffle(flashCards);
        this.flashCards = flashCards;
    }

    public List<Question> init()
    {
        List<Question> questions = new ArrayList<>();
        for (int i=0; i<flashCards.size(); i++)
        {
            questions.add(new Question(flashCards.get(i).getTerm(), getChoices(i), flashCards.get(i).getDefinition()));
        }
        return questions;
    }

    public List<String> getChoices(int pos)
    {
        List<String> choices = new ArrayList<>();
        for (int i=0; i<flashCards.size(); i++)
        {
            if (i == pos) continue;
            choices.add(flashCards.get(i).getDefinition());
        }
        shuffle(choices, pos);
        return choices;
    }

    private void shuffle(List<String> choices, int pos)
    {
        Collections.shuffle(choices);
        int size = 1;
        if (flashCards.size() < 4)
            size = flashCards.size();
        else size = 3;
        choices = choices.subList(0,size-1);
        choices.add(flashCards.get(pos).getDefinition());
        Collections.shuffle(choices);
    }
    public List<FlashCard> getFlashCards() {
        return flashCards;
    }

    public void setFlashCards(List<FlashCard> flashCards) {
        this.flashCards = flashCards;
    }
}
