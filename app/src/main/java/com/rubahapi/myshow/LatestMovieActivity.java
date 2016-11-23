package com.rubahapi.myshow;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.rubahapi.myshow.adapter.ContactsAdapter;
import com.rubahapi.myshow.data.Contact;
import com.rubahapi.myshow.data.MovieDBHelper;
import com.rubahapi.myshow.data.MovieProvider;

import java.util.ArrayList;

public class LatestMovieActivity extends AppCompatActivity {

    ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_movie);

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        contacts = Contact.createContactsList(20000);

        ContactsAdapter adapter = new ContactsAdapter(this, contacts);

        rvContacts.setAdapter(adapter);

//        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvContacts.setLayoutManager(new GridLayoutManager(this,2));

        initDB(LatestMovieActivity.this);
    }

    private void initDB(Context context){
//        MovieDBHelper dbHelper = new MovieDBHelper(context);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MovieDBHelper.COLUMN_TITLE,"Finding Dori2");
        cv.put(MovieDBHelper.COLUMN_DESCRIPTION, "Ikan");
        cv.put(MovieDBHelper.COLUMN_YEARS, 2005);
        cv.put(MovieDBHelper.COLUMN_IMAGE_PATH,"https://1.bp.blogspot.com/-TL9x5j53h2k/Vu62cZieVaI/AAAAAAAADdU/efYOrlGvzrEwwyhil3UIaXReYVtkTW1Pg/s1600/Finding_Dory_4.jpg");

        ContentValues cv1 = new ContentValues();
        cv1.put(MovieDBHelper.COLUMN_TITLE,"Finding Dorii5");
        cv1.put(MovieDBHelper.COLUMN_DESCRIPTION, "Ikan 2");
        cv1.put(MovieDBHelper.COLUMN_YEARS, 2005);
        cv1.put(MovieDBHelper.COLUMN_IMAGE_PATH,"https://1.bp.blogspot.com/-TL9x5j53h2k/Vu62cZieVaI/AAAAAAAADdU/efYOrlGvzrEwwyhil3UIaXReYVtkTW1Pg/s1600/Finding_Dory_4.jpg");

//        db.insert(MovieDBHelper.TABLE_MOVIES_NAME,null,cv);
//        db.insert(MovieDBHelper.TABLE_MOVIES_NAME,null,cv1);
//        dbHelper.close();

        Uri uri = Uri.parse("content://" + MovieProvider.CONTENT_AUTHORITY + "/movie");
        Log.i("DEBUG TEST ", MovieProvider.CONTENT_AUTHORITY);
        getContentResolver().delete(uri,null,null);
        getContentResolver().insert(uri, cv);
        getContentResolver().insert(uri, cv1);
        getContentResolver().notifyChange(uri, null);
    }
}
