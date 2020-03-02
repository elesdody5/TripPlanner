package com.tripplanner.registration.signup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tripplanner.Constants;
import com.tripplanner.R;
import com.tripplanner.databinding.SignUpFragmentBinding;
import com.tripplanner.previous_trip_details.GsonUtils;

public class SignUpFragment extends Fragment {

    private SignUpViewModel mViewModel;
    private FirebaseAuth auth;
    SignUpFragmentBinding signUpFragmentBinding;
    View view;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        signUpFragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.sign_up_fragment, container, false);
         view=signUpFragmentBinding.getRoot();
        auth=FirebaseAuth.getInstance();
        signUpFragmentBinding.signUpCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        //sign in
        signUpFragmentBinding.SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignin();
            }
        });
        return view;
    }

    private void goToSignin() {
        Bundle bundle = new Bundle();
        FirebaseUser currentUser;
        currentUser=null;
        String personJsonString = GsonUtils.getGsonParser().toJson(currentUser);
        bundle.putString(Constants.USERS, personJsonString);
        Navigation.findNavController(view).navigate(R.id.action_signUpFragment_to_loginFragment,bundle);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        // TODO: Use the ViewModel
    }
    private void signUp()
    {
        final String email=signUpFragmentBinding.EmailTextView.getText().toString().trim();
        final String password=signUpFragmentBinding.passwordTextView.getText().toString().trim();
        final String confirmPassword=signUpFragmentBinding.confirmPasswordEt.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            signUpFragmentBinding.EmailTextView.setError("Enter your email!");
            return;
        }
        if(TextUtils.isEmpty(password)){
            signUpFragmentBinding.passwordTextView.setError("Enter password!");
            return;
        }
        if(TextUtils.isEmpty(confirmPassword)){
            signUpFragmentBinding.confirmPasswordEt.setError("Confirm your password!");
            return;
        }
        if(!confirmPassword.equals(password))
        {
            signUpFragmentBinding.confirmPasswordEt.setError("Passwords Don't Match");
            return;
        }
        if(password.length()<6){
            signUpFragmentBinding.passwordTextView.setError("password too short!,minimum 6 char");
            return;
        }
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            Snackbar snackbar = Snackbar
                                    .make(signUpFragmentBinding.ConstraintLayout, "Registration Failed", Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.YELLOW);
                            snackbar.show();

                        }
                        else {
                            goToSignin();
                        }

                    }
                });

    }

}
