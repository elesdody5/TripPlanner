package com.tripplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
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
import java.util.List;

/*sara*/
/*omnia*/
/*omnia*/
public class MainActivity extends AppCompatActivity {
    /*sara*/
    private static final int RC_SIGN_IN = 123;
    LoginViewModel loginViewModel;
    FirebaseUser user;
    // to set a Listener when uer log in or log out
    private FirebaseAuth.AuthStateListener mFirebaseAuthStateListener;
    // to set Authentication for write or read in database
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigation();

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuthStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if (user == null) {
                createSignInIntent();
            } else {
                loginViewModel.savetUser(user);
            }

        };
    }

    private void setupNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(navView, navHostFragment.getNavController());

        navHostFragment.getNavController().addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.addTripFragment) {
                navView.setVisibility(View.GONE);
            } else {
                navView.setVisibility(View.VISIBLE);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                //  user = loginViewModel.getCurrentUser();
                user = FirebaseAuth.getInstance().getCurrentUser();
                loginViewModel.savetUser(user);
                Log.i("hh", "onActivityResult: " + user);
            }
        }
    }

    @Override
    protected void onPause() {

        mFirebaseAuth.removeAuthStateListener(mFirebaseAuthStateListener);
        super.onPause();

    }

    @Override
    protected void onResume() {

        mFirebaseAuth.addAuthStateListener(mFirebaseAuthStateListener);
        super.onResume();

    }
    /*sara*/
}
