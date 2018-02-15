package com.praveengupta.mycontacts;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CALL = 0,MSG=1, EDIT = 2, DELETE = 3;
    LinkedList<Contact> Contacts = new LinkedList<Contact>();
    ListView contactListView;
    EditText txtSearch;
    String TAG = "hey";
    Intent intent;
    // Uri imageUri = Uri.parse("android.resource://com.praveengupta.mycontacts/drawable/defaultuser.png");
    DatabaseHandler databaseHandler;
    int longClickedItemIndex;
    ArrayAdapter<Contact> contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtSearch = (EditText) findViewById(R.id.txtSearch);
        contactListView = (ListView) findViewById(R.id.listContacts);
        databaseHandler = new DatabaseHandler(getApplicationContext());

       /* addContacts("topchi","1234","topchi1234","Room 60");
        addContacts("tommy","gvnf","fghbdf","9 60");*/

        //addContacts();

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0)
                    onResume();
                else {
                    String input = charSequence.toString();
                    //DatabaseHandler databaseHandler=new DatabaseHandler(getApplicationContext());
                    Contacts.clear();
                    Log.i(TAG, "1");
                    populateList();
                    Log.i(TAG, "2");


                    List<Contact> addableContacts = databaseHandler.searchContacts(input);
                    System.out.println("hey");
                    Log.i(TAG, "3");

                    int count = databaseHandler.getsearchContactsCount(input);
                    System.out.println("hey");
                    Log.i(TAG, "4");
                    System.out.println("Search" + count);
                    System.out.println("hey");

                    Log.i(TAG, "2");
                    for (int j = 0; j < count; j++) {
                        System.out.println("hey" + 6);
                        Contact contact = addableContacts.get(j);
                        Contacts.add(contact);
                        System.out.println("hey" + 7);

                    }
                    // if(!addableContacts.isEmpty())
                    populateList();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        registerForContextMenu(contactListView);


        contactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedItemIndex = position;
                return false;
            }
        });

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String phoneNumber = Contacts.get(i).getMob();
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Contacts.clear();
        populateList();
        if (databaseHandler.getContactsCount() != 0)
            Contacts.addAll(databaseHandler.getAllContacts());
        populateList();
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderIcon(R.drawable.pencil_icon);
        menu.setHeaderTitle("Contact Options");

        menu.add(Menu.NONE, CALL, Menu.NONE, "Call");
        menu.add(Menu.NONE, MSG, Menu.NONE, "Message");

        menu.add(Menu.NONE, EDIT, Menu.NONE, "Edit Contact");
        menu.add(Menu.NONE, DELETE, Menu.NONE, "Delete Contact");

    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case CALL:
                String phoneNumber = Contacts.get(longClickedItemIndex).getMob();
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
                break;
            case MSG:
                intent = new Intent(this, Message.class);
                intent.putExtra("number", Contacts.get(longClickedItemIndex).getMob());
                startActivity(intent);

                break;
            case EDIT:
                intent = new Intent(this, UpdateContact.class);
                intent.putExtra("key", longClickedItemIndex);
                startActivity(intent);

                break;
            case DELETE:
                databaseHandler.deleteContact(Contacts.get(longClickedItemIndex));
                onResume();
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void addContact(View view) {
        Intent intent = new Intent(this, AddContact.class);
        startActivity(intent);
    }

    private void populateList() {
        contactAdapter = new ContactListAdpter();
        contactListView.setAdapter(contactAdapter);
    }

    private class ContactListAdpter extends ArrayAdapter<Contact> {
        public ContactListAdpter() {
            super(MainActivity.this, R.layout.listview_item, Contacts);
        }

        //CHECK IF NOT NULL NREDED OR NOT
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            Contact currentContact = Contacts.get(position);
            TextView name = (TextView) convertView.findViewById(R.id.cName);
            name.setText(currentContact.getName());
            TextView mob = (TextView) convertView.findViewById(R.id.cMob);
            mob.setText(currentContact.getMob());
            TextView mail = (TextView) convertView.findViewById(R.id.cMail);
            mail.setText(currentContact.getMail());
            TextView address = (TextView) convertView.findViewById(R.id.cAddress);
            address.setText(currentContact.getAddress());
            ImageView img = (ImageView) convertView.findViewById(R.id.imgContact);
            Log.i("pv", currentContact.getImageURI().toString());

            Uri uri = currentContact.getImageURI();


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
                filePath = currentContact.getImageURI().toString();
                try {
                    img.setImageBitmap(BitmapFactory.decodeStream((new URL(filePath)).openStream()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return convertView;

        }
    }
}
