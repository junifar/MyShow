package com.rubahapi.myshow.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by junifar on 20-Nov-16.
 */

public class RamalanDBHelper extends SQLiteOpenHelper {

    public static  final  String CUACA_SQLITE = "Cuaca.sqlite";
    public static  final int VERSION = 1;
    public static final String COLUMN_CITY_ID = "CITY_ID";
    public static final String COLUMN_DT = "DT";
    public static final String COLUMN_MIN = "MIN";
    public static final String COLUMN_MAX = "MAX";
    public static final String COLUMN_W_ID = "W_ID";
    public static final String COLUMN_W_DESC = "W_DESC";
    public static final String COLUMN_W_ICON = "W_ICON";
    public static final String COLUMN_HUMIDITY = "HUMIDITY";
    public static final String COLUMN_PRESSURE = "PRESSURE";
    public static final String COLUMN_WIND_SPEED = "WIND_SPEED";
    public static final String COLUMN_WIND_DEGREE = "WIND_DEGREE";
    public static final String TABLE_RAMALAN = "RAMALAN";

    public RamalanDBHelper(Context context) {
        super(context, CUACA_SQLITE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("DB", "DATABASE CREATED");
        final String SQL_CREATE_QUERY = "CREATE TABLE " + TABLE_RAMALAN + " (" +
                COLUMN_CITY_ID + " INT," +
                COLUMN_DT + " INT," +
                COLUMN_MIN + " REAL," +
                COLUMN_MAX + " REAL," +
                COLUMN_W_ID + " TEXT," +
                COLUMN_W_DESC + " TEXT," +
                COLUMN_W_ICON + " TEXT," +
                COLUMN_HUMIDITY + " REAL," +
                COLUMN_PRESSURE + " REAL," +
                COLUMN_WIND_SPEED + " REAL," +
                COLUMN_WIND_DEGREE + " INT, " +
                " UNIQUE (" + COLUMN_CITY_ID + "," + COLUMN_DT + ") ON CONFLICT REPLACE" +
                ")";
        sqLiteDatabase.execSQL(SQL_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i("DB", "DTABASE UPDATED");
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_RAMALAN);
        onCreate(sqLiteDatabase);
    }
}
