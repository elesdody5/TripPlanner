package com.tripplanner.data_layer.local_data;

import android.app.Application;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.tripplanner.data_layer.local_data.Entity.Note;
import com.tripplanner.data_layer.local_data.Entity.Trip;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Trip.class, Note.class}, version = 1, exportSchema = false)
@TypeConverters(DateTimeConverter.class)
public abstract class Room extends RoomDatabase {
    private static final String DB_NAME = "trip_database";
    private static Room INSTANCE;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(5);
    public abstract TripDao tripDao();

    public static Room getInstance(Application application) {
        if (Room.INSTANCE == null) {
            INSTANCE =
                    androidx.room.Room.databaseBuilder(application, Room.class, DB_NAME)
                            .allowMainThreadQueries()
                            .build();
        }

        return INSTANCE;


    }

}
