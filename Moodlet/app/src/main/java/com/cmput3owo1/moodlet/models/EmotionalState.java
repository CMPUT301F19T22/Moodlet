package com.cmput3owo1.moodlet.models;

import android.graphics.Bitmap;
import android.graphics.Color;

public enum EmotionalState {
    HAPPY(),
    SAD(),
    ANGRY(),
    EXCITED(),
    CONFUSED(),
    SCARED(),
    JEALOUS();

    private Color color;
    private Bitmap emoticon;

    EmotionalState() {
        //TODO - add color and emoticon here once we decide them
    }

    public Bitmap getEmoticon() {
        return emoticon;
    }

    public Color getColor() {
        return color;
    }
}
