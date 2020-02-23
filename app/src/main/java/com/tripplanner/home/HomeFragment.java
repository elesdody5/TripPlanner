package com.tripplanner.home;


import android.graphics.Color;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.tripplanner.R;
import com.tripplanner.Trip;
import com.tripplanner.databinding.FragmentHomeBinding;

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
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.TripList);

        setViewModel();
        return view;


    }






    void setViewModel(){
        model= ViewModelProviders.of(requireActivity()).get(HomeViewModel.class);

        binding.setModel(model);
        model.getTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> Trips) {
                HomeFragment.this.displayTrips(Trips);
                displayTrips(Trips);
            }
        });
    }
   void displayTrips(List<Trip> trips){
        mAdapter.setTripList(trips);
   }

    public void addTrip(View view){
     model.addTrip();

   }

   void deleteTrip(Trip trip){
      mAdapter.DeleteTrip(1);
      model.deleteTrip();

   }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof HomeAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = trips.getValue().get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Trip deletedTrip = trips.getValue().get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
            model.deleteTrip();
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(binding.mainlayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedTrip, deletedIndex);
                    model.addTrip();
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }




    /*omnia*/
}
