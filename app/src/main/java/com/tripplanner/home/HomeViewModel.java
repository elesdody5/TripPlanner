package com.tripplanner.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.data_layer.local_data.TripDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends AndroidViewModel {
    Repository repo;
    LiveData<List<Trip>> data;
    LiveData<List<Note>> notes;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repo = new Repository(application);
    }


    public LiveData<List<Trip>> getTrips() {

        return repo.getUpComingTrips();
    }

    public List<Note> getTripNotes(Trip trip) {

        return repo.getTripNotes(trip.getId());

    }


    public void deleteTrip(Trip trip) {
        repo.deleteTrip((int) trip.getId());
    }

    public LiveData<Long> insertTrip(Trip trip, ArrayList<Note> notes) {
    return repo.insertTrip(trip,notes);
    }
    public void updateTrip(Trip trip, Map<String, Object> tripData)
    {
        repo.updateTrip(trip,tripData);
    }
}
