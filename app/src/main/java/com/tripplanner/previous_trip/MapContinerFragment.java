package com.tripplanner.previous_trip;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tripplanner.R;
import com.tripplanner.data_layer.local_data.entity.Place;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.previous_trip.directionhelpers.FetchURL;
import com.tripplanner.previous_trip.directionhelpers.TaskLoadedCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapContinerFragment extends Fragment implements OnMapReadyCallback, TaskLoadedCallback {

    private MapContinerViewModel mViewModel;
    private GoogleMap mMap;
    private Place place1,place2,place3,place4;
    List<Trip> finshedtripList;
    List<Trip> canceledtripList=new ArrayList<>();
    private Polyline currentPolyline;
    MapFragment mapFragment;
    List<PolylineOptions>valuesLine=new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.map_continer_fragment, container, false);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map2);
        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.mapFragmentContainer, mapFragment, "mapFragment");
            ft.commit();
            fm.executePendingTransactions();
        }
        place1=new Place("12",27.658143, 85.3199503);
        place2=new Place("12",27.667491, 85.3208583);
        place3=new Place("12",27.658143, 85.3199503);
        place4=new Place("12",24.667442, 85.3208453);
        finshedtripList=new ArrayList<>();
        Trip trip=new Trip();
        trip.setStartPoint(place1);
        trip.setEndPoint(place2);
        Trip trip2=new Trip();
        trip2.setStartPoint(place3);
        trip2.setEndPoint(place4);
        finshedtripList.add(trip);
        finshedtripList.add(trip2);

        mapFragment.getMapAsync(this);
        for (int i =0;i< finshedtripList.size();i++) {
            new FetchURL(MapContinerFragment.this).execute(getUrl(finshedtripList.get(i).getStartPoint(), finshedtripList.get(i).getEndPoint(), "driving"), "driving");

        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MapContinerViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getDoneTrip().observe(getViewLifecycleOwner(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                finshedtripList=trips;
                //   Log.d("previos", "onChanged: "+trips.size());
            }
        });
        mViewModel.getCancelTrip().observe(getViewLifecycleOwner(), new Observer<List<Trip>>() {
            @Override
            public void onChanged(List<Trip> trips) {
                canceledtripList=trips;
                //   Log.d("previos", "onChanged: "+trips.size());
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        //   mMap.addMarker(place1.getLatitude());
        //   mMap.addMarker(place2);

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(place1.getLat(), place2.getLng()))
                .title("Marker"));

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
        //  if (currentPolyline != null)
        //      currentPolyline.remove();
        // currentPolyline = mMap.addPolyline( values.get(1));
        Random rnd = new Random();
        valuesLine.add(values.get(0));
        for(int i=0;i<valuesLine.size();i++)
        {
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            currentPolyline = mMap.addPolyline( valuesLine .get(i).color(color));
            Float zoom = mMap.getCameraPosition().zoom;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place1.getLat(), place1.getLng()),zoom));        }

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
