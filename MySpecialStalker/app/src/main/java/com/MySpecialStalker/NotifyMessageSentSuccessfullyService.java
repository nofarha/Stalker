package com.MySpecialSelaker

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;


public class NotifyMessageSentSuccessfullyService extends IntentService {


    public NotifyMessageSentSuccessfullyService() {
        super("NotifyMessageSentSuccessfullyService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), intent.getStringExtra("channel id"))
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Message sent successfully")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        notificationManager.notify(intent.getIntExtra("notification id", 1) , builder.build());
    }

}
