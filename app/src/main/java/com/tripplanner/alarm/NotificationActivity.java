package com.tripplanner.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class NotificationActivity extends AppCompatActivity {

        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        displayAlert();
        }

        private void displayAlert ()
        {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            builder.setMessage("Are you sure you want to exit?").setCancelable(
                    false).setPositiveButton("Yes",
                    (dialog, id) -> dialog.cancel())
                    .setNegativeButton("No",
                    (dialog, id) -> dialog.cancel());

            builder.show();
        }

    public static class TripAlarmReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent i = new Intent(context, NotificationActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
