package com.tripplanner.registration;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tripplanner.Constants;
import com.tripplanner.R;
import com.tripplanner.alarm.NotificationActivity;
import com.tripplanner.data_layer.remote.Firebase;

import com.tripplanner.data_layer.local_data.entity.Trip;

import com.tripplanner.databinding.LoginFragmentBinding;
import com.tripplanner.home.HomeViewModel;
import com.tripplanner.previous_trip_details.GsonUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;

import static android.content.Context.ALARM_SERVICE;


public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    LoginFragmentBinding loginFragmentBinding;
    private FirebaseAuth auth;
    String email, password;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private static final int RC_SIGN_IN = 234;
    private static final String TAG = "simplifiedcoding";
    GoogleSignInClient mGoogleSignInClient;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    View view;
    Bundle args;
    List<Trip> upcomingTrips;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        loginFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.login_fragment, container, false);
        view = loginFragmentBinding.getRoot();
        progressBar = view.findViewById(R.id.progress_bar);

        auth = FirebaseAuth.getInstance();
         args = getArguments();

        loginFragmentBinding.loginCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        //email password

        view.findViewById(R.id.sign_in_button_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        //forget password
        loginFragmentBinding.forgetpassWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetPassword();
            }
        });
        //sign Up
        loginFragmentBinding.signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSignUp();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

    //email_password
    public void login() {
        email = loginFragmentBinding.EmailTextView.getText().toString();
        password = loginFragmentBinding.passwordTextView.getText().toString();
        if (email.isEmpty()) {
            loginFragmentBinding.EmailTextView.setError("Please enter email");
            //    loginFragmentBinding.textInputLayout3.setError("Error message");
        } else if (password.isEmpty()) {
            loginFragmentBinding.passwordTextView.setError("Please enter password");
        } else {
            progressBar.setVisibility(View.VISIBLE);
            loginFragmentBinding.loginCardView.setVisibility(View.INVISIBLE);
            loginWithFireBase();
        }
    }

    //email password
    private void loginWithFireBase() {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) {
                                progressBar.setVisibility(View.INVISIBLE);
                                loginFragmentBinding.loginCardView.setVisibility(View.VISIBLE);
                                Snackbar snackbar = Snackbar
                                        .make(loginFragmentBinding.ConstraintLayout, "Incorrect password!", Snackbar.LENGTH_LONG);
                                snackbar.setActionTextColor(Color.YELLOW);
                                snackbar.show();


                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                                loginFragmentBinding.loginCardView.setVisibility(View.VISIBLE);
                                Snackbar snackbar = Snackbar
                                        .make(loginFragmentBinding.ConstraintLayout, "Incorrect email or password", Snackbar.LENGTH_LONG);
                                snackbar.setActionTextColor(Color.YELLOW);
                                snackbar.show();

                            }

                        } else {
                            progressBar.setVisibility(View.GONE);
                            FirebaseUser currentUser = auth.getCurrentUser();
                            goHomeScreen();

                        }
                    }
                });
    }

    public void forgetPassword() {
        //go to forgetPasswordFragment
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgetPasswordFragment);


    }

    /*  public void loginWithFacebook() {

          callbackManager = CallbackManager.Factory.create();
          LoginButton loginButton =view.findViewById(R.id.loginButton);
          loginButton.setReadPermissions("email", "public_profile");
          loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
              @Override
              public void onSuccess(LoginResult loginResult) {
                  Log.d("12", "facebook:onSuccess:" + loginResult);
                  handleFacebookAccessToken(loginResult.getAccessToken());
              }

              @Override
              public void onCancel() {
                  Log.d("12", "facebook:onCancel");
              }

              @Override
              public void onError(FacebookException error) {
                  Log.d("12", "facebook:onError", error);
              }
          });

          firebaseAuth = FirebaseAuth.getInstance();
          firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
              @Override
              public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                  FirebaseUser user = firebaseAuth.getCurrentUser();

                  if (user != null) {
                      //go to home
                      mViewModel.savetUser(user);
                      Log.i("12", "onSuccess: ");

                      goMainScreen();


                  }
              }
          };
      }

      private void handleFacebookAccessToken(AccessToken token) {
          Log.d("12", "handleFacebookAccessToken:" + token);

          AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
          auth.signInWithCredential(credential)
                  .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {
                          if (task.isSuccessful()) {
                              // Sign in success, update UI with the signed-in user's information
                              Log.d("12", "signInWithCredential:success");
                              FirebaseUser user = auth.getCurrentUser();
                             // updateUI(user);
                          } else {
                              // If sign in fails, display a message to the user.
                              Log.w("12", "signInWithCredential:failure", task.getException());
                             // Toast.makeText(FacebookLoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //  updateUI(null);
                          }

                          // ...
                      }
                  });
      }
  */
    private void goHomeScreen() {
        Log.d(TAG, "goHomeScreen: "+auth.getCurrentUser().getUid());
        mViewModel.savetUser(auth.getCurrentUser());
        //navigation to go home
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser;
             currentUser = auth.getCurrentUser();
        //String userJsonString="";
//        currentUser= GsonUtils.getGsonParser().fromJson(userJsonString, FirebaseUser.class);
        if (currentUser != null) {
            goHomeScreen();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getActivity(), "User Signed In", Toast.LENGTH_SHORT).show();
                            goHomeScreen();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private  void gotoSignUp()
    {
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signUpFragment);
    }


}
