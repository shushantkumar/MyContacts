package com.praveengupta.mycontacts;


import android.net.Uri;

/**
 * Created by Praveen Gupta on 10-11-2016.
 */

public class Contact {

    private String name1, mob1, mail1, address1;
    private int id1;
    private Uri imgURI1;

    public Contact(int id, String name, String mob, String mail, String address, Uri imgURI) {
        id1=id;
        name1 = name;
        mob1 = mob;
        mail1 = mail;
        address1 = address;
        imgURI1=imgURI;
    }

    public int getId(){return id1;}

    public String getName(){return name1;}

    public String getMob(){return mob1;}

    public String getMail(){return mail1;}

    public String getAddress(){return address1;}

    public Uri getImageURI(){return imgURI1;}


}
