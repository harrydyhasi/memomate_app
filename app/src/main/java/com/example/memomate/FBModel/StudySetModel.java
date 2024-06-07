package com.example.memomate.FBModel;

import java.util.HashMap;
import java.util.Map;

public class StudySetModel {
    private String id;
    private String name;
    private String desc;
    private int visible; //0;: just me, 1: everyone, 2: password

    public StudySetModel() {
    }

    public StudySetModel(String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.visible = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public Map<String, Object> toMap()
    {
        HashMap<String,Object> result = new HashMap<>();
        result.put("name", name);
        result.put("desc", desc);
        result.put("visible", visible);
        return result;
    }
}
