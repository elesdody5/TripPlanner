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
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;
import com.tripplanner.R;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.databinding.CancledTripFragmentBinding;
import com.tripplanner.databinding.DoneTripFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class CancledTripFragment extends Fragment {

    private CancledTripViewModel mViewModel;
    private RecyclerView cancelTripRecView;
    private TripAdapter cancelTripAdapter;
    List<Trip> canceltripList=new ArrayList<>();
    FrameLayout frameLayout;
    ConstraintLayout constraintLayout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        CancledTripFragmentBinding binding= DataBindingUtil.inflate(
                inflater, R.layout.cancled_trip_fragment, container, false);
        cancelTripRecView = binding.delayedTripRecyclerView;
        constraintLayout = binding.mainlayout;
        cancelTripAdapter = new TripAdapter(new ArrayList<>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        cancelTripRecView.setLayoutManager(mLayoutManager);
        cancelTripRecView.setAdapter(cancelTripAdapter);
        View view = binding.getRoot();
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback_delayed = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, new RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                if (viewHolder instanceof TripAdapter.PreviousTripViewHandler ) {
                    String name = canceltripList.get(viewHolder.getAdapterPosition()).getName();

                    final Trip deletedItem = canceltripList.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    cancelTripAdapter.removeItem(viewHolder.getAdapterPosition());
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, name + " removed from trip!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // undo is selected, restore the deleted item
                            cancelTripAdapter.restoreItem(deletedItem, deletedIndex);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }
        });
        new ItemTouchHelper(itemTouchHelperCallback_delayed).attachToRecyclerView(cancelTripRecView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CancledTripViewModel.class);
        // TODO: Use the ViewModel
       mViewModel.getCancelTrip().observe(getViewLifecycleOwner(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                //   previousTripAdapter=new PreviousTripAdapter(trips);
                cancelTripAdapter.setArray(trips);
                cancelTripAdapter.notifyDataSetChanged();
                canceltripList=trips;
            }
        });

    }

}
