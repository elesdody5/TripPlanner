package com.tripplanner.previous_trip;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;
import com.tripplanner.R;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.databinding.DoneTripFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class DoneTripFragment extends Fragment {

    private DoneTripViewModel mViewModel;
    private RecyclerView finishedTripRecView;

    ConstraintLayout frameLayout;
    DoneTripFragmentBinding binding;
    private TripAdapter finishedTripAdapter;
    List<Note> notes=new ArrayList<>();
    private static final String TAG = "DoneTripFragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         binding = DataBindingUtil.inflate(
                inflater, R.layout.done_trip_fragment, container, false);
        View view = binding.getRoot();
        mViewModel = ViewModelProviders.of(getActivity()).get(DoneTripViewModel.class);

        mViewModel.getDoneTrip().observe(getViewLifecycleOwner(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                finishedTripAdapter.setArray(trips);
                Log.i(TAG, "onChanged: "+ finishedTripAdapter.tripArray().size());
               // finshedtripList=trips;
              //  Log.i(TAG, "onChanged: "+trips);
                if(finishedTripAdapter.tripArray().size()==0)
                {
                    binding.emptyStateId.setVisibility(View.VISIBLE);

                }
                else
                {
                    binding.emptyStateId.setVisibility(View.INVISIBLE);


                }
            }
        });
            finishedTripRecView = binding.finishedTripRecyclerView;
            frameLayout = binding.mainlayout;
       /* if(finishedTripAdapter.tripArray().size()==0)
        {
            binding.emptyStateId.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.emptyStateId.setVisibility(View.INVISIBLE);

        }*/
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            finishedTripRecView.setLayoutManager(mLayoutManager);
           finishedTripAdapter = new TripAdapter(new ArrayList<>());

          finishedTripRecView.setAdapter(finishedTripAdapter);

            ItemTouchHelper.SimpleCallback itemTouchHelperCallback_finished = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, new RecyclerItemTouchHelper.RecyclerItemTouchHelperListener() {
                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
                    if (viewHolder instanceof TripAdapter.PreviousTripViewHandler) {
                        String name = finishedTripAdapter.tripArray().get(viewHolder.getAdapterPosition()).getName();

                        final Trip deletedItem = finishedTripAdapter.tripArray().get(viewHolder.getAdapterPosition());
                        final int deletedIndex = viewHolder.getAdapterPosition();
                        mViewModel.deleteTrip((int) deletedItem.getId());
                        notes=mViewModel.getTripNotes((int)deletedItem.getId()).getValue();

                        finishedTripAdapter.removeItem(viewHolder.getAdapterPosition());
                        Snackbar snackbar = Snackbar
                                .make(frameLayout, name + " removed from trip!", Snackbar.LENGTH_LONG);
                        snackbar.setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finishedTripAdapter.restoreItem(deletedItem, deletedIndex);
                                mViewModel.insertTrip(deletedItem, (ArrayList<Note>) notes);

                            }
                        });
                        snackbar.setActionTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                }
            });
            new ItemTouchHelper(itemTouchHelperCallback_finished).attachToRecyclerView(finishedTripRecView);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("ssss", "onCreateView: "+finishedTripAdapter.tripArray().size());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel


    }


}
