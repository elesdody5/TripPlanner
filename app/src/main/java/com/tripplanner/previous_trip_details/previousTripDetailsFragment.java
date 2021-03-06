package com.tripplanner.previous_trip_details;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tripplanner.Constants;
import com.tripplanner.R;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Place;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.previous_trip.MapOfAllTripsFragment;
import com.tripplanner.previous_trip.MapOfAllTripsViewModel;
import com.tripplanner.previous_trip.directionhelpers.FetchURL;
import com.tripplanner.previous_trip.directionhelpers.TaskLoadedCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class previousTripDetailsFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback {

    private PreviousTripDetailsViewModel mViewModel;
    private GoogleMap mMap;
    private Polyline currentPolyline;
    MapFragment mapFragment;
    RecyclerView noteRecycler;
    NoteAdapter noteAdapter;
    TextView from,to,date,time;
    Trip trip;
    RelativeLayout emptystate;
   List <Note>note=new ArrayList<>();
    public static previousTripDetailsFragment newInstance() {
        return new previousTripDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.previous_trip_details_fragment, container, false);
        emptystate=view.findViewById(R.id.emptyState);
        Bundle args = getArguments();
        String tripJsonString = (String) args.get(Constants.KEY_TRIP);
         trip= GsonUtils.getGsonParser().fromJson(tripJsonString, Trip.class);
      Toolbar toolbar= view.findViewById(R.id.toolbar);
      toolbar.setTitle(trip.getName());
      from=view.findViewById(R.id.from);
        to=view.findViewById(R.id.to);
        date=view.findViewById(R.id.date);
        time=view.findViewById(R.id.time);
        from.setText("From: "+trip.getStartPoint().getName());
        to.setText("To: "+trip.getEndPoint().getName());
        date.setText("Date: "+getDate(trip.getTripDate()));
        time.setText("Time: "+getTime(trip.getTripDate()));
        mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        if(isOnline()) {
            emptystate.setVisibility(View.INVISIBLE);
            mapFragment.getMapAsync(this);
            new FetchURL(previousTripDetailsFragment.this).execute(getUrl(trip.getStartPoint(), trip.getEndPoint(), "driving"), "driving");
            noteRecycler = view.findViewById(R.id.prenote_rv);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            noteAdapter = new NoteAdapter(new ArrayList<>());
            mViewModel = ViewModelProviders.of(this).get(PreviousTripDetailsViewModel.class);
            mViewModel.getTripNotes((int) trip.getId()).observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
                @Override
                public void onChanged(List<Note> notes) {
                    noteAdapter.setNoteList(notes);
                    noteAdapter.notifyDataSetChanged();
                }
            });
            // TODO: Use the ViewModel
            //  Log.i("gg", "onCreateView: "+mViewModel.getTripNotes((int) trip.getId()).get(0).isChecked());
            //   Log.i("gg", "onCreateView: "+mViewModel.getTripNotes((int) trip.getId()).get(1).isChecked());
            noteRecycler.setAdapter(noteAdapter);
            noteRecycler.setLayoutManager(mLayoutManager);
        }
        else
        {
            emptystate.setVisibility(View.VISIBLE);
        }
        return view;
    }
    public String getDate(Date date)
    {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf.format(date).toString();
    }
    public String getTime(Date date)
    {
        String time = new SimpleDateFormat("hh:mm", Locale.getDefault()).format(date);
        return  time;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(trip.getStartPoint().getLat(), trip.getStartPoint().getLng()))
                .title(trip.getStartPoint().getName()));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(trip.getEndPoint().getLat(), trip.getEndPoint().getLng()))
                .title(trip.getEndPoint().getName()));

        float zoomLevel = (float) 10.0;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(trip.getStartPoint().getLat(),trip.getStartPoint().getLng()), zoomLevel));
    }
    private String getUrl(Place origin, Place dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.getLat() + "," + origin.getLng();
        // Destination of route
        String str_dest = "destination=" + dest.getLat() + "," + dest.getLng();
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }
    @Override
    public void onTaskDone(List<PolylineOptions>values) {
        Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            currentPolyline = mMap.addPolyline( values.get(0).color(color));
            Float zoom = mMap.getCameraPosition().zoom;
        //    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place1.getLatitude(), place1.getLongitude()),zoom));
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();

        android.app.Fragment fragment = getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        if (null != fragment) {
            android.app.FragmentTransaction ft = getActivity()
                    .getFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
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


}
