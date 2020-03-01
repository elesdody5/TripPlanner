package com.tripplanner.previous_trip.directionhelpers;

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;


public interface TaskLoadedCallback {
    void onTaskDone(List<PolylineOptions> m);
}
