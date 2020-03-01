package com.tripplanner.alarm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;

import java.util.ArrayList;
import java.util.List;

public class AlarmViewModel extends AndroidViewModel {
    Repository repo;


    public AlarmViewModel(@NonNull Application application) {
        super(application);
        repo=new Repository(application);
    }

    Trip getTrip(long tripId)
    {
        return repo.getTripById(tripId);
    }
    List<Note> getNotes(long tripId)
    {
        return repo.getTripNotes(tripId);

    }

}
