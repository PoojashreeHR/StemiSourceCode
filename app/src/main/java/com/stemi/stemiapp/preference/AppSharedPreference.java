package com.stemi.stemiapp.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.stemi.stemiapp.utils.AppConstants;

/**
 * Created by Pooja on 18-08-2017.
 */

public class AppSharedPreference implements AppConstants {
    private SharedPreferences sharedPreferences;

    public AppSharedPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(APP_PREF_NAME, Context.MODE_PRIVATE);
    }


    //Store Token in SP
    public void addUserToken(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    // Get Token from SP.
    public String getUserToken(String key) {
        return sharedPreferences.getString(key, null);
    }

}

