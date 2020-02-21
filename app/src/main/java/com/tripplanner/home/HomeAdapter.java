package com.tripplanner.home;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


import com.tripplanner.R;
import com.tripplanner.data_layer.local_data.Entity.Trip;
import com.tripplanner.databinding.TripCardBinding;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        private List<Trip> trips;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            private final TripCardBinding binding;

            public MyViewHolder(TripCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
            public void bind(Object obj) {
                binding.setVariable(com.tripplanner.BR.obj,obj);
                binding.executePendingBindings();
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public HomeAdapter(List<Trip> myDataset) {
            trips = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
           TripCardBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.trip_card, parent, false);
            // set the view's size, margins, paddings and layout parameters
            return new MyViewHolder(binding);
        }


    // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Trip tripData = trips.get(position);
            holder.bind(tripData);


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return trips.size();
        }


}
