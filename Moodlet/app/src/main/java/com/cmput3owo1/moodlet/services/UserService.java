package com.cmput3owo1.moodlet.services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput3owo1.moodlet.models.FollowRequest;
import com.cmput3owo1.moodlet.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * User Service that handles all database access and authentication that
 * is required by a user. This service is to abstract the Firestore and
 * Firebase auth away from the rest of the fragments
 */
public class UserService implements IUserServiceProvider {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    /**
     * Public constructor, takes no arguments.
     */
    public UserService (){
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    
    /**
     * This function first checks if the username is taken. If the username is not taken,
     * the account will be created, otherwise it will notify the user that their username is already taken
     * @param user User details of the user to register
     * @param password Password of Account to register with.
     * @param listener Registration listener passed from fragment
     */
    @Override
    public void validateUsernameAndCreateUser(final User user, final String password, final RegistrationListener listener){
        db.collection("users").document(user.getUsername()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.getData() == null) {
                        createUser(user, password, listener);
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
     * @param user User details of the user to register
     * @param password Password of Account to register with.
     * @param listener Registration listener passed from fragment
     */
    private void createUser(final User user, String password, final RegistrationListener listener){
        auth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(user.getUsername())
                            .build();

                    currentUser.updateProfile(profileUpdates);
                    Log.v(TAG, "Putting user into DB");
                    putUserIntoDB(user, listener);
                } else {
                    listener.onRegistrationFailure();
                }
            }
        });
    }

    /**
     * This function is called to put the registered user into the database.
     * @param user The user to put into the database
     * @param listener Registration listener passed from fragment
     */
    private void putUserIntoDB(User user, final RegistrationListener listener){

        db.collection("users").document(user.getUsername()).set(user)
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

    /**
     * Logs the current user out.
     */
    @Override
    public void logoutUser(){
        auth.signOut();
    }

    /**
     * Search for users of Moodlet that begin with the searchText. Pass the results to the listener.
     * @param searchText The username prefix to search with
     * @param listener The listener to pass the user search results to
     */
    @Override
    public void searchForUsers(final String searchText, final OnUserSearchListener listener) {
        final String currentUser = auth.getCurrentUser().getDisplayName();

        db.collection("users")
                .orderBy("username")
                .whereGreaterThanOrEqualTo("username", searchText)
                .whereLessThanOrEqualTo("username", searchText + '\uf8ff')
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        final ArrayList<User> searchList = new ArrayList<>();
                        ArrayList<Task<?>> taskList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            User user = doc.toObject(User.class);
                            if (!user.getUsername().equals(currentUser)) {
                                searchList.add(user);
                                taskList.add(setFollowStatusForUser(user));
                                taskList.add(setRequestStatusForUser(user));
                            }
                        }
                        Tasks.whenAllComplete(taskList).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
                            @Override
                            public void onSuccess(List<Task<?>> tasks) {
                                listener.onSearchResult(searchList, searchText);
                            }
                        });
                    }
                });
    }

    /**
     * Send a follow request to the user specified.
     * @param user The user to send the follow request to
     * @param listener The listener to inform of a success
     */
    @Override
    public void sendFollowRequest(final User user, final OnFollowRequestListener listener) {
        String currentUser = auth.getCurrentUser().getDisplayName();
        FollowRequest followRequest = new FollowRequest(currentUser, user.getUsername());
        DocumentReference doc = db.collection("requests").document();

        doc.set(followRequest);
        doc.update("id", doc.getId())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onRequestSuccess(user);
                    }
                });
                //TODO .addOnFailureListener() - Add this later
    }

    /**
     * Accept the specified follow request. Call the listener's callback function upon success
     * @param request The follow request to accept
     * @param listener The listener to notify upon success
     */
    @Override
    public void acceptFollowRequest(FollowRequest request, final OnAcceptRequestListener listener) {
        final String newFollowerUsername = request.getRequestFrom();
        final String currentUser = request.getRequestTo();

        HashMap<String, String> followerData = new HashMap<>();
        followerData.put("username", newFollowerUsername);

        HashMap<String, String> followingData = new HashMap<>();
        followingData.put("username", currentUser);

        db.collection("users")
                .document(currentUser)
                .collection("followers")
                .document(newFollowerUsername)
                .set(followerData);

        db.collection("users")
                .document(newFollowerUsername)
                .collection("following")
                .document(currentUser)
                .set(followingData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onAcceptRequestSuccess(newFollowerUsername);
                    }
        });
        deleteFollowRequest(request);
    }

    /**
     * Deletes the follow request from Firestore database
     * @param request The follow request to delete
     */
    @Override
    public void deleteFollowRequest(FollowRequest request) {
        db.collection("requests")
                .document(request.getId())
                .delete();
    }

    /**
     * Set the follow status of the user. Looks in the Firestore database to determine if the
     * current user is following the passed in user. If so, it sets the following status of that
     * user to true.
     * @param user The user to set the following status for.
     */
    private Task<DocumentSnapshot> setFollowStatusForUser(final User user) {
        String currentUser = auth.getCurrentUser().getDisplayName();

        return db.document("users/" + currentUser + "/following/" + user.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                user.setFollowing(true);
                            }
                        }
                    }
                });
    }

    /**
     * Set the request status of the user. Looks in the Firestore database to determine if the
     * current user has sent a follow request to the passed in user. If so, it sets the request
     * status of that user to true.
     * @param user The user to set the request status for.
     */
    private Task<QuerySnapshot> setRequestStatusForUser(final User user) {
        String currentUser = auth.getCurrentUser().getDisplayName();

        return db.collection("requests")
                .whereEqualTo("requestFrom", currentUser)
                .whereEqualTo("requestTo", user.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                user.setRequested(true);
                            }
                        }
                    }
                });
    }

    /**
     * Listen to follow request updates of the current user. Calls the listener's onRequestUpdate
     * method with the new follow request list when a change occurs.
     * @param listener The listener to pass the new follow request list to
     */
    @Override
    public void getRequestUpdates(final OnRequestsUpdateListener listener) {
        String username = auth.getCurrentUser().getDisplayName();

        Query requestsQuery = db.collection("requests")
                .whereEqualTo("requestTo", username);

        requestsQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<FollowRequest> newRequests = new ArrayList<>();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    FollowRequest followRequest = doc.toObject(FollowRequest.class);
                    newRequests.add(followRequest);
                }
                listener.onRequestsUpdate(newRequests);
            }
        });
    }

}



