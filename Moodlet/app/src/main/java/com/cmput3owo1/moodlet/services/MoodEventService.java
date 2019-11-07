package com.cmput3owo1.moodlet.services;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.MoodEventAssociation;
import com.cmput3owo1.moodlet.models.SocialSituation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MoodEventService {
    private FirebaseFirestore database;
    private FirebaseAuth auth;

    public interface OnFeedUpdateListener {
        void onFeedUpdate(ArrayList<MoodEventAssociation> newFeed);
    }

    public MoodEventService() {
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void getFeedUpdates(final OnFeedUpdateListener listener) {
        // String username = auth.getCurrentUser().getDisplayName();
        // Hardcoded until login gets merged
        String username = "tbojovic";

        Query followingQuery = database.collection("users/"+ username + "/following");

        followingQuery.orderBy("dateTime", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<MoodEventAssociation> newFeed = new ArrayList<>();

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    EmotionalState emotionalState = EmotionalState.valueOf(String.valueOf(doc.getData().get("emotionalState")));
                    String reasoning = String.valueOf(doc.getData().get("reasoning"));
                    SocialSituation socialSituation = SocialSituation.valueOf(String.valueOf(doc.getData().get("socialSituation")));
                    Timestamp timestamp = (Timestamp) doc.getData().get("dateTime");
                    //TODO: photograph
                    //TODO: location
                    MoodEvent moodEvent = new MoodEvent(emotionalState, timestamp.toDate());
                    moodEvent.setReasoning(reasoning);
                    moodEvent.setSocialSituation(socialSituation);

                    newFeed.add(new MoodEventAssociation(moodEvent, doc.getId()));
                }
                listener.onFeedUpdate(newFeed);
            }
        });
    }

    public void addMoodEvent(final MoodEvent moodEvent) {
        DocumentReference newMoodEventRef = database.collection("moodEvents").document();
        newMoodEventRef.set(moodEvent);
        //String username = auth.getCurrentUser().getDisplayName();
        final String username = "tbojovic";
        newMoodEventRef.update("username", username);

        Query followersQuery = database.collection(username + "/followers");
        followersQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String follower = document.getId();
                        String path = "users/" + follower + "/following/" + username;
                        database.document(path).set(moodEvent);
                        database.document(path).update("username", username);
                    }
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

}
