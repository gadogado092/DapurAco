package budi.dapuraco;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ForgotActivity extends AppCompatActivity {
    private EditText kode;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 117;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        kode = findViewById(R.id.editTextKode);

        requestSmsPermission();

    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");


                kode.setText(message);
            }
        }
    };


    private void requestSmsPermission() {
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ForgotActivity.this,"permission granted", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(ForgotActivity.this,"permission not granted", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(ForgotActivity.this).
                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    /*if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");


                kode.setText(message);
            }
        }
    };

    private boolean checkAndRequestPermissions() {


        int receiveSMS = ContextCompat.checkSelfPermission(ForgotActivity.this,
                Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(ForgotActivity.this,
                Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(ForgotActivity.this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(ForgotActivity.this).
                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }*/


}
