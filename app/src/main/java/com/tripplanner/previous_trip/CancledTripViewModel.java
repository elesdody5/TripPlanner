package com.tripplanner.previous_trip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;

import java.util.ArrayList;
import java.util.List;

public class CancledTripViewModel extends AndroidViewModel {
Repository repository;
    public CancledTripViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);

    }
    public List<Note> getTripNotes(long id)
    {
        return repository.getTripNotes(id);
    }

    public LiveData<List<Trip>> getCancelTrip() {
        return repository.getCancelTrips();
    }
    public void deleteTrip(int id)
    {
        repository.deleteTrip(id);
    }
    public void insertTrip(Trip trip, ArrayList<Note> notes)
    {
        repository.insertTrip(trip,notes);
    }
}
