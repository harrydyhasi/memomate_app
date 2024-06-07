package com.example.memomate.Models;

public class Intro {
    private int imageBack;
    private int imageSlider;
    private int description;

    public Intro(int imageBack, int imageSlider, int description) {
        this.imageBack = imageBack;
        this.imageSlider = imageSlider;
        this.description = description;
    }

    public int getImageBack() {
        return imageBack;
    }

    public void setImageBack(int imageBack) {
        this.imageBack = imageBack;
    }

    public int getImageSlider() {
        return imageSlider;
    }

    public void setImageSlider(int imageSlider) {
        this.imageSlider = imageSlider;
    }

    public int getDescription() {
        return description;
    }

    public void setDescription(int description) {
        this.description = description;
    }
}
