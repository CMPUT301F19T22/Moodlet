package com.cmput3owo1.moodlet.services;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput3owo1.moodlet.R;
import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.MoodEventAssociation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class that provides Firebase database services for the {@link MoodEvent} class.
 * It handles everything to do with MoodEvents and their interaction with the database. This
 * includes adding, deleting, editing, and querying.
 */
public class MoodEventService implements IMoodEventServiceProvider {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private StorageReference storageRef;
    private FirebaseStorage storage;

    /**
     * Default constructor for MoodEventService.
     */
    public MoodEventService() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    /**
     * Listen to feed updates of the current user. Calls the listener onFeedUpdate with the new
     * feed when a change occurs.
     * @param listener The listener to pass the new feed to
     */
    @Override
    public void getFeedUpdates(final OnFeedUpdateListener listener) {
        String username = auth.getCurrentUser().getDisplayName();

        Query followingQuery = db.collection("users/"+ username + "/following");

        followingQuery.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<MoodEventAssociation> newFeed = new ArrayList<>();

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    MoodEvent moodEvent = doc.toObject(MoodEvent.class);
                    newFeed.add(new MoodEventAssociation(moodEvent, doc.getId()));
                }
                listener.onFeedUpdate(newFeed);
            }
        });
    }

    /**
     * Add a Mood Event to the database.
     * @param moodEvent The {@link MoodEvent} to add to the database.
     * @param listener The listener to notify upon completion of add.
     */
    @Override
    public String addMoodEvent(final MoodEvent moodEvent, OnMoodUpdateListener listener) {
        DocumentReference newMoodEventRef = db.collection("moodEvents").document();
        newMoodEventRef.set(moodEvent);

        final String username = auth.getCurrentUser().getDisplayName();
        newMoodEventRef.update("username", username);
        newMoodEventRef.update("id", newMoodEventRef.getId());

        moodEvent.setId(newMoodEventRef.getId());
        updateFollowersFeed(moodEvent);

        listener.onMoodUpdateSuccess();
        return newMoodEventRef.getId();
    }


    /**
     * Edit an existing MoodEvent on the database.
     * @param moodEvent The {@link MoodEvent} to edit.
     * @param listener The listener to notify upon completion of edit.
     */
    @Override
    public void editMoodEvent(final MoodEvent moodEvent, final OnMoodUpdateListener listener){
        DocumentReference newMoodEventRef = db.collection("moodEvents").document(moodEvent.getId());
        newMoodEventRef.set(moodEvent);

        final String username = auth.getCurrentUser().getDisplayName();
        newMoodEventRef.update("username", username).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onMoodUpdateSuccess();
            }
        });

        // See if moodEvent is the most recent - if so, we have to notify followers
        Query moodHistoryQuery = db.collection("moodEvents")
                .whereEqualTo("username", username)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1);
        moodHistoryQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    MoodEvent mostRecentEvent = doc.toObject(MoodEvent.class);
                    if (mostRecentEvent.getId().equals(moodEvent.getId())) {
                        updateFollowersFeed(moodEvent);
                    }
                }
            }
        });
    }

    /**
     * Update the feed or friend activity of the current user's followers with the specified MoodEvent
     * @param moodEvent The most recent {@link MoodEvent} of the current user
     */
    private void updateFollowersFeed(final MoodEvent moodEvent) {
        final String username = auth.getCurrentUser().getDisplayName();

        Query followersQuery = db.collection("users/" + username + "/followers");
        followersQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String follower = document.getId();
                    String path = "users/" + follower + "/following/" + username;
                    if (moodEvent != null) {
                        db.document(path).set(moodEvent);
                        db.document(path).update("username", username);
                    } else {
                        HashMap<String, String> data = new HashMap<String, String>();
                        data.put("username", username);
                        db.document(path).set(data);
                    }
                }
            }
        });
    }

    @Override
    public void updateFollowerWithMostRecentMood(final String followerUsername) {
        final String currentUser = auth.getCurrentUser().getDisplayName();

        Query moodHistoryQuery = db.collection("moodEvents")
                .whereEqualTo("username", currentUser)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1);

        moodHistoryQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshots) {
                for (QueryDocumentSnapshot doc : snapshots) {
                    String path = "users/" + followerUsername + "/following/" + currentUser;
                    MoodEvent mostRecentEvent = doc.toObject(MoodEvent.class);
                    db.document(path).set(mostRecentEvent);
                    db.document(path).update("username", currentUser);
                }
            }
        });
    }

    /**
     * Delete swiped MoodEvent from database.
     * @param moodEvent The mood event to be deleted.
     * @param listener The listener to notify upon completion of deletion.
     */
    @Override
    public void deleteMoodEvent(final MoodEvent moodEvent, final OnMoodDeleteListener listener) {
        String username = auth.getCurrentUser().getDisplayName();

        // See if moodEvent is the most recent - if so, we have to notify followers
        Query moodHistoryQuery = db.collection("moodEvents")
                .whereEqualTo("username", username)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(2);
        moodHistoryQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot snapshots) {
                List<MoodEvent> moodEvents = snapshots.toObjects(MoodEvent.class);
                if (!moodEvents.isEmpty()) {
                    final MoodEvent mostRecentEvent = moodEvents.get(0);
                    MoodEvent nextMostRecentEvent = null;

                    if (moodEvents.size() > 1) {
                        nextMostRecentEvent = moodEvents.get(1);
                    }

                    final MoodEvent finalNextMostRecentEvent = nextMostRecentEvent;

                    DocumentReference moodEventRef = db.collection("moodEvents").document(moodEvent.getId());
                    moodEventRef.delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    listener.onMoodDeleteSuccess();

                                    // Most recent event was deleted, we need to update followers
                                    // with the next most recent event
                                    if (mostRecentEvent.getId().equals(moodEvent.getId())) {
                                        updateFollowersFeed(finalNextMostRecentEvent);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    listener.onMoodDeleteFailure();
                                }
                            });
                }
            }
        });
    }

    /**
     * Listen to mood history updates of the current user. Calls the listener's onMoodHistoryUpdate
     * method with the new mood history list when a change occurs.
     * @param listener The listener to pass the new mood history list to
     */
    @Override
    public void getMoodHistoryUpdates(OnMoodHistoryUpdateListener listener) {
        String username = auth.getCurrentUser().getDisplayName();

        Query moodHistoryQuery = db.collection("moodEvents")
                .whereEqualTo("username", username)
                .orderBy("date", Query.Direction.DESCENDING);

        runMoodHistoryQuery(moodHistoryQuery, listener);
    }

    /**
     * Listen to mood history updates of the current user. Calls the listener's onMoodHistoryUpdate
     * method with the new mood history list when a change occurs.
     * @param listener The listener to pass the new mood history list to
     * @param filterBy The {@link EmotionalState} to filter the list by.
     */
    @Override
    public void getMoodHistoryUpdates(OnMoodHistoryUpdateListener listener, EmotionalState filterBy) {
        String username = auth.getCurrentUser().getDisplayName();

        Query moodHistoryQuery = db.collection("moodEvents")
                .whereEqualTo("username", username)
                .whereEqualTo("emotionalState", filterBy)
                .orderBy("date", Query.Direction.DESCENDING);

        runMoodHistoryQuery(moodHistoryQuery, listener);
    }

    /**
     * Run the specified Mood History query and notify the listener on completion
     * @param moodHistoryQuery The query to run
     * @param listener The listener to pass the new mood history list to
     */
    private void runMoodHistoryQuery(Query moodHistoryQuery, final OnMoodHistoryUpdateListener listener) {
        moodHistoryQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<MoodEvent> newHistory = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    MoodEvent moodEvent = doc.toObject(MoodEvent.class);
                    newHistory.add(moodEvent);
                }
                listener.onMoodHistoryUpdate(newHistory);
            }
        });
    }

    /**
     * Listen to mood history updates of the current user. Calls the listener's onMoodHistoryUpdate
     * method with the new mood history list when a change occurs.
     * @param listener The listener to pass the new mood history list to
     * @param imageToUpload The Uri to upload to FireBase.
     */
    @Override
    public void uploadImage(final OnImageUploadListener listener, final Uri imageToUpload){

        final String username = auth.getCurrentUser().getDisplayName();
        final String filepath = "images/" + username + "/" + imageToUpload.getLastPathSegment();

        final StorageReference filepathRef = storageRef.child(filepath);

        filepathRef.putFile(imageToUpload).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.onImageUploadFailure();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepathRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        listener.onImageUploadSuccess(uri.toString());
                    }
                });

            }
        });

    }

}

