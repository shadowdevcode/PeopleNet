package com.learnsocial.acer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.learnsocial.acer.application.PeopleNetApplication;
import com.learnsocial.acer.peoplenet.R;
import com.learnsocial.acer.utils.DbHelper;
import com.learnsocial.acer.utils.TimelineAdapter;

/**
 * Created by acer on 9/3/2015.
 */
public class TimelineActivity extends BaseActivity {

    SQLiteDatabase db;
    DbHelper dbHelper;
    Cursor cursor;
    TextView textTimeline;
    ListView listView;
    SimpleCursorAdapter adapter;
    TimelineReceiver receiver;


    // TimelineAdapter adapter;
    static final String[] FROM = {DbHelper.C_CREATED_AT, DbHelper.C_USER, DbHelper.C_TEXT}; //
    static final int[] TO = {R.id.textCreatedAt, R.id.textUser, R.id.textText}; //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (peopleNetApplication.getPrefs().getString("username", null) == null) {
            startActivity(new Intent(this, PrefsActivity.class));
            Toast.makeText(this, "Please Enter Credentials", Toast.LENGTH_LONG).show();
        }
        setContentView(R.layout.timeline);
        dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
        receiver = new TimelineReceiver();
        IntentFilter filter = new IntentFilter(PeopleNetApplication.NEW_STATUS);
        registerReceiver(receiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void refreshList() {
        cursor = db.query(DbHelper.TABLE, null, null, null, null, null, DbHelper.C_CREATED_AT + " DESC");

        adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO, 0);
        adapter.setViewBinder(VIEW_BINDER);
        // adapter = new TimelineAdapter(this,cursor);

        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cursor.close();
    }

    static final SimpleCursorAdapter.ViewBinder VIEW_BINDER = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

            if (view.getId() != R.id.textCreatedAt)
                return false;

            TextView textCreatedAt = (TextView) view.findViewById(R.id.textCreatedAt);
            long timeStamp = cursor.getLong(columnIndex);

            textCreatedAt.setText(DateUtils.getRelativeTimeSpanString(timeStamp));

            return true;
        }
    };

    class TimelineReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshList();
        }
    }
}
