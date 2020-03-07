package com.tripplanner.registration;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.data_layer.local_data.entity.User;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    Repository repository;
    String uri="";

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    //save user to firebase database
    public void savetUser(FirebaseUser firebaseUser) {
        User user = mapFirebaseUserIntoUser(firebaseUser);
        insertUser(user);
    }

    //map FirebaseUser Into User entity
    private User mapFirebaseUserIntoUser(FirebaseUser firebaseUser) {
        if(firebaseUser.getPhotoUrl()!=null)
        {
            uri=firebaseUser.getPhotoUrl().toString();
        }
        return new User(firebaseUser.getUid(), firebaseUser.getDisplayName(),uri );
    }

    private void insertUser(User user) {
        repository.insertUser(user);
    }
    public LiveData<List<Trip>> getTrips() {
        return repository.getUpComingTrips();
    }
}
