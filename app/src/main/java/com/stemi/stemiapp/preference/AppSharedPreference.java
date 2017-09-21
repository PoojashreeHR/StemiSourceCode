package com.stemi.stemiapp.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.GlobalClass;

/**
 * Created by Pooja on 18-08-2017.
 */

public class AppSharedPreference implements AppConstants {
    private static final String USERID = "userid";
    private static final String LOGINID = "loginId";
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

    //Store  Person Name in SP
    public void addProfileName(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    // Get Token from SP.
    public String getProfileName(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void setUserId(String userId){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERID, userId);
        editor.apply();
    }

    public String getUserId(){
        return sharedPreferences.getString(USERID, null);
    }

    public void setFirstTimeLaunch(String key , boolean isFirstTime) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, isFirstTime);
        editor.apply();
    }

    public boolean isFirstTimeLaunch(String value) {
        return sharedPreferences.getBoolean(value, false);
    }

    public void removeAllSPData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_TOKEN);
        editor.remove(PROFILE_NAME);
        //editor.remove(LOGINID);
        editor.remove(USERID);
        editor.apply();
    }

    public void addLoginId(String loginId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGINID, loginId);
        editor.apply();
    }

    public String getLoginId(){
        return sharedPreferences.getString(LOGINID,null);
    }
}

