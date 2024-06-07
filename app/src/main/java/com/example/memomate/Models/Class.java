package com.example.memomate.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Class implements Serializable {
    private String id;
    private String title;
    private String desc;
    private Boolean right;
    private String creator;
    private ArrayList<String> studySetList;
    private ArrayList<String> memberList;

    public Class() {
    }

    public Map<String, Object> toMap()
    {
        HashMap<String,Object> result = new HashMap<>();
        Map<String, Object> updates = new HashMap<>();
        updates.put("desc", "New description");
        updates.put("right", "New rights");

        return result;
    }

    public Class(String id, String title, String desc, Boolean right, String creator, ArrayList<String> studySetList, ArrayList<String> memberList) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.right = right;
        this.creator = creator;
        this.studySetList = studySetList;
        this.memberList = memberList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getRight() {
        return right;
    }

    public void setRight(Boolean right) {
        this.right = right;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public ArrayList<String> getStudySetList() {
        return studySetList;
    }

    public void setStudySetList(ArrayList<String> studySetList) {
        this.studySetList = studySetList;
    }

    public ArrayList<String> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<String> memberList) {
        this.memberList = memberList;
    }
}
