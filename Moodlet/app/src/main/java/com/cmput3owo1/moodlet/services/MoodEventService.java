package com.cmput3owo1.moodlet.services;

import androidx.annotation.Nullable;

import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.MoodEventAssociation;
import com.cmput3owo1.moodlet.models.SocialSituation;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MoodEventService {
    private FirebaseFirestore database;

    public interface OnFeedUpdateListener {
        void onFeedUpdate(ArrayList<MoodEventAssociation> newFeed);
    }

    public MoodEventService() {
        database = FirebaseFirestore.getInstance();
    }

    public void getFeedUpdates(final OnFeedUpdateListener listener) {
        CollectionReference following = database.collection("users/tbojovic/following");

        following.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<MoodEventAssociation> newFeed = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    EmotionalState emotionalState = EmotionalState.valueOf(String.valueOf(doc.getData().get("emotionalState")));
                    String reasoning = String.valueOf(doc.getData().get("reasoning"));
                    SocialSituation socialSituation = SocialSituation.valueOf(String.valueOf(doc.getData().get("socialSituation")));
                    Timestamp timestamp = (Timestamp) doc.getData().get("dateTime");
                    //photograph
                    //location
                    MoodEvent moodEvent = new MoodEvent(emotionalState, timestamp.toDate());

                    moodEvent.setReasoning(reasoning);
                    moodEvent.setSocialSituation(socialSituation);
                    newFeed.add(new MoodEventAssociation(moodEvent, doc.getId()));
                }
                listener.onFeedUpdate(newFeed);
            }
        });
    }
}
