package com.learnsocial.acer.utils;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.learnsocial.acer.peoplenet.R;

/**
 * Created by acer on 9/4/2015.
 */
public class TimelineAdapter extends SimpleCursorAdapter{

    static final String[] FROM = { DbHelper.C_CREATED_AT, DbHelper.C_USER,DbHelper.C_TEXT }; //
    static final int[] TO = { R.id.textCreatedAt, R.id.textUser, R.id.textText }; //
    public TimelineAdapter(Context context, Cursor c) {

        super(context, R.layout.row, c, FROM, TO, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        TextView textCreatedAt = (TextView)view.findViewById(R.id.textCreatedAt);
        long timeStamp = cursor.getLong(cursor.getColumnIndex(DbHelper.C_CREATED_AT));

        textCreatedAt.setText(DateUtils.getRelativeTimeSpanString(timeStamp));
    }
}
