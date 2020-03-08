package com.tripplanner.home;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.tripplanner.Constants;
import com.tripplanner.R;
import com.tripplanner.alarm.FloatingViewService;
import com.tripplanner.alarm.NotificationActivity;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Place;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.ALARM_SERVICE;
import static com.tripplanner.alarm.NotificationActivity.CODE_DRAW_OVER_OTHER_APP_PERMISSION;

/**
 * A simple {@link Fragment} subclass.
 */
// TODO create view model
// TODO create recyclerview done
// TODO create home layout done

public class HomeFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener ,HomeAdapter.StartTrip {

    HomeViewModel model;
   // LiveData<List<Trip>> trips;
    private HomeAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FragmentHomeBinding binding;
    boolean isConnected;
    List<Trip> upcomingTrips;

    public HomeFragment() {

    }

    private static final String TAG = "HomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*omnia*/
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        View view = binding.getRoot();

        layoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new HomeAdapter(this);
        binding.TripList.setHasFixedSize(true);
        binding.TripList.setAdapter(mAdapter);
        binding.TripList.setItemAnimator(new DefaultItemAnimator());
        binding.TripList.setLayoutManager(layoutManager);

        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged: ");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d(TAG, "afterTextChanged: ");
                if(!TextUtils.isEmpty(editable)) {
                    search(editable.toString());
                }
                else {
                    binding.TripList.setVisibility(View.VISIBLE);
                    binding.noresult.setVisibility(View.INVISIBLE);
                    setViewModel();
                }
            }
        });

        binding.addtrip.setOnClickListener(v ->

        {
            if (isConnected)
                Navigation.findNavController(view).navigate(R.id.addTripFragment);
            else
                Toast.makeText(getActivity(), "Cannot add in Offline mode", Toast.LENGTH_LONG).show();
        });


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.TripList);

        setViewModel();
        conecctionStatus(isOnline());
        return view;


    }

    private void search(String tripName) {
        ArrayList<Trip> foundTrips = new ArrayList<>();
        for (Trip trip : mAdapter.getTrips())
        {

            if(trip.getName().contains(tripName))
            {
                foundTrips.add(trip);
            }
        }
        if(foundTrips.isEmpty())
        {
            binding.noresult.setVisibility(View.VISIBLE);
            binding.TripList.setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.noresult.setVisibility(View.INVISIBLE);
            binding.TripList.setVisibility(View.VISIBLE);
            mAdapter.setTripList(foundTrips);
        }
    }


    public  boolean isOnline() {


        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    void setViewModel() {
        model = ViewModelProviders.of(requireActivity()).get(HomeViewModel.class);
        binding.setModel(model);

        model.getTrips().observe(getViewLifecycleOwner(), this::displayTrips);


    }

    void displayTrips(List<Trip> trips) {
        if (!trips.isEmpty()) {
            Log.d(TAG, "displayTrips: "+trips.size());
            mAdapter.setTripList(trips);
            setAlarmManger(trips);
        }else {
            binding.noupcomingrips.setVisibility(View.VISIBLE);
        }
    }

    void conecctionStatus(Boolean state) {
        isConnected = state;
        if (state) {
            binding.noConnection.setVisibility(View.GONE);
        } else {
            binding.noConnection.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof HomeAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = mAdapter.trips.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Trip deletedTrip = mAdapter.trips.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            List<Note> notes  = model.getTripNotes(deletedTrip);
            model.deleteTrip(deletedTrip);
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
                    model.insertTrip(deletedTrip, (ArrayList<Note>) notes);
                    //                model.addTrip(deletedTrip);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }


  /*  private void filter(String text) {

        List<Trip> filterdtrips = new ArrayList<>();

        if (!text.equals("")) {


            mAdapter.setTripList(trips.getValue());
            binding.noresult.setVisibility(View.INVISIBLE);

        } else {
            for (Trip t : mAdapter.trips) {
                if (t.getName().toLowerCase().contains(text.toLowerCase())) {

                    filterdtrips.add(t);
                }

            }
            if (filterdtrips.size() <= 0) {
                binding.noresult.setVisibility(View.VISIBLE);
                binding.TripList.setVisibility(View.INVISIBLE);
                binding.noupcomingrips.setVisibility(View.INVISIBLE);
            } else {
                mAdapter.filterList(filterdtrips);
                binding.noresult.setVisibility(View.INVISIBLE);
                binding.TripList.setVisibility(View.VISIBLE);
                binding.noupcomingrips.setVisibility(View.INVISIBLE);
            }
        }
    }
*/
    @Override
    public void startTrip(Trip trip) {
       Map<String,Object> hm = new HashMap<>();

        hm.put("tripStatus", Constants.STATUS_DONE);
        model.updateTrip(trip, hm);
        setPermation(trip);
        cancleAlarm((int)trip.getId());
        mAdapter.removeItem(trip.getId());
    }

    public void setPermation(Trip trip) {
        Log.d("ddd", "setPermation: dddd");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getActivity())) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getContext().getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            Log.d("ffff", "setPermation: fffff");
            Intent intent = new Intent(getActivity(), FloatingViewService.class);
            List<Note> notes  = model.getTripNotes(trip);
            intent.putParcelableArrayListExtra("notes", (ArrayList<? extends Parcelable>) notes);
            getContext().startService(intent);
            openGoogleMapDierction(trip.getEndPoint().getLat(), trip.getEndPoint().getLng());


        }
    }
    private void openGoogleMapDierction(double latatute, double longatute) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + latatute + "," + longatute + ""));
        startActivity(intent);
    }
    public  void cancleAlarm(int tripId)
    {
        Intent notifyIntent = new Intent(getContext(), NotificationActivity.TripAlarmReciver.class);
        notifyIntent.putExtra(Constants.TRIPS, tripId);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (getContext(), tripId, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        alarmManager.cancel(notifyPendingIntent);

    }

    /*omnia*/
    private void setAlarmManger(List<Trip> trips) {
        Log.d(TAG, "setAlarmManger: "+trips.size());
        for(int i=0;i<trips.size();i++){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.setTime(trips.get(i).getTripDate());
            Intent notifyIntent = new Intent(getContext(), NotificationActivity.TripAlarmReciver.class);
            notifyIntent.putExtra(Constants.TRIPS,(int)trips.get(i).getId());
            final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                    (getContext(), (int)trips.get(i).getId(), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            final AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

            if (alarmManager != null) {
                Log.d(TAG, "setAlarmManger: " + alarmManager);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        notifyPendingIntent);

            }

        }

    }

}
