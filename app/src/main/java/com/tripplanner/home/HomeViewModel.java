package com.tripplanner.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.data_layer.local_data.TripDao;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    Repository repo;
    LiveData<List<Trip>> data;
    LiveData<List<Note>> notes;
    TripDao tripDao;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repo=new Repository(application);
    }


    public LiveData<List<Trip>> getTrips() {

        return repo.getUpComingTrips();
    }

    public void getTripNotes(Trip trip)  {
        repo.getTodoNotes((int)trip.getId());

    }

    public void addTrip(){
     // repo.
    }

    public  void editTrip(){}
    public void deleteTrip(){}
    public void deleteNote(){}
    public  void EditTip(){}



}
