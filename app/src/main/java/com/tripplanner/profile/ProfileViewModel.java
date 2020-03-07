package com.tripplanner.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;
import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.entity.Trip;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    UserRepo repo=new UserRepo();
    Repository repository=new Repository(getApplication());




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

    public LiveData<List<Trip>> getTrips() {

        return repository.getUpComingTrips();
    }

}
