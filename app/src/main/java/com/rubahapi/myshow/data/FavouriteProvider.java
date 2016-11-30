package com.rubahapi.myshow.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by prasetia on 11/30/2016.
 */

public class FavouriteProvider extends ContentProvider {

    private static final int FAVOURITE = 100;
    public static final String CONTENT_AUTHORITY = "com.rubahapi.favourite";
    //    public final String CONTENT_AUTHORITY = this.getContext().getResources().getString(R.string.content_authority);
    private MovieDBHelper dbHelper;
    private UriMatcher mUriMatcher;

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDBHelper(getContext());
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(CONTENT_AUTHORITY, "favourite", FAVOURITE);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionargs, String sortOrder) {
        Cursor retCursor = null;

        switch(mUriMatcher.match(uri)){
            case FAVOURITE :
                retCursor = dbHelper.getReadableDatabase().query(
                        MovieDBHelper.TABLE_FAVOURITE_NAME,
                        projection,
                        selection,
                        selectionargs,
                        null,
                        null,
                        sortOrder);
                retCursor.setNotificationUri(getContext().getContentResolver(), uri);
//                retCursor.close();
                break;
        }
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch ((mUriMatcher.match(uri))){
            case FAVOURITE:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/favourite";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (mUriMatcher.match(uri)){
            case FAVOURITE :
                long id = dbHelper.getWritableDatabase().insert(MovieDBHelper.TABLE_FAVOURITE_NAME,
                        null, contentValues);
                return Uri.parse("content://" + CONTENT_AUTHORITY + "/favourite/" + id);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)){
            case FAVOURITE:
                return dbHelper.getWritableDatabase().delete(MovieDBHelper.TABLE_FAVOURITE_NAME,
                        selection, selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)){
            case FAVOURITE:
                return dbHelper.getWritableDatabase().update(
                        MovieDBHelper.TABLE_FAVOURITE_NAME,
                        contentValues,
                        selection,
                        selectionArgs
                );
        }
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        switch (mUriMatcher.match(uri)){
            case FAVOURITE:
                int returnCount = 0;

                SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                writableDatabase.beginTransaction();
                try {
                    for (ContentValues cv : values){
                        writableDatabase.insert(
                                MovieDBHelper.TABLE_FAVOURITE_NAME,
                                null,
                                cv
                        );
                        returnCount++;
                    }
                    writableDatabase.setTransactionSuccessful();
                }catch (Exception e){
                    returnCount = 0;
                }finally {
                    writableDatabase.endTransaction();
                }
                return returnCount;
        }
        return 0;
    }
}
