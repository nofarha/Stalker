package com.example.myspecialstalker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;
import com.example.myspecialstalker.MainActivity;


public class MyBroadcastReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("a call is being made!!!!");
            final String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);          // 3
//            Intent i=new Intent(context,MainActivity.class);
//            i.putExtra("phoneNumber", phoneNumber );



        }
}