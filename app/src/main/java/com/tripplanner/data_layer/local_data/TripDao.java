package com.tripplanner.data_layer.local_data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tripplanner.data_layer.local_data.Entity.Trip;

import java.util.List;

@Dao
public interface TripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrip(Trip trip);

    @Update
    void updateTrip(Trip trip);

    @Query("DELETE FROM trip_table WHERE id = :id")
    void deleteTrip(String id);

    @Query("Select * From trip_table where userId=:userid ")
    LiveData<List<Trip>> getAllTrips(String userid);

    @Query("Select * From trip_table where tripStatus=:status")
    LiveData<List<Trip>> getTrips(int status);



}



