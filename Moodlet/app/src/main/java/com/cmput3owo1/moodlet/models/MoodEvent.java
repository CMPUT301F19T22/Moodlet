package com.cmput3owo1.moodlet.models;

/**
 * A class that extends from {@link Event} and keeps track of mood events. A mood event is an event
 * that triggered a particular mood in the form of an {@link EmotionalState}. A mood event can
 * also keep track of other details such as a textual reasoning for the event, the
 * {@link SocialSituation} the event occurred in, and a photograph explaining the event.
 */
public class MoodEvent extends Event {
    private EmotionalState emotionalState;
    private String reasoning;
    private SocialSituation socialSituation;
    private String photograph;

    /**
     * Default constructor for the MoodEvent.
     */
    public MoodEvent() {
        // Need this for Firestore
        super();
    }

    /**
     * Constructor for the MoodEvent
     * @param emotionalState This is the emotional state the MoodEvent incurred.
     */
    public MoodEvent(EmotionalState emotionalState) {
        super();
        this.emotionalState = emotionalState;
    }

    /**
     * This gets the emotional state of the MoodEvent
     * @return The emotional state
     */
    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    /**
     * This sets the emotional state of the MoodEvent
     * @param emotionalState The emotional state
     */
    public void setEmotionalState(EmotionalState emotionalState) {
        this.emotionalState = emotionalState;
    }

    /**
     * This gets the textual reasoning for the MoodEvent
     * @return The event reasoning
     */
    public String getReasoning() {
        return reasoning;
    }

    /**
     * This sets the textual reasoning behind the MoodEvent
     * @param reasoning The event reasoning
     */
    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    /**
     * This gets the social situation in which the event occurred
     * @return The social situation
     */
    public SocialSituation getSocialSituation() {
        return socialSituation;
    }

    /**
     * This sets the social situation in which the event occurred
     * @param socialSituation The social situation
     */
    public void setSocialSituation(SocialSituation socialSituation) {
        this.socialSituation = socialSituation;
    }

    /**
     * This gets the FirebaseStorage path for the photograph that explains the MoodEvent
     * @return The photograph path
     */
    public String getPhotographPath() {
        return photograph;
    }

    /**
     * This sets the FirebaseStorage path for the photograph that explains the MoodEvent
     * @param photographPath The photograph path
     */
    public void setPhotographPath(String photographPath) {
        this.photograph = photograph;
    }
}
