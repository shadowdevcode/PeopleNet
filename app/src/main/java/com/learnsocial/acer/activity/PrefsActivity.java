package com.learnsocial.acer.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.learnsocial.acer.peoplenet.R;

/**
 * Created by acer on 9/1/2015.
 */
public class PrefsActivity extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);
    }
}
