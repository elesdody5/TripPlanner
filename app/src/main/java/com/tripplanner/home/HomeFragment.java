package com.tripplanner.home;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripplanner.R;
import com.tripplanner.data_layer.local_data.Entity.Trip;
import com.tripplanner.databinding.FragmentHomeBinding;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
// TODO create view model
// TODO create recyclerview done
// TODO create home layout done

public class HomeFragment extends Fragment {

    HomeViewModel model;
    LiveData<List<Trip>> trips;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private  FragmentHomeBinding binding;
    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*omnia*/
       binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
       View view = binding.getRoot();
       layoutManager=new LinearLayoutManager(this.getContext());
       setViewModel();
        return view;

    }
    void setViewModel(){
        model= ViewModelProviders.of(this.getActivity()).get(HomeViewModel.class);
        model.getTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> Trips) {
                HomeFragment.this.displayTrips(Trips);
                displayTrips(Trips);
            }
        });
    }
   void displayTrips(List<Trip> trips){
       mAdapter=new HomeAdapter(trips);
       binding.TripList.setHasFixedSize(true);
       binding.TripList.setAdapter(mAdapter);
       binding.TripList.setLayoutManager(layoutManager);

   }

    public void addTrip(View view){


   }

    /*omnia*/
}
