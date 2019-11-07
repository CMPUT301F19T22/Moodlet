package com.cmput3owo1.moodlet.models;

import android.graphics.Bitmap;
import android.graphics.Color;

public enum EmotionalState {
    HAPPY("Happy",Color.YELLOW),
    SAD("Sad",Color.CYAN),
    ANGRY("Angry",Color.RED),
    EXCITED("Excited",Color.rgb(255,128,0)),
    CONFUSED("Confused",Color.rgb(178,102,255)),
    SCARED("Scared",Color.rgb(204,204,255)),
    JEALOUS("Jealous", Color.rgb(102,255,178));

    private String display_name;
    private int color;
    private Bitmap emoticon;

    EmotionalState(String name, int color) {
        //TODO - add color and emoticon here once we decide them
        this.display_name = name;
        this.color = color;

    }

    @Override
    public String toString(){
        return this.display_name;
    }

    public String getDisplayName(){
        return this.display_name;
    }

    public int getColor(){
        return this.color;
    }

}
