package com.learnsocial.acer.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.learnsocial.acer.application.PeopleNetApplication;
import com.learnsocial.acer.peoplene.service.RefreshService;
import com.learnsocial.acer.peoplenet.R;

import com.learnsocial.acer.peoplene.service.UpdaterService;

/**
 * Created by acer on 9/4/2015.
 */
public class BaseActivity extends ActionBarActivity {

    PeopleNetApplication peopleNetApplication;
    Menu mMenu = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        peopleNetApplication = (PeopleNetApplication) getApplicationContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_status, menu);
        mMenu = menu;
        onMenuOpened(0, mMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemRefresh:

                startService(new Intent(this, RefreshService.class));

                break;
            case R.id.itemStatus:
                startActivity(new Intent(this, StatusActivity.class));
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, PrefsActivity.class));
                break;
            case R.id.itemTimeline:
                startActivity(new Intent(this, TimelineActivity.class));
                break;

            /**
             case R.id.itemToogle:

             if (peopleNetApplication.isServiceRunning()) {
             //Stop Service
             stopService(new Intent(this, UpdaterService.class));
             Toast.makeText(this,"1 " + peopleNetApplication.isServiceRunning(),Toast.LENGTH_SHORT).show();
             onMenuOpened(0,mMenu);
             } else {
             //Start Service
             startService(new Intent(this, UpdaterService.class));
             Toast.makeText(this," 2 " + peopleNetApplication.isServiceRunning(),Toast.LENGTH_SHORT).show();
             onMenuOpened(0, mMenu);
             }
             * */
        }
        return true;


    }
/*
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {

        if(menu!=null) {
            MenuItem itemToogle = menu.findItem(R.id.itemToogle);

            if (peopleNetApplication.isServiceRunning()) {
                //Stop Service
                itemToogle.setTitle(R.string.titleStopService);
                Toast.makeText(this, " 3 " + peopleNetApplication.isServiceRunning(), Toast.LENGTH_SHORT).show();
            } else
                //Start Service
                itemToogle.setTitle(R.string.titleStartService);
            Toast.makeText(this, "4 " + peopleNetApplication.isServiceRunning(), Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    */
}
