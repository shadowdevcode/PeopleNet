package com.learnsocial.acer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.learnsocial.acer.application.PeopleNetApplication;
import com.learnsocial.acer.peoplene.service.UpdaterService;
import com.learnsocial.acer.peoplenet.R;
import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;


public class StatusActivity extends ActionBarActivity implements TextWatcher,LocationListener {

    Button buttonUpdate;
    EditText editText;
    Location location;
    LocationManager locationManager;
    String provider;
    ProgressDialog progressDialog;
    TextView textCounter;
    PeopleNetApplication peopleNetApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        peopleNetApplication = (PeopleNetApplication) getApplicationContext();
        textCounter = (TextView)findViewById(R.id.textCount);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        editText = (EditText) findViewById(R.id.editText);
        editText.addTextChangedListener(this);

        textCounter.setText(Integer.toString(140));
        textCounter.setTextColor(Color.GREEN);

        progressDialog = new ProgressDialog(this);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String status = editText.getText().toString();
                new PostToTwitter().execute(status);
            }
        };

        buttonUpdate.setOnClickListener(listener);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    protected void onResume() {
        super.onResume();
        provider = peopleNetApplication.getProvider();

        if(!provider.equalsIgnoreCase(PeopleNetApplication.LOCATION_PROVIDER_NONE)){

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

        if(locationManager != null){
            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 0, 0, this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void afterTextChanged(Editable status) {

        int count = 140 - status.length();

        textCounter.setText(Integer.toString(count));
        textCounter.setTextColor(Color.GREEN);

        if(count <10){
            textCounter.setTextColor(Color.YELLOW);
        }
        if(count<=0)
        {
            textCounter.setTextColor(Color.RED);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
// TODO Auto-generated method stub
        if (this.provider.equals(provider))
            locationManager.requestLocationUpdates(provider, 0, 0, this);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        if (this.provider.equals(provider))
            locationManager.removeUpdates(this);

    }

    class PostToTwitter extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {//Intialisation here
            super.onPreExecute();
            progressDialog.setMessage("Please Wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {//This is an Implicit thread

            try {
                peopleNetApplication.getYambaClient().postStatus(params[0],location.getLatitude(),location.getLongitude());
                //peopleNetApplication.getYambaClient().postStatus(params[0]);
                return "Successfully Posted " + params[0];
            } catch (YambaClientException e) {
                e.printStackTrace();
                return "Failed To Post";

            }

        }

        @Override
        protected void onPostExecute(String result) {//Update the UI thread --> Show toast
            super.onPostExecute(result);

            progressDialog.dismiss();
            Toast.makeText(StatusActivity.this, result, Toast.LENGTH_SHORT).show();


        }

    }


}
