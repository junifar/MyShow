package com.rubahapi.myshow.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by prasetia on 11/29/2016.
 */

public class VideoDBHelper extends SQLiteOpenHelper {

    public static  final  String DB_NAME = "video.sqlite";
    public static  final int VERSION = 1;
    public static final String TABLE_VIDEOS_NAME = "VIDEOS";
    public static final String COLUMN_VIDEO_ID = "_id";
    public static final String COLUMN_VIDEO_MOVIE_ID = "MOVIE_ID";
    public static final String COLUMN_VIDEO_NAME = "NAME";
    public static final String COLUMN_VIDEO_KEY = "KEY";

    VideoDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_QUERY_VIDEO = "CREATE TABLE " + TABLE_VIDEOS_NAME + " (" +
                COLUMN_VIDEO_ID + " TEXT," +
                COLUMN_VIDEO_MOVIE_ID + " INT," +
                COLUMN_VIDEO_NAME + " TEXT," +
                COLUMN_VIDEO_KEY + " TEXT," +
                "UNIQUE ("+ COLUMN_VIDEO_ID +") ON CONFLICT REPLACE" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_QUERY_VIDEO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS_NAME);
        onCreate(sqLiteDatabase);
    }
}
