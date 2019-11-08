package com.cmput3owo1.moodlet.services;

import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.MoodEventAssociation;

import java.util.ArrayList;

/**
 * Interface for providing database services for {@link MoodEvent} objects. Allows the specific
 * service provider to be substituted out with little hassle.
 */
public interface IMoodEventServiceProvider {
    /**
     * Listener interface to get the new feed (MoodEvents of users you are following) upon an update
     */
    interface OnFeedUpdateListener {
        void onFeedUpdate(ArrayList<MoodEventAssociation> newFeed);
    }

    /**
     * Listener interface to get the new mood history upon an update
     */
    interface OnMoodHistoryUpdateListener {
        void onMoodHistoryUpdate(ArrayList<MoodEvent> newHistory);
    }

    /**
     * Listen to feed updates of the current user. Calls the listener onFeedUpdate with the new
     * feed when a change occurs.
     * @param listener The listener to pass the new feed to
     */
    void getFeedUpdates(OnFeedUpdateListener listener);

    /**
     * Add a Mood Event to the database.
     * @param moodEvent The mood event to add
     */
    void addMoodEvent(MoodEvent moodEvent);

    /**
     * Listen to mood history updates of the current user. Calls the listener's onMoodHistoryUpdate
     * method with the new mood history list when a change occurs.
     * @param listener The listener to pass the new mood history list to
     */
    void getMoodHistoryUpdates(OnMoodHistoryUpdateListener listener);

    /**
     * Listen to mood history updates of the current user. Calls the listener's onMoodHistoryUpdate
     * method with the new mood history list when a change occurs.
     * @param listener The listener to pass the new mood history list to
     * @param filterBy The {@link EmotionalState} to filter the list by.
     */
    void getMoodHistoryUpdates(OnMoodHistoryUpdateListener listener, EmotionalState filterBy);
}
