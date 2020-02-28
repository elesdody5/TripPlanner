package com.tripplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

/*sara*/
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tripplanner.R;
import com.tripplanner.data_layer.local_data.entity.Place;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.home.LoginViewModel;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/*sara*/
/*omnia*/
/*omnia*/
public class MainActivity extends AppCompatActivity {
    /*sara*/
    private static final int RC_SIGN_IN = 123;
    LoginViewModel loginViewModel;
    FirebaseUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        /*sara*/
        /*
        user = FirebaseAuth.getInstance().getCurrentUser();
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        if (user == null) {
            createSignInIntent();
            //    setContentView(R.layout.activity_main);

        } else {
            setContentView(R.layout.activity_main);
            loginViewModel.setCurrentUser(user);
        }*/

        /*sara*/
    }

    /*omnia*/
    /*omnia*/
    /*sara*/
    //TODO create login with firebase
    public void createSignInIntent() {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .build(),
                RC_SIGN_IN);
    }



   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                //  user = loginViewModel.getCurrentUser();
                user = FirebaseAuth.getInstance().getCurrentUser();
                loginViewModel.savetUser(user);
                Log.i("hh", "onActivityResult: " + user);
                setContentView(R.layout.activity_main);
            } else {

                finish();
            }
        }
    }*/
    /*sara*/
}
