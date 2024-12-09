package com.example.d308.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.d308.R;

public class ExcursionReceiver extends BroadcastReceiver {
    String channel_id = "test";
    static int notificationID;
    @Override
    public void onReceive(Context context, Intent intent) {


        Toast.makeText(context, intent.getStringExtra("key"), Toast.LENGTH_LONG).show();
        createNotificationChannel(context, channel_id);
        Notification n = new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.baseline_access_alarm_24)
                .setContentText(intent.getStringExtra("key"))
                .setContentTitle("Notification Test").build();
        NotificationManager notificationManager=(NotificationManager)  context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID++, n);
    }
    private void createNotificationChannel(Context context, String CHANNEL_ID){
        CharSequence name = "channelname";
        String description = "mychanneldescription";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}