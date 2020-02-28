package com.tripplanner.alarm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;

import java.util.ArrayList;

public class AlarmViewModel extends AndroidViewModel {
    Repository repo;


    public AlarmViewModel(@NonNull Application application) {
        super(application);
        repo=new Repository(application);
    }

    public Trip getTrip(int tripId)
    {
        return repo.getTripDummy(tripId);

    }
    public ArrayList<Note> getNotes(int tripId)
    {
        return repo.getNotesDummy(tripId);

    }

}
