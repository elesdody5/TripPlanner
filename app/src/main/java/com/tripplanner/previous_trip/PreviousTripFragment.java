package com.tripplanner.previous_trip;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.tripplanner.R;
import com.tripplanner.data_layer.local_data.Entity.Place;
import com.tripplanner.data_layer.local_data.Entity.Trip;
import com.tripplanner.databinding.PreviousTripFragmentBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreviousTripFragment extends Fragment {

    private PreviousTripViewModel previousTripViewModel;
    private RecyclerView finishedTripRecView;
    private RecyclerView delayedTripRecView;

    private PreviousTripAdapter finishedTripAdapter;
    private PreviousTripAdapter delayedTripAdapter;

    List<Trip> delayedtripList=new ArrayList<>();
    List<Trip> finshedtripList=new ArrayList<>();

    ConstraintLayout constraintLayout;
    public static PreviousTripFragment newInstance() {
        return new PreviousTripFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
     /*   View view= inflater.inflate(R.layout.previous_trip_fragment, container, false);
        return view;/

      */
        PreviousTripFragmentBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.previous_trip_fragment, container, false);
        finishedTripRecView=binding.finishedTripRecyclerView;
        delayedTripRecView=binding.delayedTripRecyclerView;


        finishedTripAdapter=new PreviousTripAdapter(new ArrayList<>());
        delayedTripAdapter=new PreviousTripAdapter(new ArrayList<>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager delayedLayoutManager = new LinearLayoutManager(getContext());

        finishedTripRecView.setLayoutManager(mLayoutManager);
        delayedTripRecView.setLayoutManager(delayedLayoutManager);
        finishedTripRecView.setAdapter(finishedTripAdapter);
        delayedTripRecView.setAdapter(delayedTripAdapter);
        finishedTripRecView.setHasFixedSize(true);
        delayedTripRecView.setHasFixedSize(true);
        constraintLayout=binding.constraintLayout;
        View view = binding.getRoot();
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback_finished = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, new RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                if (viewHolder instanceof PreviousTripAdapter.PreviousTripViewHandler ) {
                    // get the removed item name to display it in snack bar
                    String name = finshedtripList.get(viewHolder.getAdapterPosition()).getName();

                    // backup of removed item for undo purpose
                    final Trip deletedItem = finshedtripList.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    // remove the item from recycler view
                    finishedTripAdapter.removeItem(viewHolder.getAdapterPosition());
                    //  previousTripViewModel.notify();
                    // showing snack bar with Undo option
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, name + " removed from trip!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // undo is selected, restore the deleted item
                            finishedTripAdapter.restoreItem(deletedItem, deletedIndex);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }
        });
        new ItemTouchHelper(itemTouchHelperCallback_finished).attachToRecyclerView(finishedTripRecView);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback_delayed = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, new RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                if (viewHolder instanceof PreviousTripAdapter.PreviousTripViewHandler ) {
                    // get the removed item name to display it in snack bar
                    String name = delayedtripList.get(viewHolder.getAdapterPosition()).getName();

                    // backup of removed item for undo purpose
                    final Trip deletedItem = delayedtripList.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    // remove the item from recycler view
                    delayedTripAdapter.removeItem(viewHolder.getAdapterPosition());
                    //  previousTripViewModel.notify();
                    // showing snack bar with Undo option
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, name + " removed from trip!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // undo is selected, restore the deleted item
                            delayedTripAdapter.restoreItem(deletedItem, deletedIndex);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }
        });
        new ItemTouchHelper(itemTouchHelperCallback_delayed).attachToRecyclerView(delayedTripRecView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        previousTripViewModel = ViewModelProviders.of(this).get(PreviousTripViewModel.class);
        // TODO: Use the ViewModel
        previousTripViewModel.getCancelTrip().observe(getViewLifecycleOwner(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                //   previousTripAdapter=new PreviousTripAdapter(trips);
                delayedTripAdapter.setArray(trips);
                delayedTripAdapter.notifyDataSetChanged();
                delayedtripList=trips;
            }
        });
        previousTripViewModel.getDoneTrip().observe(getViewLifecycleOwner(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                finishedTripAdapter.setArray(trips);
                finishedTripAdapter.notifyDataSetChanged();
                finshedtripList=trips;
            }
        });
    }


}
