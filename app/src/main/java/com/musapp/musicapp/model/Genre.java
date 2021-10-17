package com.musapp.musicapp.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

public class Genre {
    private String name;
    private Integer imageResource;

    @Exclude
    private boolean isChecked;

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public Genre() {
    }

    public Genre(String name, Integer imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public Integer getImageResource() {
        return imageResource;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageResource(Integer imageResource) {
        this.imageResource = imageResource;
    }
}

