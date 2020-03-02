package com.tripplanner.alarm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;

import java.util.List;
import java.util.Map;

public class AlarmViewModel extends AndroidViewModel {
    Repository repo;


    public AlarmViewModel(@NonNull Application application) {
        super(application);
        repo=new Repository(application);
    }

    public Trip getTrip(long tripId)
    {
        return repo.getTripById(tripId);

    }
    public List<Note>getNotes(long tripId)
    {
        return repo.getTripNotes(tripId);

    }
    public void updateTrip(Trip trip, Map<String, Object> tripData)
    {
        repo.updateTrip(trip,tripData);
    }
    public void updateNote(Note note, Map<String, Object> notedata)
    {
        repo.updateNote(note,notedata);
    }
}
