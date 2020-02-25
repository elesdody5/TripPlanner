package com.tripplanner.previous_trip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.Entity.Trip;

import java.util.List;

public class PreviousTripViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    Repository repository;

    public PreviousTripViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }
    public LiveData<List<Trip>> getDoneTrip() {

        return repository.getDoneTrips();
    }
    public LiveData<List<Trip>> getCancelTrip() {
        return repository.getCancelTrips();
    }
}
