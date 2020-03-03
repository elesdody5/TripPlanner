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
    NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigation();
    }

    private void setupNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
         navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(navView, navHostFragment.getNavController());

        navHostFragment.getNavController().addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.addTripFragment||destination.getId()==R.id.previousTripDetailsFragment2||
            destination.getId()==R.id.loginFragment|| destination.getId()==R.id.forgetPasswordFragment
            ||destination.getId()==R.id.signUpFragment) {
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

    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    /*sara*/
}
