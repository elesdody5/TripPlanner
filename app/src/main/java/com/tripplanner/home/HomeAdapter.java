package com.tripplanner.home;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.databinding.TripCardBinding;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
        private List<Trip> trips;


 public class MyViewHolder extends RecyclerView.ViewHolder {

     public CardView viewBackground, viewForeground;

     private final TripCardBinding binding;

            public MyViewHolder(TripCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
            public void bind(Object obj) {
                binding.setVariable(com.tripplanner.BR.obj,obj);
                viewBackground=binding.background;
                viewForeground=binding.foreground;
                binding.executePendingBindings();
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public HomeAdapter() {

        }


        public void setTripList(List<Trip> myDataset) {
            trips = myDataset;
            notifyDataSetChanged();
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


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
         if (trips!=null)
            return trips.size();
         else return 0;
        }


        public void removeItem(int position) {
            trips.remove(position);
            // notify the item removed by position
            // to perform recycler view delete animations
            notifyItemRemoved(position);

       }

    public void DeleteTrip(int position) {



    }
        public void restoreItem(Trip trip, int position) {
        trips.add(position, trip);
        // notify item added by position
        notifyItemInserted(position);
       }


}
