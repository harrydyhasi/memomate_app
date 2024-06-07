package com.example.memomate.Models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Question implements Serializable {
    private String question;
    private List<String> choices;
    private String answer;

    public Question() {
    }

    public Question(String question, List<String> choices, String answer) {
        this.question = question;
        this.choices = choices;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect(String cont)
    {
        return Objects.equals(cont, answer);
    }
}
