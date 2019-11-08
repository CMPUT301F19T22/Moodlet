package com.cmput3owo1.moodlet.models;

/**
 * Enum for the social situations that a {@link MoodEvent} can occur in.
 * Includes ALONE, ONE_PERSON, SEVERAL, CROWD
 */
public enum SocialSituation {
    ALONE("Alone"),
    ONE_PERSON("With another person"),
    SEVERAL("With several people"),
    CROWD("In a crowd");

    String display_name;

    /**
     * Constructor for the social situation that takes the display name
     * @param name The display name
     */
    SocialSituation(String name){
        this.display_name = name;
    }

    /**
     * Makes the default return of toString the display name of the social situation when called.
     * @return The display name
     */
    @Override
    public String toString(){
        return this.display_name;
    }

    /**
     * Returns the display name of the social situation when called
     * @return The display name
     */
    public String getDisplayName(){
        return this.display_name;
    }

}


