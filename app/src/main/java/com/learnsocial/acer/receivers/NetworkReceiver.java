package com.learnsocial.acer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.learnsocial.acer.peoplene.service.UpdaterService;

/**
 * Created by acer on 9/7/2015.
 */
public class NetworkReceiver extends BroadcastReceiver {

    static final String TAG = "NetworkReceiver";
    Context mContext = null;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;

        if(isConnected()){
            //start Service
            mContext.startService(new Intent(mContext,UpdaterService.class));

            Log.d(TAG,"NetworkReceiver - Start Service");
        }else{
            //stop service
            mContext.stopService(new Intent(mContext, UpdaterService.class));
            Log.d(TAG, "NetworkReceiver - Stop Service");
        }
    }

    public  boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!=null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
