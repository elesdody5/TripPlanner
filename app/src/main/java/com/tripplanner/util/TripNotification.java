package com.tripplanner.util;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.tripplanner.Constants;
import com.tripplanner.R;
import com.tripplanner.alarm.NotificationActivity;
import com.tripplanner.data_layer.local_data.entity.Trip;


public class TripNotification {

    private Context context;
    private NotificationManager notificationManager;
    private Trip trip;

    public TripNotification(Context context, Trip trip) {
        this.context = context;
        this.trip = trip;
        createNotificationChannel();
    }

    public  void sendNotification() {
        notificationManager.notify(Constants.NOTIFICATION_ID, getNotificationBuilder().build());
    }

    public void cancelNotification() {
        notificationManager.cancel(Constants.NOTIFICATION_ID);
    }

    private void createNotificationChannel() {
        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, "Upcoming trips",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setDescription("Upcoming trips notification");
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.putExtra(Constants.RIP_OB_KEY, trip);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,Constants.NOTIFICATION_ID,
                intent, PendingIntent.FLAG_ONE_SHOT);

        return new NotificationCompat.Builder(context,Constants.NOTIFICATION_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle("upcoming trip")
                .setSmallIcon(R.drawable.logo)
                .setContentText("you trip " + trip.getName()+ "Wating to start")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
    }
}
