package com.rubahapi.myshow.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Dibuat Oleh junifar on 11/23/2016.
 */
public class MovieProvider extends ContentProvider {

    private static final int MOVIE = 100;
    public static final String CONTENT_AUTHORITY = "com.rubahapi.myshow";
//    public final String CONTENT_AUTHORITY = this.getContext().getResources().getString(R.string.content_authority);
    private MovieDBHelper dbHelper;
    private UriMatcher mUriMatcher;

    @Override
    public boolean onCreate() {
        dbHelper = new MovieDBHelper(getContext());
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(CONTENT_AUTHORITY, "movie", MOVIE);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionargs, String sortOrder) {
        Cursor retCursor = null;

        switch(mUriMatcher.match(uri)){
            case MOVIE :
                retCursor = dbHelper.getReadableDatabase().query(
                        MovieDBHelper.TABLE_MOVIES_NAME,
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
            case MOVIE:
                Log.i("TEST",CONTENT_AUTHORITY + "/movie");
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/movie";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (mUriMatcher.match(uri)){
            case MOVIE :
                long id = dbHelper.getWritableDatabase().insert(MovieDBHelper.TABLE_MOVIES_NAME,
                        null, contentValues);
                return Uri.parse("content://" + CONTENT_AUTHORITY + "/movie/" + id);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)){
            case MOVIE:
                return dbHelper.getWritableDatabase().delete(MovieDBHelper.TABLE_MOVIES_NAME,
                        selection, selectionArgs);
        }
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        switch (mUriMatcher.match(uri)){
            case MOVIE:
                int returnCount = 0;

                SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
                writableDatabase.beginTransaction();
                try {
                    for (ContentValues cv : values){
                        writableDatabase.insert(
                                MovieDBHelper.TABLE_MOVIES_NAME,
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

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        switch (mUriMatcher.match(uri)){
            case MOVIE:
                return dbHelper.getWritableDatabase().update(
                        MovieDBHelper.TABLE_MOVIES_NAME,
                        contentValues,
                        selection,
                        selectionArgs
                );
        }
        return 0;
    }
}
