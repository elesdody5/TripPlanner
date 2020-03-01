package com.tripplanner.registration;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.tripplanner.R;
import com.tripplanner.databinding.ForgetPasswordFragmentBinding;

public class ForgetPasswordFragment extends Fragment {

    private ForgetPasswordViewModel mViewModel;
    private FirebaseAuth auth;;
    ForgetPasswordFragmentBinding forgetPasswordFragmentBinding;
    View view;
    public static ForgetPasswordFragment newInstance() {
        return new ForgetPasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         forgetPasswordFragmentBinding= DataBindingUtil.inflate(
                        inflater, R.layout.forget_password_fragment, container, false);
        view = forgetPasswordFragmentBinding.getRoot();
        forgetPasswordFragmentBinding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetPassword();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ForgetPasswordViewModel.class);
        // TODO: Use the ViewModel
    }
    private void sendResetPassword()
    {
        String email = forgetPasswordFragmentBinding.forgetemailET.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            forgetPasswordFragmentBinding.forgetemailET.setError("Please enter email");
            return;
        }

        auth.sendPasswordResetEmail(email)

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Snackbar snackbar = Snackbar
                                    .make(forgetPasswordFragmentBinding.ConstraintLayout, "We have sent you instructions to reset your password", Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.YELLOW);
                            snackbar.show();
                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(forgetPasswordFragmentBinding.ConstraintLayout, "incorrect email,please enter correct one to reset password", Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.YELLOW);
                            snackbar.show();
                        }

                    }
                });
    }

}
