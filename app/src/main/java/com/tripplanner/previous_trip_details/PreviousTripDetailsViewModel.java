package com.tripplanner.previous_trip_details;

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

public class PreviousTripDetailsViewModel extends AndroidViewModel {

    // TODO: Implement the ViewModel
    Repository repository;

    public PreviousTripDetailsViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public List<Note> getTripNotes(int id)
    {
        return repository.getTripNotes(id);
    }


}
