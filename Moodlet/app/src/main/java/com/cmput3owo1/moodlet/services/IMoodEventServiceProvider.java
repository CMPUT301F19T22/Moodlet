package com.cmput3owo1.moodlet.services;

import android.net.Uri;

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
     * Listener interface to get the filepath of the newly uploaded image
     */
    interface OnImageUploadListener {
        void onImageUploadSuccess(String filepath);
        void onImageUploadFailure();
    }

    /**
     * Listener interface to notify when mood is added/updated.
     */
    interface OnMoodUpdateListener {
        void onMoodUpdateSuccess();
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
    String addMoodEvent(MoodEvent moodEvent, OnMoodUpdateListener listener);

    /**
     * Edit an existing MoodEvent on the database.
     * @param listener The listener to notify upon completion of edit.
     * @param moodEvent The {@link MoodEvent} to edit.
     */
    void editMoodEvent(MoodEvent moodEvent, OnMoodUpdateListener listener);

    /**
     * Listen to mood history updates of the current user. Calls the listener's onMoodHistoryUpdate
     * method with the new mood history list when a change occurs.
     * @param listener The listener to pass the new mood history list to
     * @param imageToUpload The Uri to upload to FireBase.
     */
    void uploadImage(OnImageUploadListener listener, Uri imageToUpload);

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
