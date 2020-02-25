package com.tripplanner.add_trip;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.tripplanner.R;
import com.tripplanner.alarm.NotificationActivity;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.databinding.FragmentAddTripBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;


public class AddTripFragment extends Fragment {
    private AddTripViewModel tripViewModel;
    private FragmentAddTripBinding fragmentAddTripBinding;
    private NoteAdapter noteAdapter;
    public static final int ALARM_ID = 200;
    private static final String TAG = "AddTripFragment";
    private Trip trip;

    public AddTripFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAddTripBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_trip, container, false);
        View root = fragmentAddTripBinding.getRoot();
        tripViewModel = ViewModelProviders.of(getActivity()).get(AddTripViewModel.class);
        trip = new Trip();
        trip.setOnline(isNetworkConnected());
        fragmentAddTripBinding.setTrip(trip);
        fragmentAddTripBinding.setFragmet(this);
        noteAdapter = new NoteAdapter(new ArrayList<>());
        fragmentAddTripBinding.addNoteCard.noteRv.setAdapter(noteAdapter);
        setupTimeDatePicker();
        return root;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    //...
                    return !capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                }
            } else {
                // current code
                return true;
            }
        }
        return false;
    }



    private void setupTimeDatePicker() {
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker materialDatePicker = builder.build();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTS"));
        calendar.clear();
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        builder.setSelection(today);
        fragmentAddTripBinding.dateView.datePicker.setOnClickListener(view -> {

            materialDatePicker.show(getActivity().getSupportFragmentManager(), "a");
            materialDatePicker.addOnPositiveButtonClickListener(selection ->

            {
                if (selection != null) {
                    fragmentAddTripBinding.dateView.dateTv.setText(materialDatePicker.getHeaderText());
                    Date date = getDate(materialDatePicker.getHeaderText());
                    trip.setTripDate(date);
                    Log.d(TAG, "setupTimeDatePicker: " + selection.toString());
                    openTimePicker();

                }
            });
        });
    }

    private Date getDate(String headerText) {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date d = format.parse(headerText);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void openTimePicker() {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), (timePicker, selectedHour, selectedMinute) -> {
            fragmentAddTripBinding.dateView.timeTv.setText(selectedHour + ":" + selectedMinute);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, selectedHour);
            cal.set(Calendar.MINUTE, selectedMinute);
            trip.getTripDate().setTime(cal.getTime().getTime());
        }, 12, 0, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    public void showNoteNameDialog() {
        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getContext());
        alert.setTitle("Note Name");
        // Set an EditText view to get user input
        final TextInputEditText input = new TextInputEditText(getContext());
        alert.setView(input);
        alert.setPositiveButton("Confirm", (dialog, whichButton) -> {
            if (!input.getText().toString().isEmpty()) {
                Note note = new Note();
                note.setNoteName(input.getText().toString());
                noteAdapter.addNote(note);
            }
        });

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            // Canceled.
        });

        alert.show();
    }

    public void insertTip() {
        String name = fragmentAddTripBinding.tripName.getText().toString();
        boolean hasName = !name.isEmpty();
        if (!hasName)
            fragmentAddTripBinding.tripName.setError("Please enter trip name");
        String time = fragmentAddTripBinding.dateView.timeTv.getText().toString();
        boolean hasDateTime = !time.isEmpty();
        if (!hasDateTime) {
            Toast.makeText(getContext(), "Please choose trip date and time", Toast.LENGTH_SHORT).show();
        }
        if (hasDateTime && hasName) {

            tripViewModel.insertTrip(trip, noteAdapter.getNotes()).observe(getActivity(), aBoolean -> {
                Toast.makeText(getContext(),"Inserted Successfully",Toast.LENGTH_SHORT).show();
                setAlarmManger();

            });
        }



    }
    private void setAlarmManger() {

        // Set the alarm to start at 8:30 a.m.
         Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.setTime(trip.getTripDate());
        Intent notifyIntent = new Intent(getContext(), NotificationActivity.class);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (getContext(), ALARM_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    notifyPendingIntent);

        }
    }
    public void goback(View v)
    {
        Navigation.findNavController(v).popBackStack();
    }
}
