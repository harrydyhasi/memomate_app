package com.example.memomate.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.memomate.FBModel.StudySetModel;

import java.io.Serializable;
import java.util.ArrayList;

public class StudySet implements Serializable{
    private String id;

    private String name;
    private String desc;

    private int visible;

    private String avatar;

    private String userName;

    private ArrayList<FlashCard> flashCardList;

    public StudySet() {
    }
    public StudySet(StudySetModel A)
    {
        this.name = A.getName();
        this.desc = A.getDesc();
        this.id = A.getId();
    }
    public StudySet(String id, String name, String desc, String avatar, int visible, String userName) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.avatar = avatar;
        this.visible = visible;
        this.userName = userName;
    }
    public StudySet(String name, String avatar, String userName, ArrayList<FlashCard> flashCardList) {
        this.name = name;
        this.avatar = avatar;
        this.userName = userName;
        this.flashCardList = flashCardList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getQuantity()
    {
        return flashCardList.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<FlashCard> getFlashCardList() {
        return flashCardList;
    }

    public void setFlashCardList(ArrayList<FlashCard> flashCardList) {
        this.flashCardList = flashCardList;
    }
}
