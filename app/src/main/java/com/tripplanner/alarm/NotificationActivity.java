package com.tripplanner.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tripplanner.Constants;
import com.tripplanner.data_layer.local_data.entity.Note;
import com.tripplanner.data_layer.local_data.entity.Trip;
import com.tripplanner.util.NotificationUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    AlarmViewModel alarmViewModel;
    Trip trip;
    List<Note> tripNotes;
    private static final String TAG = "NotificationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        Log.d(TAG, "onCreate: "+getIntent().getIntExtra(Constants.TRIPS,0));
        trip = alarmViewModel.getTrip(getIntent().getIntExtra(Constants.TRIPS,0));
        tripNotes = alarmViewModel.getNotes(trip.getId());

        displayAlert(this);
    }

    private void displayAlert(Context context) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setMessage("Are you sure you want to exit?").setCancelable(false)
                .setPositiveButton("Start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setPermation();

                    }
                }).setNegativeButton("Cancel", (dialogInterface, i) -> {


                }).setNeutralButton("Snoze", (dialogInterface, i) -> {
                NotificationUtil.makeStatusNotification(trip.getName(),"Starting strip ",context);
                });

        builder.show();
    }

    private void openGoogleMapDierction(double latatute, double longatute) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + latatute + "," + longatute + ""));
        startActivity(intent);
    }


    public void setPermation() {
        Log.d("ddd", "setPermation: dddd");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            Log.d("ffff", "setPermation: fffff");
            Intent intent = new Intent(NotificationActivity.this, FloatingViewService.class);
            intent.putParcelableArrayListExtra("notes", (ArrayList<? extends Parcelable>) tripNotes);
            startService(intent);
            openGoogleMapDierction(trip.getEndPoint().getLat(), trip.getEndPoint().getLng());


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                Log.d("result", "onActivityResult: ");
                Intent intent = new Intent(NotificationActivity.this, FloatingViewService.class);
                intent.putParcelableArrayListExtra("notes", (ArrayList<? extends Parcelable>) tripNotes);
                startService(intent);
                openGoogleMapDierction(trip.getEndPoint().getLat(), trip.getEndPoint().getLng());
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public static class TripAlarmReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent i = new Intent(context, NotificationActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(Constants.TRIPS,intent.getIntExtra(Constants.TRIPS,0));
            context.startActivity(i);
        }
    }


}
