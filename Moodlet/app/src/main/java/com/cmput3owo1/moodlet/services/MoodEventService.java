package com.cmput3owo1.moodlet.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput3owo1.moodlet.activities.MoodEditorActivity;
import com.cmput3owo1.moodlet.models.EmotionalState;
import com.cmput3owo1.moodlet.models.MoodEvent;
import com.cmput3owo1.moodlet.models.MoodEventAssociation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

/**
 * A class that provides Firebase database services for the {@link MoodEvent} class.
 * It handles everything to do with MoodEvents and their interaction with the database. This
 * includes adding, deleting, editing, and querying.
 */
public class MoodEventService {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private StorageReference storageRef;
    private FirebaseStorage storage;
    private StorageReference filepathRef;


    /**
     * Listener interface to get the new feed (MoodEvents of users you are following) upon an update
     */
    public interface OnFeedUpdateListener {
        void onFeedUpdate(ArrayList<MoodEventAssociation> newFeed);
    }

    /**
     * Listener interface to get the new mood history upon an update
     */
    public interface OnMoodHistoryUpdateListener {
        void onMoodHistoryUpdate(ArrayList<MoodEvent> newHistory);
    }

    public interface OnImageUploadListener {
        void onImageUploadSuccess(String filepath);
        void onImageUploadFailure();
    }

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
     * @param moodEvent The mood event to add
     */
    public void addMoodEvent(final MoodEvent moodEvent) {
        DocumentReference newMoodEventRef = db.collection("moodEvents").document();
        newMoodEventRef.set(moodEvent);

        final String username = auth.getCurrentUser().getDisplayName();
        newMoodEventRef.update("username", username);

        Query followersQuery = db.collection("users/" + username + "/followers");
        followersQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String follower = document.getId();
                        String path = "users/" + follower + "/following/" + username;
                        db.document(path).set(moodEvent);
                        db.document(path).update("username", username);
                    }
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * Listen to mood history updates of the current user. Calls the listener's onMoodHistoryUpdate
     * method with the new mood history list when a change occurs.
     * @param listener The listener to pass the new mood history list to
     * @param filterBy The optional {@link EmotionalState} to filter the list by.
     */
    public void getMoodHistoryUpdates(final OnMoodHistoryUpdateListener listener, @Nullable EmotionalState filterBy) {
        String username = auth.getCurrentUser().getDisplayName();

        Query moodHistoryQuery = db.collection("moodEvents")
                .whereEqualTo("username", username)
                .orderBy("dateTime", Query.Direction.DESCENDING);

        if (filterBy != null) {
            moodHistoryQuery.whereEqualTo("emotionalState", filterBy);
        }

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

    public void uploadImage(final OnImageUploadListener listener, final Uri imageToUpload){


        final String username = auth.getCurrentUser().getDisplayName();
        final String filepath = "images/" + username + "/" + imageToUpload.getLastPathSegment();
        boolean uploaded = false;


        filepathRef = storageRef.child(filepath);

        filepathRef.putFile(imageToUpload).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.onImageUploadFailure();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...

                listener.onImageUploadSuccess(filepath);

            }
        });

    }

}

