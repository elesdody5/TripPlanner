package com.tripplanner.previous_trip_details;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class previousTripDetailsFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback {

    private PreviousTripDetailsViewModel mViewModel;
    private Place place1,place2,place3,place4;
    private GoogleMap mMap;
    private Polyline currentPolyline;
    MapFragment mapFragment;
    RecyclerView noteRecycler;
    NoteAdapter noteAdapter;
   List <Note>note=new ArrayList<>();
    public static previousTripDetailsFragment newInstance() {
        return new previousTripDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.previous_trip_details_fragment, container, false);
        place1=new Place("12",27.658143, 85.3199503);
        place2=new Place("12",27.667491, 85.3208583);
        Bundle args = getArguments();
        String tripJsonString = (String) args.get(Constants.KEY_TRIP);
        Trip trip= GsonUtils.getGsonParser().fromJson(tripJsonString, Trip.class);
      Toolbar toolbar= view.findViewById(R.id.toolbar);
      toolbar.setTitle(trip.getName());
        mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        new FetchURL(previousTripDetailsFragment.this).execute(getUrl(place1, place2, "driving"), "driving");
        noteRecycler=view.findViewById(R.id.prenote_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        noteAdapter =new NoteAdapter(note);
        noteRecycler.setAdapter(noteAdapter);
        noteRecycler.setLayoutManager(mLayoutManager);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PreviousTripDetailsViewModel.class);
        // TODO: Use the ViewModel
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        //   mMap.addMarker(place1.getLatitude());
        //   mMap.addMarker(place2);

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(place1.getLatitude(), place2.getLongitude()))
                .title("Marker"));

    }
    private String getUrl(Place origin, Place dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.getLatitude() + "," + origin.getLongitude();
        // Destination of route
        String str_dest = "destination=" + dest.getLatitude() + "," + dest.getLongitude();
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
                .findFragmentById(R.id.map2);
        if (null != fragment) {
            android.app.FragmentTransaction ft = getActivity()
                    .getFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
        }
    }


}
