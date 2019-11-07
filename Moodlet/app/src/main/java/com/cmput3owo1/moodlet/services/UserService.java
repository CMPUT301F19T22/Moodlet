package com.cmput3owo1.moodlet.services;

import android.util.Log;

import androidx.annotation.NonNull;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;

import java.util.HashMap;


public class UserService {

   public interface CallbackFunction {
        void callback();
    }


    /**
     * This function is called to register a user with their email and password
     * @param collectionReference A reference to the database collection that we are writing to.
     * @param username Username to register with.
     * @param email Email to register with.
     * @param password Password of Account to register with.
     * @param fullname Full name of user registering.
     * @param success Callback function for successful registration and database addition
     * @param failure Callback function for failed registration and database addition
     * @return none
     */
    public static void registerUser(final FirebaseAuth auth, final CollectionReference collectionReference, final String username, final String email, String password, final String fullname, final CallbackFunction success, final CallbackFunction failure){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                    user.updateProfile(profileUpdates);
                    putUserIntoDB(collectionReference, email, fullname, username, success, failure);
                } else {
                    Log.e("DB add error", "Failed to add user to db");
                }
            }
        });
    }

    /**
     * This function is called to put the registered user into the database
     * @param collectionReference A reference to the database collection that we are writing to.
     * @param username Username to register with.
     * @param email Email to register with.
     * @param fullname Full name of user registering.
     * @param success Callback function for successful registration and database addition
     * @param failure Callback function for failed registration and database addition
     * @return none
     */
    public static void putUserIntoDB(final CollectionReference collectionReference, final String email, final String fullname, final String username, final CallbackFunction success, final CallbackFunction failure){
        HashMap<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("fullname", fullname);
        data.put("username", username);

        collectionReference.document(username).set(data)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        success.callback();
                    } else {
                        failure.callback();
                    }
                }
            });
    }

    /**
     * This function is called to login a user with their email and password
     * @param txt_email Email to login with.
     * @param txt_password Password to login with.
     * @return none
     */
    public static void loginUser(FirebaseAuth auth, String txt_email, String txt_password, final CallbackFunction success, final CallbackFunction failure){
        auth.signInWithEmailAndPassword(txt_email, txt_password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()) {
                           success.callback();
                       } else {
                           failure.callback();
                       }
                   }
               }
            );
    }

}



