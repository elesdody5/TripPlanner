package com.tripplanner.home;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.Entity.User;

import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
  Repository repository=new Repository();
//save user to firebase database
   public void savetUser(FirebaseUser firebaseUser)
   {
       User user= mapFirebaseUserIntoUser( firebaseUser);
       insertUser(user);
   }
   //map FirebaseUser Into User entity
   public User mapFirebaseUserIntoUser(FirebaseUser firebaseUser)
   {
       User currentUser=new User(firebaseUser.getUid(),firebaseUser.getDisplayName(),firebaseUser.getPhotoUrl().toString());
       return currentUser;
   }
   public  void  insertUser(User user)
   {
       repository.insertUser(user);
   }
   public void setCurrentUser(FirebaseUser firebaseUser)
   {
       User currentUser=new User(firebaseUser.getUid(),firebaseUser.getDisplayName(),firebaseUser.getPhotoUrl().toString());
       repository.setCurrentUser(currentUser);
   }
}
