package com.tripplanner;

import androidx.annotation.ContentView;
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

import android.content.Context;
import android.net.Uri;
import android.os.Build;
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
import android.util.DisplayMetrics;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Arrays;
import java.util.List;

import static com.tripplanner.alarm.NotificationActivity.CODE_DRAW_OVER_OTHER_APP_PERMISSION;

/*sara*/

public class MainActivity extends AppCompatActivity {
    /*sara*/
    private static final int RC_SIGN_IN = 123;
    NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }
        setupNavigation();

        /*omnia*/
        final View root=findViewById(R.id.activityRoot);
        BottomNavigationView navView = findViewById(R.id.nav_view);


        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if((root.getRootView().getHeight()-root.getHeight())>dpToPx(MainActivity.this,200)){navView.setVisibility(View.GONE);}
                else{
                    navView.setVisibility(View.VISIBLE);
                }
            }
        });

        /*omnia*/


    }

    public static float dpToPx(Context context, float valueDp){
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,valueDp,metrics);
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
