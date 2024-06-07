package com.example.memomate.Models;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String avatar;
    private String userName;
    private String email;
    private String passWord;
    private ArrayList<Folder> folders;
    private ArrayList<Class> classes;

    public User() {
    }
    public User(@NonNull DataSnapshot snapshot) {
        this.avatar = snapshot.child("avatar").getValue(String.class);
        this.userName = snapshot.child("email").getValue(String.class);
        this.email = snapshot.child("passWord").getValue(String.class);
        this.passWord = snapshot.child("passWord").getValue(String.class);
    }

    public User(String avatar, String userName, String email, String passWord) {
        this.avatar = avatar;
        this.userName = userName;
        this.email = email;
        this.passWord = passWord;
    }
    public User(String userName, String email, String passWord) {
        this.userName = userName;
        this.email = email;
        this.passWord = passWord;
    }


    public User(String userName, String email, String passWord, ArrayList<Folder> folders, ArrayList<Class> classes) {
        this.userName = userName;
        this.email = email;
        this.passWord = passWord;
        this.folders = folders;
        this.classes = classes;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
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
}
