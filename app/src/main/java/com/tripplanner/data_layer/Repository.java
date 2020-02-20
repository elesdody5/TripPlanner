package com.tripplanner.data_layer;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tripplanner.data_layer.local_data.Entity.Note;
import com.tripplanner.data_layer.local_data.Entity.Trip;
import com.tripplanner.data_layer.local_data.Entity.User;
import com.tripplanner.data_layer.local_data.TripDao;
import com.tripplanner.data_layer.remote.Firebase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Repository {
    private static final String TAG = "Repository";
    TripDao tripDao;
    Firebase firebase;
    User user;

    // TODO create repo
    @Inject
    public Repository(TripDao tripDao, Firebase firebase) {
        this.tripDao = tripDao;
        this.firebase = firebase;
    }

    public Repository() {

    }

    public LiveData<List<Trip>> getUpComingTrips() throws Exception {
        throw new Exception("Not finished yet");

//        refreshData();
//
//        return tripDao.getAllTrips();

    }

    private void refreshData(String userId) {
        firebase.getUserCollection(userId).collection(Firebase.TRIPS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        //tripDao.insertTrip(new Trip(document.));
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });


    }

    public LiveData<List<Trip>> getPastTrips() throws Exception {
        throw new Exception("Not finished yet");
    }


    public void insertUser(User user) {

    }

    public LiveData<List<Note>> getTodoNotes(String TripId) throws Exception {
        throw new Exception("Not finished yet");
    }

    public void insertNote(Note note) {

    }

    public void insertNote(List<Note> note) {

    }

    public void setCurrentUser(User user) {
        this.user = user;
    }

}
