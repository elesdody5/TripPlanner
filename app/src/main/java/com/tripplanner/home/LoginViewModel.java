package com.tripplanner.home;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
  //  private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
   // public MutableLiveData<FirebaseUser> mutableLiveData=new MutableLiveData<>();
    public FirebaseUser getCurrentUser() {

        return  FirebaseAuth.getInstance().getCurrentUser();
    }
}
