package com.example.memomate.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Member implements Serializable {
    private String userName;
    private String avatar;
    private ArrayList<StudySet> studySets;
    private ArrayList<Folder> folders;
    private ArrayList<Class> classes;

    public Member(String userName, String avatar) {
        this.userName = userName;
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ArrayList<StudySet> getStudySets() {
        return studySets;
    }

    public void setStudySets(ArrayList<StudySet> studySets) {
        this.studySets = studySets;
    }

    public ArrayList<Folder> getFolders() {
        return folders;
    }

    public void setFolders(ArrayList<Folder> folders) {
        this.folders = folders;
    }

    public ArrayList<Class> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<Class> classes) {
        this.classes = classes;
    }

    public Member() {
    }
}
