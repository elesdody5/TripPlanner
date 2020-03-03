package com.tripplanner.profile;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.tripplanner.R;
import com.tripplanner.databinding.FragmentProfileBinding;
import com.tripplanner.home.HomeViewModel;


public class ProfileFragment extends Fragment {

    ProfileViewModel model;
    FirebaseUser user;
    private FragmentProfileBinding binding;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        View view = binding.getRoot();

        binding.saveemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.editEmail.getText().toString().equals("")){
                    model.ChangePassword(binding.editEmail.getText().toString()).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if(aBoolean==true)
                            {
                                Toast toast = Toast.makeText(getContext(),"Email Changed !", Toast.LENGTH_SHORT);
                            }
                        }
                    });}

                else{
                    binding.password2.setError("Please Enter Email");
                }


            }
        });

        binding.savepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.password1.getText().toString().equals(binding.password2.getText().toString())&&binding.password1.getText().toString().length()>=6){
                model.ChangePassword(binding.password1.getText().toString()).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if(aBoolean==true)
                        {
                            Toast toast = Toast.makeText(getContext(),"Password Changed Successfully!", Toast.LENGTH_SHORT);
                        }
                    }
                });}
                else if(binding.password1.getText().toString().equals("")||binding.password1.getText().length()<6){binding.password1.setError("Please Enter Password");}
                else if (binding.password2.getText().toString().equals("")||binding.password2.getText().length()<6) {
                    binding.password2.setError("Enter at least 6 characters");
                }
                else{
                    binding.password2.setError("Enter at least 6 characters");
                }

            }
        });
        binding.cancelemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editAccount.setVisibility(View.GONE);
                binding.editEmail.setText("");
                binding.editPassword.setVisibility(View.GONE);
            }
        });
        binding.cancelpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              binding.editAccount.setVisibility(View.GONE);
                binding.password1.setText("");
                binding.password2.setText("");
              binding.editPassword.setVisibility(View.GONE);
            }
        });
        binding.logoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              model.logout();
              //navigate
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginFragment);
            }
        });
        binding.editClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              binding.editAccount.setVisibility(View.VISIBLE);
            }
        });

        binding.passClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.editPassword.setVisibility(View.VISIBLE);
            }
        });


        return view ;
    }


    void setModel(){
        model= ViewModelProviders.of(requireActivity()).get(ProfileViewModel.class);
        user= FirebaseAuth.getInstance().getCurrentUser();
        binding.setUser(user);
        if(!user.getPhotoUrl().toString().equals("")){
        Picasso.get().load(user.getPhotoUrl()).into(binding.ProfileImage);}


    }
}
