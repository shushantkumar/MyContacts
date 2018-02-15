package com.praveengupta.mycontacts;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddContact extends AppCompatActivity {
   // List<Contact> Contacts = new ArrayList<Contact>();
    String TAG="khmm";
    EditText txtName, txtMob, txtMail, txtAddress;
    ImageView img;
    Button add;
    DatabaseHandler databaseHandler;
    Uri imageUri = Uri.parse("android.resource://com.praveengupta.mycontacts/drawable/defaultuser.png");
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        databaseHandler = new DatabaseHandler(getApplicationContext());
        txtName = (EditText) findViewById(R.id.txtName);
        txtMob = (EditText) findViewById(R.id.txtMob);
        txtMail = (EditText) findViewById(R.id.txtMail);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        img = (ImageView) findViewById(R.id.img);

        add = (Button) findViewById(R.id.add);

        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                add.setEnabled(!txtName.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);
            }
        });
    }

    public void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == RESULT_OK) {
            if (reqCode == 1) {
                imageUri = data.getData();
                img.setImageURI(data.getData());
            }
        }
    }


    public void add(View view) {
        Log.i(TAG,imageUri.toString());
        Log.i(TAG,"Hey Tommy");
        Contact contact = new Contact(databaseHandler.getContactsCount(),
                txtName.getText().toString(), txtMob.getText().toString()
                , txtMail.getText().toString(), txtAddress.getText().toString(),
                imageUri);
        databaseHandler.createContact(contact);
        Toast.makeText(getApplicationContext(), txtName.getText().toString() + " successfully added !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
