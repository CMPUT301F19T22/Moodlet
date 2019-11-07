package com.cmput3owo1.moodlet.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseUtil {
    private FirebaseAuth auth;
    private FirebaseFirestore database;

    public DatabaseUtil() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseFirestore getDatabase() {
        return database;
    }
}
