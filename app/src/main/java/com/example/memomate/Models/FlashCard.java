package com.example.memomate.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class FlashCard implements Serializable {
    private String id;
    private String term;
    private String definition;

    public FlashCard() {
    }

    public FlashCard(String id, String term, String definition) {
        this.id = id;
        this.term = term;
        this.definition = definition;
    }

    public FlashCard(String term, String definition) {
        this.term = term;
        this.definition = definition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public boolean isMatch(String term, String def)
    {
        return (Objects.equals(term, this.term) && Objects.equals(def, this.definition));
    }

    @Override
    public String toString() {
        return "FlashCard{" +
                "id='" + id + '\'' +
                ", term='" + term + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }
}
