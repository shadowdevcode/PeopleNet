package com.learnsocial.acer.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by acer on 9/3/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    static final String TAG = "DbHelper";
    public static final String DB_NAME = "timeline.db"; //2
    public static final int DB_VERSION = 2; //
    public static final String TABLE = "timeline"; //3
    public static final String C_ID = BaseColumns._ID;
    public static final String C_CREATED_AT = "created_at";
    public static final String C_TEXT = "txt";
    public static final String C_USER = "user";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table " + TABLE + " (" + C_ID + " int primary key, "
                + C_CREATED_AT + " int, " + C_USER + " text, " + C_TEXT + " text)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists " + TABLE);
        onCreate(db);
    }
}
