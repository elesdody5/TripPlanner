package com.tripplanner.previous_trip;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;

import java.util.ArrayList;
import java.util.List;

public class DoneTripViewModel extends AndroidViewModel {

    // TODO: Implement the ViewModel
    Repository repository;

    public DoneTripViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }
    public LiveData<List<Trip>> getDoneTrip() {
        Log.d("size", "getDoneTrip: "+repository.getDoneTrips().getValue());
        return repository.getDoneTrips();
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
