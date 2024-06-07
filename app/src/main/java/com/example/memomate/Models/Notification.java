package com.example.memomate.Models;

public class Notification {
    int image;
    String content;

    public Notification() {
    }

    public Notification(int image, String content) {
        this.image = image;
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
