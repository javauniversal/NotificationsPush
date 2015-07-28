package com.appgestor.notificationspush;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gcm.GCMRegistrar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerUser(this);
    }


    private void registerUser(Context context){
        //GCMRegistrar.checkDevice(this);
        //GCMRegistrar.checkManifest(this);
        final String regId = GCMRegistrar.getRegistrationId(context);
        if (regId.equals("")) {
            GCMRegistrar.register(context, "AIzaSyAncH6Db138fR1D5YCoYE9SeOQEn8XivAA");
            GCMRegistrar.setRegisteredOnServer(this, true);
            Log.v("GCM", "Registrado");
        } else {
            Log.v("GCM", "Ya registrado");
        }
    }

    private void unregisterUser(Context context){
        final String regId = GCMRegistrar.getRegistrationId(context);
        if (!regId.equals("")) {
            GCMRegistrar.unregister(context);
        } else {
            Log.v("GCM", "Ya des-registrado");
        }
    }
}
