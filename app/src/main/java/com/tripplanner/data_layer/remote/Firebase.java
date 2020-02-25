package com.tripplanner.data_layer.remote;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import static com.tripplanner.Constants.TRIPS;
import static com.tripplanner.Constants.USERS;

public class Firebase {


    private FirebaseFirestore db;
    private DocumentReference userDocumentReference;


    public Firebase() {
        db = FirebaseFirestore.getInstance();
    }

    public DocumentReference getUserDocument(String userId) {
        return db.collection(USERS).document(userId);
    }
    public DocumentReference getTripDocument(String userId,String id)
    {
        return getUserDocument(userId).collection(TRIPS).document(id);
    }

    public CollectionReference getNotesCollection(String userId, String tripid)
    {
        return getUserDocument(userId).collection(TRIPS).document(tripid).collection("notes");

    }
    public WriteBatch getBatch()
    {
        return db.batch();
    }


}
