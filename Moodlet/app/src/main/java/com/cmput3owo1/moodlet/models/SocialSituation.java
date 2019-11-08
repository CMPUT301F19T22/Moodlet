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

    SocialSituation(String name){
        this.display_name = name;
    }

    @Override
    public String toString(){
        return this.display_name;
    }

    public String getDisplayName(){
        return this.display_name;
    }

}


