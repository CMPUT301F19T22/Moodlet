package com.cmput3owo1.moodlet.models;

import android.graphics.Bitmap;

public class MoodEvent extends Event {
    private EmotionalState emotionalState;
    private String reasoning;
    private SocialSituation socialSituation;
    private Bitmap photograph;

    public MoodEvent(EmotionalState es) {
        super();
        emotionalState = es;
    }

    public EmotionalState getEmotionalState() {
        return emotionalState;
    }

    public void setEmotionalState(EmotionalState emotionalState) {
        this.emotionalState = emotionalState;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    public SocialSituation getSocialSituation() {
        return socialSituation;
    }

    public void setSocialSituation(SocialSituation socialSituation) {
        this.socialSituation = socialSituation;
    }

    public Bitmap getPhotograph() {
        return photograph;
    }

    public void setPhotograph(Bitmap photograph) {
        this.photograph = photograph;
    }
}
