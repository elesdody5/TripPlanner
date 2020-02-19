package com.tripplanner.data_layer.remote;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tripplanner.data_layer.local_data.Entity.User;

import java.util.HashMap;
import java.util.Map;

public class Firebase {
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String USERS = "Users";
    public static final String TRIPS = "trip";
    public static final String USER_ID = "User_id";
    private FirebaseFirestore db;
    private DocumentReference userDocumentReference;


    public Firebase() {
        db = FirebaseFirestore.getInstance();
    }

    public DocumentReference getUserCollection(String userId) {
        return db.collection(USERS).document(userId);
    }


}
