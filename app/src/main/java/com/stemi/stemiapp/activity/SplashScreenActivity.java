package com.stemi.stemiapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity implements AppConstants {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private static final int MY_PERMISSIONS_REQUEST = 1234;
    boolean introScreensShown;
    AppSharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPreference = new AppSharedPreference(this);
        introScreensShown = sharedPreference.isFirstTimeLaunch(IS_FIRST_TIME_LAUNCH);
        checkAppPermission();
           /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/

    }


    private void proceedWithAppLaunch() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!introScreensShown) {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(SplashScreenActivity.this, OnBoardingActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void checkAppPermission() {

        String[] allPermissions = new String[8];
        int i = 0;

        int readStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (readStorage != PackageManager.PERMISSION_GRANTED) {
            allPermissions[i] = Manifest.permission.READ_EXTERNAL_STORAGE;
            i++;
        }

        int writeStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writeStorage != PackageManager.PERMISSION_GRANTED) {
            allPermissions[i] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            i++;
        }

        int camera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (camera != PackageManager.PERMISSION_GRANTED) {
            allPermissions[i] = Manifest.permission.CAMERA;
            i++;
        }
        int fineLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (fineLocation != PackageManager.PERMISSION_GRANTED) {
            allPermissions[i] = Manifest.permission.ACCESS_FINE_LOCATION;
            i++;
        }

        int coarseLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (coarseLocation != PackageManager.PERMISSION_GRANTED) {
            allPermissions[i] = Manifest.permission.ACCESS_COARSE_LOCATION;
            i++;
        }
        int phoneState = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);
        if (phoneState != PackageManager.PERMISSION_GRANTED) {
            allPermissions[i] = Manifest.permission.READ_PHONE_STATE;
            i++;
        }

        int calender = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALENDAR);
        if (calender != PackageManager.PERMISSION_GRANTED) {
            allPermissions[i] = Manifest.permission.READ_CALENDAR;
            i++;
        }
        int write_calender = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR);
        if (write_calender != PackageManager.PERMISSION_GRANTED) {
            allPermissions[i] = Manifest.permission.WRITE_CALENDAR;
            i++;
        }


        String[] allPerms = new String[i];
        System.arraycopy(allPermissions, 0, allPerms, 0, i);

        if (i > 0) {
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(allPerms, MY_PERMISSIONS_REQUEST);
            } else {
                // checkApiInCache();
                proceedWithAppLaunch();
            }
        } else {
            // checkApiInCache();
            proceedWithAppLaunch();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proceedWithAppLaunch();
                    //   checkApiInCache();
                }
            }
        }
    }
}
