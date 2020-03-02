package com.tripplanner.data_layer.local_data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTrip(Trip trip);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTrip(List<Trip> listTrips);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertNote(List<Note> listNote);

    @Update
    int updateTrip(Trip trip);

    @Update
    int updateNote(Note note);

    @Query("DELETE FROM trip_table WHERE id = :id")
    void deleteTrip(long id);

    @Query("DELETE FROM note_table WHERE tripId = :tripid")
    void deleteTripNote(long tripid);

    @Delete
    int deleteTripNote(Note note);

    @Query("Select * From trip_table where  userId=:userid AND tripStatus=:status")
    LiveData<List<Trip>> getTrips(String userid, long status);
    @Query("Select * From trip_table where  id=:id")
    Trip getTripbyId(long id);
    @Query("Select * From note_table where tripId=:tripId")
    List<Note> getNotes(long tripId);


}



