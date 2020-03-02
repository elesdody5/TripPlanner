package com.tripplanner.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;

public class ProfileViewModel extends AndroidViewModel {
    UserRepo repo=new UserRepo();




    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }

    LiveData<Boolean> ChangePassword(String password){
     return repo.changePassword(password);
    }
    LiveData<Boolean> ChangeEmail(String email){
       return repo.changeEmail(email);
    }
    void logout(){
        repo.logout();
    }

}
