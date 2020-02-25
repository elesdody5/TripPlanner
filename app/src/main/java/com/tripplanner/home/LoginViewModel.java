package com.tripplanner.home;


import android.app.Application;

import com.google.firebase.auth.FirebaseUser;
import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.entity.User;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class LoginViewModel extends AndroidViewModel {
    Repository repository;

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
        return new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString());
    }

    private void insertUser(User user) {
        repository.insertUser(user);
    }

}
