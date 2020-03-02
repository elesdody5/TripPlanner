package com.tripplanner.add_trip;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tripplanner.data_layer.Repository;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.databinding.FragmentAddTripBinding;
import com.tripplanner.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class AddTripViewModel extends AndroidViewModel {
    private Repository repository;
    private static final String TAG = "AddTripViewModel";

    public AddTripViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        Log.d(TAG, "AddTripViewModel: ");
    }

    LiveData<Long> insertTrip(Trip trip, ArrayList<Note> notes) {
        return repository.insertTrip(trip, notes);

    }


    boolean validate(FragmentAddTripBinding fragmentAddTripBinding) {
        return ValidationUtil.tripValidation(fragmentAddTripBinding);
    }

    public List<Note> getTripNote(long tripId) {
        return repository.getTripNotes(tripId);
    }
}
