package com.tripplanner.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tripplanner.data_layer.remote.Firebase;

public class UserRepo {
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    Firebase firebase;
    LiveData<Boolean> changePassword(String password){
        MutableLiveData<Boolean> updated= new MutableLiveData<>();
        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updated.postValue(true);
                        }
                    }
                });
       return updated;
    }
    LiveData<Boolean> changeEmail(String email){

        MutableLiveData<Boolean> updated= new MutableLiveData<>();
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updated.postValue(true);
                        }
                    }
                });
        return updated;
    }



    void logout(){
        FirebaseAuth.getInstance().signOut();
    }

}
