package com.rubahapi.myshow.data;

import java.util.ArrayList;

/**
 * Created by prasetia on 11/22/2016.
 */

public class Contact {
    private String mName;
    private boolean mOnline;

    public String getmImagePath() {
        return mImagePath;
    }

    private String mImagePath;

    public String getmName() {
        return mName;
    }

//    public void setmName(String mName) {
//        this.mName = mName;
//    }

    public boolean ismOnline() {
        return mOnline;
    }

//    public void setmOnline(boolean mOnline) {
//        this.mOnline = mOnline;
//    }

    public Contact(String mName, boolean mOnline, String mImagePath) {
        this.mName = mName;
        this.mOnline = mOnline;
        this.mImagePath = mImagePath;
    }

    private static  int lastContactId = 0;

    public static ArrayList<Contact> createContactsList(int numContacts){
        ArrayList<Contact> contacts = new ArrayList<>();
        for (int i = 1; i <= numContacts; i++){
            contacts.add(new Contact("Person " + ++lastContactId, i <= numContacts / 2, "http://i37.photobucket.com/albums/e60/mavikaye/yoyo-cici/yoyocici-disp028.jpg"));
        }

        return contacts;
    }
}
