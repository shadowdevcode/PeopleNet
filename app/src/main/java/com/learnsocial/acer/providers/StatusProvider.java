package com.learnsocial.acer.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.learnsocial.acer.utils.DbHelper;

/**
 * Created by acer on 9/8/2015.
 */
public class StatusProvider extends ContentProvider {

    private static final String TAG = StatusProvider.class.getSimpleName();
    public static final Uri CONTENT_URI = Uri.parse("content://com.learnsocial.acer.providers.statusprovider");

    public static final String SINGLE_RECORD_MIME_TYPE = "vnd.android.cursor.item/vnd.sanjeev.yamba.status";
    public static final String MULTIPLE_RECORDS_MIME_TYPE = "vnd.android.cursor.dir/vnd.sanjeev.yamba.status";

    DbHelper dbHelper;
    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        long id = this.getId(uri);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Log.d(TAG, "querying");

        Cursor c;

        if (id < 0) {
            c = db.query(DbHelper.TABLE, projection, selection, selectionArgs,
                    null, null, sortOrder);
        } else {
            c = db.query(DbHelper.TABLE, projection, DbHelper.C_ID + "=" + id,
                    null, null, null, null);
        }

        // Notify the context's ContentResolver if the cursor result set changes
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        return this.getId(uri) < 0 ? MULTIPLE_RECORDS_MIME_TYPE
                : SINGLE_RECORD_MIME_TYPE;
    }


    private long getId(Uri uri) {
        String lastPathSegment = uri.getLastPathSegment();
        if (lastPathSegment != null) {
            try {
                return Long.parseLong(lastPathSegment);
            } catch (NumberFormatException e) {
                // at least we tried
            }
        }
        return -1;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            long id = db.insertOrThrow(DbHelper.TABLE, null, values);
            if (id == -1) {
                throw new RuntimeException(String.format(
                        "%s: Failed to insert [%s] to [%s] for unknown reasons.", TAG,
                        values, uri));
            } else {
                Uri newUri = ContentUris.withAppendedId(uri, id);
                // Notify the Context's ContentResolver of the change
                getContext().getContentResolver().notifyChange(newUri, null);
                return newUri;
            }
        } finally {
            db.close();
        }
    }

    @Override
    public int delete(Uri uri,   String selection, String[] selectionArgs) {
        long id = this.getId(uri);
        int count;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (id < 0) {
                count = db.delete(DbHelper.TABLE, selection, selectionArgs);
            } else {
                count = db.delete(DbHelper.TABLE, DbHelper.C_ID + "=" + id,
                        null);
            }
        } finally {
            db.close();
        }

        // Notify the Context's ContentResolver of the change
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        long id = this.getId(uri);
        int count;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (id < 0) {
                count = db.update(DbHelper.TABLE, values, selection, selectionArgs);
            } else {
                count = db.update(DbHelper.TABLE, values, DbHelper.C_ID + "=" + id,
                        null);
            }
        } finally {
            db.close();
        }

        // Notify the Context's ContentResolver of the change
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
