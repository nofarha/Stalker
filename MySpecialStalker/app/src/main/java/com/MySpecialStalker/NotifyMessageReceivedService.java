package com.MySpecialSelaker

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;


public class NotifyMessageReceivedService extends IntentService {

    public NotifyMessageReceivedService() {
        super("NotifyMessageReceivedService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), intent.getStringExtra("channel id"))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Message received successfully!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(intent.getIntExtra("notification id", 1) , builder.build());
    }

}

