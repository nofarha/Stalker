package com.MySpecialSelaker

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQ = 17;
    public static final String PHONE_KEY = "PHONE NUMBER KEY";
    public static final String SMS_KEY = "SMS PREFIX KEY";

    private AlertDialog permissionsExplanationDialog;
    private AlertDialog exitDialog;

    private EditText phoneNumberEditText;
    private EditText smsPrefixEditText;
    private Button saveButton;
    private Boolean isPhoneNumberLegal = false;
    private Boolean isPrefixLegal = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberEditText = findViewById(R.id.editTextPhoneNumber);
        smsPrefixEditText = findViewById(R.id.editTextPrefix);
        saveButton = findViewById(R.id.saveButton);

        phoneNumberEditText.setBackgroundColor(Color.RED);
        smsPrefixEditText.setBackgroundColor(Color.RED);
        saveButton.setEnabled(false);

        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 9) {
                    phoneNumberEditText.setBackgroundColor(Color.GREEN);
                    isPhoneNumberLegal = true;
                    if (isPrefixLegal)
                        saveButton.setEnabled(true);
                } else {
                    phoneNumberEditText.setBackgroundColor(Color.RED);
                    isPhoneNumberLegal = false;
                    saveButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        smsPrefixEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    smsPrefixEditText.setBackgroundColor(Color.GREEN);
                    isPrefixLegal = true;
                    if (isPhoneNumberLegal)
                        saveButton.setEnabled(true);
                } else {
                    smsPrefixEditText.setBackgroundColor(Color.RED);
                    isPrefixLegal = false;
                    saveButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(PHONE__KEY, phoneNumberEditText.getText().toString());
                editor.putString(SMS_KEY, smsPrefixEditText.getText().toString());
                editor.apply();
                finish();
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("The app can't work without all the requested permissions");
        alertDialogBuilder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS,
                                Manifest.permission.SEND_SMS},
                        PERMISSIONS_REQ);
            }
        });
        permissionsExplanationDialog = alertDialogBuilder.create();

        alertDialogBuilder.setMessage("The app can't work without all the requested permissions, please allow permissions in settings.");
        alertDialogBuilder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        exitDialog = alertDialogBuilder.create();

        getPermissions();

    }

    private void getPermissions() {
        Boolean deniedReadPhoneStatePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED;
        Boolean deniedProcessOutgoingCallsPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS)
                != PackageManager.PERMISSION_GRANTED;
        Boolean deniedSendSmsPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED;

        if (deniedProcessOutgoingCallsPermission || deniedReadPhoneStatePermission || deniedSendSmsPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.PROCESS_OUTGOING_CALLS,
                            Manifest.permission.SEND_SMS},
                    PERMISSIONS_REQ);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                grantResults[2] != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.PROCESS_OUTGOING_CALLS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                permissionsExplanationDialog.show();
            } else
                exitDialog.show();
        }
    }

}
