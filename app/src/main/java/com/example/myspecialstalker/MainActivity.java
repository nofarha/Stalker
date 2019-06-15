package com.example.myspecialstalker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_PER_SMS = 1546;

    BroadcastReceiver reciever;
    EditText phoneNumber;
    EditText textSend;
    TextView info;
    boolean is_phone_inserted = false;
    boolean is_text_inserted = false;
    private SharedPreferences sp;
    String textSP;
    String phoneNumberSP;
    private MyBroadcastReciever bc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textSP = getPreference(getApplicationContext(), "text");
        phoneNumberSP = getPreference(getApplicationContext(), "phone");

        phoneNumber = findViewById(R.id.editTextPhoneNumber);
        textSend = findViewById(R.id.editTextTextSend);
        info = findViewById(R.id.textViewInfo);

        phoneNumber.setText(phoneNumberSP);
        textSend.setText(textSP);


        textSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                is_text_inserted = true;
                setPreference(getApplicationContext(), "text", textSend.getText().toString());
                if (is_text_inserted) {
                    textChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                is_phone_inserted = true;
                setPreference(getApplicationContext(), "phone", phoneNumber.getText().toString());
                if (is_text_inserted) {
                    textChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });


        if (reciever == null) {
            // INTENT FILTER FOR GPS MONITORING
            final IntentFilter theFilter = new IntentFilter();
            theFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
            reciever = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null) {
                        String s = intent.getAction();
                        if (s != null) {
                            if (s.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                                final String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);          // 3
                                sendSMS(phoneNumber);
                            }
                        }
                    }
                }
            };
            this.registerReceiver(reciever, theFilter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        textSP = getPreference(getApplicationContext(), "text");
        phoneNumberSP = getPreference(getApplicationContext(), "phone");
        if (phoneNumberSP != null) {
            phoneNumber.setText(phoneNumberSP);
        }

        if (textSP != null) {
            textSend.setText(textSP);
        }

    }

    public boolean setPreference(Context context, String key, String value) {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public String getPreference(Context context, String key) {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (key.equals("text")) {
            return sp.getString(key, "Im going ti call this number : ");
        }
        return sp.getString(key, " ");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void sendSMS(String MobileNumber) {
        if (permissionSMS()) {
            SmsManager smgr = SmsManager.getDefault();
            System.out.println(textSend.getText().toString()+MobileNumber);
            smgr.sendTextMessage(phoneNumber.getText().toString(), null, textSend.getText().toString()+MobileNumber, null, null);
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.SEND_SMS}, REQ_CODE_PER_SMS);
            if (permissionSMS()) {
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(MobileNumber, null, textSend.getText().toString(), null, null);
            }
        }
    }

    public boolean permissionSMS() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean permissionOutGoingCall() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED;
    }

    public void textChanged() {
        info.setText("Ready to send SMS!");
        if (permissionOutGoingCall()) {
            bc = new MyBroadcastReciever();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);
            if (permissionOutGoingCall()) {
                bc = new MyBroadcastReciever();
            }
        }
    }
}



