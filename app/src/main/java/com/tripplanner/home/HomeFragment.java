package com.tripplanner.home;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.material.snackbar.Snackbar;
import com.tripplanner.R;
import com.tripplanner.alarm.NotificationActivity;
import com.tripplanner.data_layer.local_data.entity.Place;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
// TODO create view model
// TODO create recyclerview done
// TODO create home layout done

public class HomeFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    HomeViewModel model;
    LiveData<List<Trip>> trips;
    List<Trip> alltrips=new ArrayList<>();
    private HomeAdapter mAdapter;
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
       mAdapter=new HomeAdapter();
        binding.TripList.setHasFixedSize(true);
        binding.TripList.setAdapter(mAdapter);
        binding.TripList.setItemAnimator(new DefaultItemAnimator());
        binding.TripList.setLayoutManager(layoutManager);
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());
            }
        });

        binding.addtrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             conecctionStatus(false);
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.TripList);

        setViewModel();
        return view;


    }



    void setViewModel() {
        model = ViewModelProviders.of(requireActivity()).get(HomeViewModel.class);
        binding.setModel(model);
        if (model.getTrips() != null){
            model.getTrips().observe(this, new Observer<List<Trip>>() {
                @Override
                public void onChanged(List<Trip> Trips) {
                    if (Trips != null) {

                        displayTrips(Trips);
                        binding.noupcomingrips.setVisibility(View.INVISIBLE);
                    }
                }
            });
    }else {
             binding.noupcomingrips.setVisibility(View.VISIBLE);
          }


    }

    void displayTrips(List<Trip> trips){
        mAdapter.setTripList(trips);
    }

    void conecctionStatus(Boolean state)
    {
        if(state==true){ binding.noConnection.setVisibility(View.GONE);}
        else { binding.noConnection.setVisibility(View.VISIBLE);}
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof HomeAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = mAdapter.trips.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Trip deletedTrip = mAdapter.trips.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
//            model.deleteTrip(deletedTrip);
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(binding.mainlayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedTrip, deletedIndex);
    //                model.addTrip(deletedTrip);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }


    private void filter(String text) {

        List<Trip> filterdtrips = new ArrayList<>();
        if(text.equals("")||text==null){

            if(trips!=null){

                mAdapter.setTripList(trips.getValue());
                binding.noresult.setVisibility(View.INVISIBLE);
            }
        }


        for (Trip t : mAdapter.trips) {
            if (t.getName().toLowerCase().contains(text.toLowerCase())) {

                filterdtrips.add(t);
            }

        }
        if (filterdtrips.size()<=0){
        binding.noresult.setVisibility(View.VISIBLE);
        binding.TripList.setVisibility(View.INVISIBLE);
        binding.noupcomingrips.setVisibility(View.INVISIBLE);
        }
        else{
        mAdapter.filterList(filterdtrips);
            binding.noresult.setVisibility(View.INVISIBLE);
            binding.TripList.setVisibility(View.VISIBLE);
            binding.noupcomingrips.setVisibility(View.INVISIBLE);
        }
    }


    /*omnia*/
}
