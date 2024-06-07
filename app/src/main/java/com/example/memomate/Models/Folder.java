package com.example.memomate.Models;

import java.util.ArrayList;

public class Folder {
    private String title;
    private int quantity;
    private String avatar;
    private String author;
    private String description, id;

//    private ArrayList<StudySet> studySetList;

//    public ArrayList<StudySet> getStudySetList() {
//        return studySetList;
//    }

//    public void setStudySetList(ArrayList<StudySet> studySetList) {
//        this.studySetList = studySetList;
//    }

    public Folder() {
    }
    public Folder(String title, String id, String description) {
        this.title = title;
        this.id = id;
        this.description = description;
    }
    public Folder (String title, String description)
    {
        this.title= title;
        this.description= description;
    }

    public Folder(String title, int quantity, String avatar, String author) {
        this.title = title;
        this.quantity = quantity;
        this.avatar = avatar;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
