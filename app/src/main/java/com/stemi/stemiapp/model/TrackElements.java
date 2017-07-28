package com.stemi.stemiapp.model;

/**
 * Created by Pooja on 25-07-2017.
 */

public class TrackElements {
    private String name;
    private int image;

    public TrackElements(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
