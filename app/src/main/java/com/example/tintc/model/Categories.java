package com.example.tintc.model;

public class Categories {
    private String nameCategories;
    private int mFlagImage;

    public Categories(String nameCategories, int mFlagImage) {
        this.nameCategories = nameCategories;
        this.mFlagImage = mFlagImage;
    }

    public Categories() {
    }

    public String getNameCategories() {
        return nameCategories;
    }

    public void setNameCategories(String nameCategories) {
        this.nameCategories = nameCategories;
    }

    public int getmFlagImage() {
        return mFlagImage;
    }

    public void setmFlagImage(int mFlagImage) {
        this.mFlagImage = mFlagImage;
    }
}
