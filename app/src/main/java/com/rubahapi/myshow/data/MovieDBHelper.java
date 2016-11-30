package com.rubahapi.myshow.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by prasetia on 11/23/2016.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    public static  final  String DB_NAME = "movie.sqlite";
    public static  final int VERSION = 7;
    public static final String TABLE_MOVIES_NAME = "MOVIES";
    public static final String COLUMN_REVIEW_ID = "_id";
    public static final String COLUMN_ID = COLUMN_REVIEW_ID;
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_YEARS = "YEARS";
    public static final String COLUMN_DURATION = "DURATION";
    public static final String COLUMN_RATING = "RATING";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_IMAGE_PATH = "IMAGE_PATH";

    public static final String TABLE_VIDEOS_NAME = "VIDEOS";
    public static final String COLUMN_VIDEO_ID = COLUMN_REVIEW_ID;
    public static final String COLUMN_FAVOURITE_MOVIE_ID = "MOVIE_ID";
    public static final String COLUMN_REVIEW_MOVIE_ID = COLUMN_FAVOURITE_MOVIE_ID;
    public static final String COLUMN_VIDEO_MOVIE_ID = COLUMN_REVIEW_MOVIE_ID;
    public static final String COLUMN_VIDEO_NAME = "NAME";
    public static final String COLUMN_VIDEO_KEY = "KEY";
    public static final String TABLE_REVIEW_NAME = "REVIEW";
    public static final String COLUMN_REVIEW_AUTHOR = "AUTHOR";
    public static final String COLUMN_REVIEW_CONTENT = "CONTENT";
    public static final String COLUMN_REVIEW_URL = "URL";
    public static final String COLUMN_FAVOURITE = "FAVOURITE";
    public static final String TABLE_FAVOURITE_NAME = COLUMN_FAVOURITE;


    public MovieDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_QUERY = "CREATE TABLE " + TABLE_MOVIES_NAME + "(" +
                COLUMN_ID + " INTEGER," +
                COLUMN_TITLE + " TEXT NOT NULL," +
                COLUMN_YEARS + " INTEGER," +
                COLUMN_DURATION + " TEXT," +
                COLUMN_RATING + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_IMAGE_PATH + " TEXT," +
                COLUMN_FAVOURITE + " TEXT," +
                " UNIQUE (" + COLUMN_ID + ") ON CONFLICT REPLACE" +
                ")";
        sqLiteDatabase.execSQL(SQL_CREATE_QUERY);

        final String SQL_CREATE_QUERY_VIDEO = "CREATE TABLE " + TABLE_VIDEOS_NAME + " (" +
                COLUMN_VIDEO_ID + " TEXT," +
                COLUMN_VIDEO_MOVIE_ID + " INTEGER," +
                COLUMN_VIDEO_NAME + " TEXT," +
                COLUMN_VIDEO_KEY + " TEXT," +
                "UNIQUE ("+ COLUMN_VIDEO_ID +") ON CONFLICT REPLACE" +
                ")";

        sqLiteDatabase.execSQL(SQL_CREATE_QUERY_VIDEO);

        final String SQL_CREATE_QUERY_REVIEW = "CREATE TABLE " + TABLE_REVIEW_NAME + " (" +
                COLUMN_REVIEW_ID + " TEXT," +
                COLUMN_REVIEW_MOVIE_ID + " INTEGER," +
                COLUMN_REVIEW_AUTHOR + " TEXT," +
                COLUMN_REVIEW_CONTENT + " TEXT," +
                COLUMN_REVIEW_URL + " TEXT," +
                "UNIQUE (" + COLUMN_REVIEW_ID + ") ON CONFLICT REPLACE" +
                ")";
        sqLiteDatabase.execSQL(SQL_CREATE_QUERY_REVIEW);

        final String SQL_CREATE_QUERY_FAVOURITE = "CREATE TABLE " + TABLE_FAVOURITE_NAME + " (" +
                COLUMN_FAVOURITE_MOVIE_ID + " INTEGER," +
                "UNIQUE (" + COLUMN_FAVOURITE_MOVIE_ID + ") ON CONFLICT REPLACE" +
                ")";

        sqLiteDatabase.execSQL(SQL_CREATE_QUERY_FAVOURITE);;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEW_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE_NAME);
        onCreate(sqLiteDatabase);
    }
}