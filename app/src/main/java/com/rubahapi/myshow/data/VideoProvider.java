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
 * Created by prasetia on 11/29/2016.
 */

public class VideoProvider extends ContentProvider {

    private static final int VIDEO = 200;
    public static final String CONTENT_AUTHORITY = "com.rubahapi";
    //    public final String CONTENT_AUTHORITY = this.getContext().getResources().getString(R.string.content_authority);
    private MovieDBHelper dbHelper;
    private UriMatcher mUriMatcher;

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDBHelper(getContext());
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(CONTENT_AUTHORITY, "video", VIDEO);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionargs, String sortOrder) {
        Cursor cursor = null;
        switch (mUriMatcher.match(uri)){
            case VIDEO:
                cursor = dbHelper.getReadableDatabase().query(
                        MovieDBHelper.TABLE_VIDEOS_NAME,
                        projection,
                        selection,
                        selectionargs,
                        null,
                        null,
                        sortOrder
                );
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = mUriMatcher.match(uri);
        switch (match){
            case VIDEO:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/video";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (mUriMatcher.match(uri)){
            case VIDEO:
                long id = dbHelper.getWritableDatabase().insert(
                        MovieDBHelper.TABLE_VIDEOS_NAME,
                        null,
                        contentValues
                );
                return Uri.parse("content://" + CONTENT_AUTHORITY + "/video/row/" + id);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionargs) {
        switch (mUriMatcher.match(uri)){
            case VIDEO:
                int countDeleted = dbHelper.getWritableDatabase().delete(
                        MovieDBHelper.TABLE_VIDEOS_NAME,
                        selection,
                        selectionargs
                );
                return countDeleted;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionargs) {
        switch (mUriMatcher.match(uri)){
            case VIDEO:
                int countUpdated = dbHelper.getWritableDatabase().update(
                        MovieDBHelper.TABLE_VIDEOS_NAME,
                        contentValues,
                        selection,
                        selectionargs
                );
                return countUpdated;
        }
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        switch (mUriMatcher.match(uri)) {
            case VIDEO:
                int returnCount = 0;

                SQLiteDatabase writableDatabase= dbHelper.getWritableDatabase();
                writableDatabase.beginTransaction();
                try {
                    for (ContentValues cv : values) {
                        writableDatabase.insert(
                                MovieDBHelper.TABLE_VIDEOS_NAME,
                                null,
                                cv
                        );
                        returnCount++;
                    }
                    writableDatabase.setTransactionSuccessful();
                }
                catch (Exception e) {
                    returnCount = 0;
                }
                finally {
                    writableDatabase.endTransaction();
                }
                return returnCount;
        }
        return 0;
    }
}
