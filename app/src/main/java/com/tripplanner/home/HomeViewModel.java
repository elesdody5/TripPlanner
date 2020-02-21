package com.tripplanner.home;

import android.graphics.Movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.Entity.Note;
import com.tripplanner.data_layer.local_data.Entity.Trip;
import com.tripplanner.data_layer.local_data.TripDao;
import com.tripplanner.data_layer.remote.Firebase;

import java.util.List;

public class HomeViewModel extends ViewModel {
    Repository repo;
    LiveData<List<Trip>> data;
    TripDao tripDao;
    public HomeViewModel(Firebase fireBase) {
        repo=new Repository(tripDao,fireBase);
    }

    public LiveData<List<Trip>> getTrips(){
        try {
            return repo.getUpComingTrips();
        } catch (Exception e) {
            e.printStackTrace();
        }
         return null;}

    public void getTripNotes(Trip trip) throws Exception {
        repo.getTodoNotes(trip.getUserId());
    }

    public void addTrip(){
      //  repo.
    }

    public void addNote(Note note){
        repo.insertNote(note);
    }

}
