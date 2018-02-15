package com.praveengupta.mycontacts;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class UpdateContact extends AppCompatActivity {
    int key;
    Contact contact;
    DatabaseHandler databaseHandler;
    EditText txtName, txtMob, txtMail, txtAddress;
    ImageView img;
    Uri imageUri = Uri.parse("android.resource://com.praveengupta.mycontacts/drawable/defaultuser.png");
    Button update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        databaseHandler = new DatabaseHandler(getApplicationContext());

        key = getIntent().getExtras().getInt("key");
        key += 1;
        Log.i("key", key + " ");

        txtName = (EditText) findViewById(R.id.txtName);
        txtMob = (EditText) findViewById(R.id.txtMob);
        txtMail = (EditText) findViewById(R.id.txtMail);
        txtAddress = (EditText) findViewById(R.id.txtAddress);
        img = (ImageView) findViewById(R.id.img);

        contact = databaseHandler.getContact(key);
        Log.i("key", contact.getName());

        txtName.setText(contact.getName());
        txtMob.setText(contact.getMob());
        txtMail.setText(contact.getMail());
        txtAddress.setText(contact.getAddress());
        Uri uri = contact.getImageURI();
        update = (Button) findViewById(R.id.update);
        imageUri=contact.getImageURI();


        String filePath = "";


        try {
            String wholeID = DocumentsContract.getDocumentId(uri);

// Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

// where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = getContentResolver().
                    query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);


            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
            img.setImageBitmap(BitmapFactory.decodeFile(filePath));

        } catch (IllegalArgumentException e) {
            filePath = contact.getImageURI().toString();
            try {
                img.setImageBitmap(BitmapFactory.decodeStream((new URL(filePath)).openStream()));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }


        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                update.setEnabled(!txtName.getText().toString().trim().isEmpty());

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
    public void update(View view) {
        contact = new Contact(key, txtName.getText().toString(), txtMob.getText().toString(), txtMail.getText().toString(), txtAddress.getText().toString(), imageUri);
        databaseHandler.updateContact(contact);
        Toast.makeText(getApplicationContext(), txtName.getText().toString() + " successfully updated !", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
