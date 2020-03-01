package com.tripplanner.add_trip;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.tripplanner.Constants;
import com.tripplanner.R;
import com.tripplanner.add_trip.place.PlaceAutoSuggestAdapter;
import com.tripplanner.alarm.NotificationActivity;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.databinding.FragmentAddTripBinding;
import com.tripplanner.home.HomeFragment;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;


public class AddTripFragment extends Fragment {
    private AddTripViewModel tripViewModel;
    private FragmentAddTripBinding fragmentAddTripBinding;
    private NoteAdapter noteAdapter;
    public static final int ALARM_ID = 200;
    private static final String TAG = "AddTripFragment";
    private Trip trip;
    PlaceAutoSuggestAdapter Fromadapter;
    PlaceAutoSuggestAdapter toAdapter;
    private DatePickerDialog.OnDateSetListener date;
    private Calendar myCalendar;



    long time;

    public AddTripFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentAddTripBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_trip, container, false);
        View root = fragmentAddTripBinding.getRoot();
        tripViewModel = new ViewModelProvider(getActivity()).get(AddTripViewModel.class);
        Fromadapter = new PlaceAutoSuggestAdapter(getContext(), R.layout.pop_up_item);
        toAdapter = new PlaceAutoSuggestAdapter(getContext(), R.layout.pop_up_item);
        fragmentAddTripBinding.placeView.fromEt.setAdapter(Fromadapter);
        fragmentAddTripBinding.placeView.toEt.setAdapter(toAdapter);
        fragmentAddTripBinding.placeView.fromEt.setOnItemClickListener((adapterView, view, i, l) -> trip.setStartPoint(Fromadapter.getPlace(i)));
        fragmentAddTripBinding.placeView.toEt.setOnItemClickListener((adapterView, view, i, l) -> trip.setEndPoint(toAdapter.getPlace(i)));
        trip = AddTripFragmentArgs.fromBundle(getArguments()).getTrip();
        setupTimeDatePicker();
        fragmentAddTripBinding.setFragmet(this);
        noteAdapter = new NoteAdapter(new ArrayList<>());
        fragmentAddTripBinding.addNoteCard.noteRv.setAdapter(noteAdapter);
        if (trip == null) {
            trip = new Trip();
        }
        else
        {
            time = trip.getTripDate().getTime();
            timeFormat(trip.getTripDate());
            myCalendar.setTime( trip.getTripDate());
            updateLabel();
            List<Note> noteList =tripViewModel.getTripNote(trip.getId());
            noteAdapter.addList(new ArrayList<>(noteList));
        }
        trip.setOnline(isNetworkConnected());
        fragmentAddTripBinding.setTrip(trip);
        return root;
    }
    private void timeFormat(Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("K:mm a");
        String formattedTime = sdf.format(date);
        fragmentAddTripBinding.dateView.time.setText(formattedTime);
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
        myCalendar = Calendar.getInstance();

        date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        fragmentAddTripBinding.dateView.datePicker.setOnClickListener(view ->
        {
            myCalendar.get(Calendar.YEAR);
            new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        } );
    }

    // set day and date in date text view
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date date = myCalendar.getTime();
        fragmentAddTripBinding.dateView.date.setText(sdf.format(date));
        trip.setTripDate(date);
    }

    public void openTimePicker() {
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), (timePicker, selectedHour, selectedMinute) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, selectedHour);
            cal.set(Calendar.MINUTE, selectedMinute);
            time = cal.getTime().getTime();
            timeFormat(cal.getTime());
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

    public void insertTip(View view) {
        if (tripViewModel.validate(fragmentAddTripBinding)) {
            Log.d(TAG, "insertTip: " + trip);
            trip.getTripDate().setTime(time);
            tripViewModel.insertTrip(trip, noteAdapter.getNotes()).observe(getActivity(), aBoolean -> {
                Toast.makeText(getContext(), "Inserted Successfully", Toast.LENGTH_SHORT).show();
                setAlarmManger();
                goback(view);
            });
        }
    }

    private void setAlarmManger() {

        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTime(trip.getTripDate());
        Intent notifyIntent = new Intent(getContext(), NotificationActivity.TripAlarmReciver.class);
        notifyIntent.putExtra(Constants.TRIPS, trip.getId());
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (getContext(), ALARM_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            Log.d(TAG, "setAlarmManger: "+alarmManager);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    notifyPendingIntent);

        }
    }

    public void goback(View v) {
        Navigation.findNavController(v).popBackStack();
    }
}
