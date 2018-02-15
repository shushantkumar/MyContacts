package com.praveengupta.mycontacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Message extends AppCompatActivity {
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        number = getIntent().getExtras().getString("number");
        ((EditText)findViewById(R.id.txtMob)).setText(number);
    }

    public void send(View view) {
        String mob = ((EditText) findViewById(R.id.txtMob)).getText().toString();
        String msg = ((EditText) findViewById(R.id.txtMsg)).getText().toString();

        try {
            SmsManager manager = SmsManager.getDefault();
            manager.sendTextMessage(mob, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.i("msg",e.getMessage());
            EditText txtMob=(EditText)findViewById(R.id.txtMob);
            if(txtMob.getText().toString().isEmpty())
            Toast.makeText(getApplicationContext(), "Please Enter a Mobile Number", Toast.LENGTH_SHORT).show();

        }
    }
}
