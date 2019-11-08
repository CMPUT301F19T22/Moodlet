package com.cmput3owo1.moodlet.services;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class UserService {

    FirebaseAuth auth;
    FirebaseFirestore db;

    public UserService (){
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public interface RegistrationListener {
        void onRegistrationSuccess();
        void onRegistrationFailure();
        void onAddToDatabaseFailure();
    }

    public interface LoginListener {
        void onLoginSuccess();
        void onLoginFailure();
    }

    /**
     * This function is called to check if there is an existing instance of the logged in user.
     * @return none
     */
    public boolean hasPreviousLogin(){
        return auth.getCurrentUser() != null;
    }


    /**
     * This function is called to register a user with their email and password.
     * @param username Username to register with.
     * @param email Email to register with.
     * @param password Password of Account to register with.
     * @param fullname Full name of user registering.
     * @return none
     */
    public void registerUser(final String username, final String email, String password, final String fullname, final RegistrationListener listener){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                    user.updateProfile(profileUpdates);
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
     * @return none
     */
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
                        listener.onAddToDatabaseFailure();
                    }
                }
            });
    }

    /**
     * This function is called to login a user with their email and password.
     * @param txt_email Email to login with.
     * @param txt_password Password to login with.
     * @return none
     */
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

}



