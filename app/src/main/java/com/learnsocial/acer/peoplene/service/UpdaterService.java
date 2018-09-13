package com.learnsocial.acer.peoplene.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.learnsocial.acer.application.PeopleNetApplication;
import com.learnsocial.acer.utils.DbHelper;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import java.util.Date;

/**
 * Created by acer on 8/31/2015.
 */
public class UpdaterService extends Service {

    static final long DELAY = 20000;
    Updater updater;
    boolean runFlag = false;
    PeopleNetApplication peopleNetApplication;
    FetchTimelinehandle fetchTimelinehandle;

    ContentValues values;
    DbHelper dbHelper;
    SQLiteDatabase db;

    static final String TAG = "UpdaterService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "UpdaterService - onCreate");
        peopleNetApplication = (PeopleNetApplication) getApplicationContext();
        updater = new Updater();
        fetchTimelinehandle = new FetchTimelinehandle();
        values = new ContentValues();
        dbHelper = new DbHelper(this);

         db = dbHelper.getWritableDatabase();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!runFlag) {
            runFlag = true;
            updater.start();

        }
        peopleNetApplication.setIsServiceRunning(true);
        Log.d(TAG, "UpdaterService - onStartCommand");

        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        runFlag = false;
        updater.interrupt();
        peopleNetApplication.setIsServiceRunning(false);
        Log.d(TAG, "UpdaterService - onDestroy");
    }

        class Updater extends Thread {

        public void run() {
            Log.d(TAG, "Updater - Ran");
            while (runFlag) {
                try {
                    Log.d(TAG, "Updater - Running");
                    try {
                        peopleNetApplication.getYambaClient().fetchFriendsTimeline(fetchTimelinehandle);
                    } catch (YambaClientException e) {
                        e.printStackTrace();
                    }
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class FetchTimelinehandle implements YambaClient.TimelineProcessor{

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

            Log.d(TAG,String.format("%s : %s", user,msg));


            values.put(DbHelper.C_ID, id);
            values.put(DbHelper.C_CREATED_AT,date.getTime());
            values.put(DbHelper.C_USER,user);
            values.put(DbHelper.C_TEXT, msg);

            long count = db.insert(DbHelper.TABLE, null, values);
            if(count>0){
                sendBroadcast(new Intent(PeopleNetApplication.NEW_STATUS));
            }
        }
    }
}
