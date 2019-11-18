package com.cmput3owo1.moodlet.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput3owo1.moodlet.models.FollowRequest;
import com.cmput3owo1.moodlet.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * User Service that handles all database access and authentication that
 * is required by a user. This service is to abstract the Firestore and
 * Firebase auth away from the rest of the fragments
 */
public class UserService implements IUserServiceProvider{

    FirebaseAuth auth;
    FirebaseFirestore db;

    public UserService (){
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    /**
     * This function is called to check if there is an existing instance of the logged in user.
     */
    @Override
    public boolean hasPreviousLogin(){
        return auth.getCurrentUser() != null;
    }

    /**
     * This function first checks if the username is taken. If the username is not taken,
     * the account will be created, otherwise it will notify the user that their username is already taken
     * @param username Username to register with.
     * @param email Email to register with.
     * @param password Password of Account to register with.
     * @param fullname Full name of user registering.
     * @param listener Registration listener passed from fragment
     */
    @Override
    public void validateUsernameAndCreateUser(final String username, final String email, final String password, final String fullname, final RegistrationListener listener){

        db.collection("users").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.v("data","Cached document data: " + document.getData());
                    if(document.getData() == null) {
                        createUser(username, email, password, fullname, listener);
                    } else {
                        listener.onUsernameIsTaken();
                    }
                } else {
                    listener.onDatabaseAccessFailure();
                }
            }
        });
    }

    /**
     * This a wrapper function that is called to create a user account with their email and password.
     * @param username Username to register with.
     * @param email Email to register with.
     * @param password Password of Account to register with.
     * @param fullname Full name of user registering.
     * @param listener Registration listener passed from fragment
     */
    @Override
    public void createUser(final String username, final String email, String password, final String fullname, final RegistrationListener listener){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                    user.updateProfile(profileUpdates);
                    Log.v(TAG, "Putting user into DB");
                    putUserIntoDB(email, fullname, username, listener);
                } else {
                    listener.onRegistrationFailure();
                }
            }
        });
    }

    /**
     * This function is called to put the registered user into the database.
     * @param username Username to register with.
     * @param email Email to register with.
     * @param fullname Full name of user registering.
     * @param listener Registration listener passed from fragment
     */
    @Override
    public void putUserIntoDB(final String email, final String fullname, final String username, final RegistrationListener listener){
        HashMap<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("fullname", fullname);
        data.put("username", username);

        db.collection("users").document(username).set(data)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        listener.onRegistrationSuccess();
                    } else {
                        listener.onDatabaseAccessFailure();
                    }
                }
            });
    }

    /**
     * This is a wrapper function that is called to login a user with their email and password.
     * @param txt_email Email to login with.
     * @param txt_password Password to login with.
     * @param listener Login listener passed from fragment
     */
    @Override
    public void loginUser(String txt_email, String txt_password, final LoginListener listener){
        auth.signInWithEmailAndPassword(txt_email, txt_password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()) {
                           listener.onLoginSuccess();
                       } else {
                           listener.onLoginFailure();
                       }
                   }
               }
            );
    }

    @Override
    public void getUsers(String searchText, final OnUserSearchListener listener) {
        Query usersQuery = db.collection("users")
                .orderBy("username")
                .whereGreaterThanOrEqualTo("username", searchText)
                .whereLessThanOrEqualTo("username", searchText + '\uf8ff');

        usersQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<User> searchList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        User user = doc.toObject(User.class);
                        searchList.add(user);
                        getFollowStatusForUser(user, listener);
                    }
                    listener.onSearchResult(searchList);

                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void getFollowStatusForUser(final User user, final OnUserSearchListener listener) {
        String currentUser = auth.getCurrentUser().getDisplayName();

        DocumentReference followingDoc;
        final Query requestedQuery;

        followingDoc = db.document("users/" + currentUser + "/following/" + user.getUsername());
        requestedQuery = db.collection("requests")
                .whereEqualTo("requestFrom", currentUser)
                .whereEqualTo("requestTo", user.getUsername())
                .limit(1);

        followingDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user.setFollowing(true);
                        listener.onUserUpdate();
                    }
                } else {
                    //Log.d(TAG, "Failed with: ", task.getException());
                }
            }
        });

        requestedQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    user.setRequested(true);
                    listener.onUserUpdate();
                }
            }
        });


    }

    @Override
    public void sendFollowRequest(String username, final OnFollowRequestListener listener) {
        String currentUser = auth.getCurrentUser().getDisplayName();

        FollowRequest followRequest = new FollowRequest(currentUser, username);
        db.collection("requests").document()
                .set(followRequest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onRequestSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onRequestFailure();
                    }
                });
    }
}



