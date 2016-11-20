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
 * Created by junifar on 20-Nov-16.
 */

public class SunshineProvider extends ContentProvider {
    private static final int RAMALAN = 100;
    private static final int RAMALAN_BY_DATE = 200;
    RamalanDBHelper dbHelper;
    UriMatcher mUriMatcher;

    @Override
    public boolean onCreate() {
        dbHelper = new RamalanDBHelper(getContext());
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI("com.rubahapi.myshow", "ramalan", RAMALAN);
        mUriMatcher.addURI("com.rubahapi.myshow", "ramalan/#", RAMALAN_BY_DATE);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor retCursor = dbHelper.getReadableDatabase().query(RamalanDBHelper.TABLE_RAMALAN,strings, s, strings1, s1, null, null);
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int kode = mUriMatcher.match(uri);
        switch (kode){
            case  RAMALAN:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.rubahapi.myshow/ramalan";
            case RAMALAN_BY_DATE:
                return  ContentResolver.CURSOR_DIR_BASE_TYPE + "/com.rubahapi.myshow/ramalan";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (mUriMatcher.match(uri)){
            case RAMALAN :
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.insert(RamalanDBHelper.TABLE_RAMALAN, null, contentValues);

                return  Uri.parse("content://com.rubahapi.myshow/ramalan/row/" + 1);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;

        if (null == s) s = "1";

        rowsDeleted = db.delete(RamalanDBHelper.TABLE_RAMALAN, s, strings);

        if(rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }
    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(RamalanDBHelper.COLUMN_DT)) {
            long dateValue = values.getAsLong(RamalanDBHelper.COLUMN_DT);
//            values.put(RamalanDBHelper.COLUMN_DT, RamalanDBHelper.normalizeDate(dateValue));
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated;

        rowsUpdated = db.update(RamalanDBHelper.TABLE_RAMALAN, contentValues, s, strings);

        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
