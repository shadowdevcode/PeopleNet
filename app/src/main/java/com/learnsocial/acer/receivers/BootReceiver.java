package com.learnsocial.acer.receivers;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;

        import com.learnsocial.acer.application.PeopleNetApplication;
        import com.learnsocial.acer.peoplene.service.UpdaterService;

/**
 * Created by acer on 9/7/2015.
 */
public class BootReceiver extends BroadcastReceiver{

    static final String TAG = "BootReceiver";

    PeopleNetApplication peopleNetApplication;
    @Override
    public void onReceive(Context context, Intent intent) {

        //Start Service
        peopleNetApplication = (PeopleNetApplication) context.getApplicationContext();
        peopleNetApplication.createAlarm();
        Log.d(TAG, "BootReceiver - Start Service");
    }
}
