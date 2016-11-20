package com.rubahapi.myshow;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.rubahapi.myshow.data.RamalanDBHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RamalanDBHelper dbHelper = new RamalanDBHelper(MainActivity.this);
//        Try insert 1 record
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(RamalanDBHelper.COLUMN_CITY_ID, 1642911);
        cv.put(RamalanDBHelper.COLUMN_DT, 1479614400);
        cv.put(RamalanDBHelper.COLUMN_MIN, 26.52);
        cv.put(RamalanDBHelper.COLUMN_MAX, 31.99);
        cv.put(RamalanDBHelper.COLUMN_W_DESC, "Mendung");

        db.insert(RamalanDBHelper.TABLE_RAMALAN,null,cv);

        long cnt = DatabaseUtils.queryNumEntries(db,RamalanDBHelper.TABLE_RAMALAN);
        Log.i("DB","Total Record : " + cnt);

        dbHelper.close();
    }
}