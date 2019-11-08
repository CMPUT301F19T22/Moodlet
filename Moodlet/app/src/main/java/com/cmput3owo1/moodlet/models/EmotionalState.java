package com.cmput3owo1.moodlet.models;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.cmput3owo1.moodlet.R;

/**
 * Enum for the emotional states that a {@link MoodEvent} can have as descriptors of mood.
 * Includes HAPPY, SAD, ANGRY, EXCITED, CONFUSED, SCARED and JEALOUS.
 */
public enum EmotionalState {
    HAPPY("Happy",Color.YELLOW),
    SAD("Sad",Color.CYAN),
    ANGRY("Angry",Color.RED),
    EXCITED("Excited",Color.rgb(255,128,0)),
    CONFUSED("Confused",Color.rgb(178,102,255)),
    SCARED("Scared",Color.rgb(204,204,255)),
    JEALOUS("Jealous", Color.rgb(102,255,178));

//    HAPPY("Happy", R.color.happy),
//    SAD("Sad",R.color.sad),
//    ANGRY("Angry",R.color.angry),
//    EXCITED("Excited",R.color.excited),
//    CONFUSED("Confused",R.color.confused),
//    SCARED("Scared",R.color.scared),
//    JEALOUS("Jealous", R.color.jealous);

    private String display_name;
    private int color;
    private Bitmap emoticon;

    /**
     * Constructor for the MoodEvent
     * @param name The display name for the given emotional state
     * @param color The color associated with the given emotional state
     */
    EmotionalState(String name, int color) {
        //TODO - add color and emoticon here once we decide them
        this.display_name = name;
        this.color = color;
    }

    /**
     * Makes the default return of toString the display name of the emotional state when called.
     * @return The display name
     */
    @Override
    public String toString(){
        return this.display_name;
    }

    /**
     * Returns the display name of the emotional state when called.
     * @return The display name
     */
    public String getDisplayName(){
        return this.display_name;
    }

    /**
     * Returns the color of the emotional state when called.
     * @return The color
     */
    public int getColor(){return this.color; }

}
