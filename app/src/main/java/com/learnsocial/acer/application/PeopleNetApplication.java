package com.learnsocial.acer.application;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.learnsocial.acer.peoplene.service.RefreshService;
import com.marakana.android.yamba.clientlib.YambaClient;

/**
 * Created by acer on 9/1/2015.
 */
public class PeopleNetApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String NEW_STATUS ="vijay.sachin.mahesh";
    AlarmManager alarmManager;



    long interval;

    public SharedPreferences getPrefs() {
        return prefs;
    }

    SharedPreferences prefs;
    YambaClient yambaClient;
    public static final String LOCATION_PROVIDER_NONE ="NONE";
    String provider;

    public String getProvider() {
        return prefs.getString("provider",null);
    }
    public long getInterval() {
         return Long.parseLong(prefs.getString("interval", "0"));
    }
    public boolean isServiceRunning() {
        return isServiceRunning;
    }

    public void setIsServiceRunning(boolean isServiceRunning) {
        this.isServiceRunning = isServiceRunning;
    }

    boolean isServiceRunning;
    public YambaClient getYambaClient() {

        String name, password, apiRoot;
        if (yambaClient == null) {
            name = prefs.getString("username",null);
            password = prefs.getString("password",null);
            apiRoot = prefs.getString("apiRoot",null);

            yambaClient = new YambaClient(name,password,apiRoot);
        }
        return yambaClient;
    }



    @Override
    public void onCreate() {
        super.onCreate();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        yambaClient = null;
    }


    public void createAlarm(){

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent iRefreshService = new Intent(this, RefreshService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this,-1,iRefreshService,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),getInterval(),pendingIntent);

    }
}
