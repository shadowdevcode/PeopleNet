package com.learnsocial.acer.peoplene.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.learnsocial.acer.activity.TimelineActivity;
import com.learnsocial.acer.application.PeopleNetApplication;
import com.learnsocial.acer.peoplenet.R;
import com.learnsocial.acer.utils.DbHelper;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import java.util.Date;

/**
 * Created by acer on 9/10/2015.
 */
public class RefreshService extends IntentService {

    PeopleNetApplication peopleNetApplication;
    FetchTimelinehandle fetchTimelinehandle;
    ContentValues values;
    DbHelper dbHelper;
    SQLiteDatabase db;

    static final String TAG = "RefreshService";

    public RefreshService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {//Implcit thread that is created

        init();

        try {
            Log.d(TAG, "Updater - Running");
            try {
                peopleNetApplication.getYambaClient().fetchFriendsTimeline(fetchTimelinehandle);
            } catch (YambaClientException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class FetchTimelinehandle implements YambaClient.TimelineProcessor {

        @Override
        public boolean isRunnable() {
            return true;
        }

        @Override
        public void onStartProcessingTimeline() {

        }

        @Override
        public void onEndProcessingTimeline() {

        }

        @Override
        public void onTimelineStatus(long id, Date date, String user, String msg) {

            Log.d(TAG, String.format("%s : %s", user, msg));


            values.put(DbHelper.C_ID, id);
            values.put(DbHelper.C_CREATED_AT, date.getTime());
            values.put(DbHelper.C_USER, user);
            values.put(DbHelper.C_TEXT, msg);

            long count = db.insert(DbHelper.TABLE, null, values);
            if (count > 0) {
                sendBroadcast(new Intent(PeopleNetApplication.NEW_STATUS));

                //trigger the notification
                showNotification();
            }
        }
    }


    private void init() {

        values = new ContentValues();
        dbHelper = new DbHelper(this);
        fetchTimelinehandle = new FetchTimelinehandle();
        db = dbHelper.getWritableDatabase();
        peopleNetApplication = (PeopleNetApplication) getApplicationContext();
    }

    private void showNotification(){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        mBuilder.setSmallIcon(R.drawable.ic_menu_notifications);//Icone
        mBuilder.setContentTitle("Notification Alert, Click Me!");
        mBuilder.setContentText("Hi, This is Android Notification Detail!");
        mBuilder.setAutoCancel(true);

        Intent resultIntent = new Intent(this, TimelineActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(TimelineActivity.class);

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());
    }
}
