package com.tripplanner.previous_trip;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
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
    private Polyline currentPolyline;
    List<PolylineOptions>valuesLine=new ArrayList<>();
    RelativeLayout noconn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.map_continer_fragment, container, false);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map2);
        noconn=view.findViewById(R.id.NoConnection1);
        if(isOnline())
        {
            noconn.setVisibility(View.INVISIBLE);
        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.mapFragmentContainer, mapFragment, "mapFragment");
            ft.commit();
            fm.executePendingTransactions();
        }

        mapFragment.getMapAsync(this); }
        else {
            noconn.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MapContinerViewModel.class);
        // TODO: Use the ViewModel
        if(isOnline()) {
            noconn.setVisibility(View.INVISIBLE);

            mViewModel.getDoneTrip().observe(getViewLifecycleOwner(), new Observer<List<Trip>>() {
                @Override
                public void onChanged(List<Trip> trips) {
                    for (int i = 0; i < trips.size(); i++) {
                        new FetchURL(MapContinerFragment.this).execute(getUrl(trips.get(i).getStartPoint(), trips.get(i).getEndPoint(), "driving"), "driving");
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(trips.get(i).getStartPoint().getLat(), trips.get(i).getStartPoint().getLng()))
                                .title(trips.get(i).getStartPoint().getName()));
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(trips.get(i).getEndPoint().getLat(), trips.get(i).getEndPoint().getLng()))
                                .title(trips.get(i).getEndPoint().getName()));
                    }
                    if (trips.size() != 0) {
                        float zoomLevel = (float) 10.0;
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(trips.get(0).getStartPoint().getLat(), trips.get(0).getStartPoint().getLng()), zoomLevel));
                    }
                }
            });
        }
        else {
            noconn.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(isOnline()) {
            mMap = googleMap;
        }



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
            for (int i = 0; i < valuesLine.size(); i++) {
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                currentPolyline = mMap.addPolyline(valuesLine.get(i).color(color));
            }


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
