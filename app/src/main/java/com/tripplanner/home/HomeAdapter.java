package com.tripplanner.home;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.tripplanner.add_trip.AddTripFragmentArgs;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.databinding.TripCardBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    public List<Trip> trips = new ArrayList<>();

    public List<Trip> getTrips() {
        return trips;
    }

    interface  StartTrip{
        void startTrip(long tripId);
    }
    StartTrip startTrip ;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView viewBackground, viewForeground;

        private final TripCardBinding binding;

        public MyViewHolder(TripCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object obj) {
            binding.setVariable(com.tripplanner.BR.obj, obj);
            viewBackground = binding.background;
            viewForeground = binding.foreground;
            binding.executePendingBindings();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomeAdapter(StartTrip startTrip) {
    this.startTrip = startTrip;

    }


    public void setTripList(List<Trip> myDataset) {
        trips = myDataset;
        notifyDataSetChanged();
    }


    public void addTripList(Trip trip) {
        Log.i("Omnia", "adding");
        trips.add(trip);
        notifyItemInserted(trips.size() - 1);
    }


    // Create new views (invoked by the layout manager)



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TripCardBinding binding = TripCardBinding.inflate(layoutInflater, parent, false);
        return new MyViewHolder(binding);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Trip tripData = trips.get(position);
        holder.bind(tripData);
        holder.binding.tripCard.setOnClickListener(view -> {
                    HomeFragmentDirections.ActionHomeFragmentToAddTripFragment action = HomeFragmentDirections.actionHomeFragmentToAddTripFragment();
                    action.setTrip(trips.get(position));
                    Navigation.findNavController(view).navigate(action);
                }
        );
        holder.binding.startTrip.setOnClickListener(view -> {
            startTrip.startTrip(trips.get(position).getId());
        });
        holder.binding.Date.setText(updateLabel(tripData.getTripDate()));
        holder.binding.Time.setText(roundtimeFormat(tripData.getTripDate()));


}
    private String roundtimeFormat(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("K:mm a");
        String formattedTime = sdf.format(date);
        return formattedTime;
    }
    private String updateLabel(Date date) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf.format(date);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return trips.size();

    }


    public void removeItem(int position) {
        trips.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        notifyItemRemoved(position);

    }


    public void restoreItem(Trip trip, int position) {
        trips.add(position, trip);
        // notify item added by position
        notifyItemInserted(position);
    }


    public void filterList(List<Trip> filterdTrips) {
        this.trips = filterdTrips;
        notifyDataSetChanged();
    }


}


