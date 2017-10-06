package com.stemi.stemiapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.stemi.stemiapp.databases.BloodTestDB;
import com.stemi.stemiapp.databases.DBHelper;
import com.stemi.stemiapp.databases.DataUploadedDB;
import com.stemi.stemiapp.databases.DatabaseManager;
import com.stemi.stemiapp.databases.FollowupsDB;
import com.stemi.stemiapp.databases.TrackExerciseDB;
import com.stemi.stemiapp.databases.TrackMedicationDB;
import com.stemi.stemiapp.databases.TrackSmokingDB;
import com.stemi.stemiapp.databases.TrackStressDB;
import com.stemi.stemiapp.databases.TrackWeightDB;
import com.stemi.stemiapp.databases.UserDetailsTable;
import com.stemi.stemiapp.model.RegisteredUserDetails;
import com.stemi.stemiapp.preference.AppSharedPreference;
import com.stemi.stemiapp.utils.AppConstants;
import com.stemi.stemiapp.utils.GlobalClass;
import com.stemi.stemiapp.utils.TyprfaceUtils;
import com.testfairy.TestFairy;

import java.util.List;

/**
 * Created by praburaam on 14/09/17.
 */

public class StemiApplication extends Application {
    private static Context context;
    private static DBHelper dbHelper;


    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setupFonts();
        context = this;
        dbHelper = new DBHelper();
        DatabaseManager.initializeInstance(dbHelper);
        AppSharedPreference appSharedPreference = new AppSharedPreference(this);
        GlobalClass.userID = appSharedPreference.getUserId();
        Log.e("StemiApplication", " GlobalClass.userID = "+ GlobalClass.userID);
        if(GlobalClass.userID == null) {
            UserDetailsTable dBforUserDetails = new UserDetailsTable(this);
            List<RegisteredUserDetails> allUsers = dBforUserDetails.getAllUsers();
            if(allUsers != null && allUsers.size()>0) {
                GlobalClass.userID = allUsers.get(0).getUniqueId();
                appSharedPreference.setUserId(GlobalClass.userID);
                Log.e("StemiApplication", " GlobalClass.userID = " + GlobalClass.userID);
            }
        }

        boolean firstTimeLaunch = appSharedPreference.isFirstTimeLaunch(AppConstants.IS_FIRST_TIME_LAUNCH);
        if(!firstTimeLaunch){
            createAllTables();
        }

        TestFairy.begin(this, "2077dc440588e99912fa2b124e87756f90295c5f");
    }

    public void createAllTables(){
        BloodTestDB bloodTestDB = new BloodTestDB();
        //DBforUserDetails dBforUserDetails = new DBforUserDetails(this);
        TrackMedicationDB medicationDB = new TrackMedicationDB(this);
        TrackSmokingDB smokingDB = new TrackSmokingDB(this);
        TrackExerciseDB exerciseDB = new TrackExerciseDB(this);
        TrackStressDB stressDB = new TrackStressDB(this);
        TrackWeightDB weightDB = new TrackWeightDB(this);
        FollowupsDB followupsDB = new FollowupsDB(this);
        DataUploadedDB dataUploadedDB = new DataUploadedDB(this);

        bloodTestDB.createIfNotExists();
        //bloodTestDB.close();

        //dBforUserDetails.createIfNotExists();
        //dBforUserDetails.close();

        medicationDB.createIfNotExists();
        //medicationDB.close();

        smokingDB.createIfNotExists();
        smokingDB.close();

        exerciseDB.createIfNotExists();
        exerciseDB.close();

        stressDB.createIfNotExists();
        stressDB.close();

        weightDB.createIfNotExists();
        weightDB.close();

        followupsDB.createIfNotExists();
        followupsDB.close();

        dataUploadedDB.createIfNotExists();
        dataUploadedDB.close();
    }


    private void setupFonts() {
        TyprfaceUtils.setDefaultFont(this, "SANS", "fonts/font_roboto_regular.ttf");
        TyprfaceUtils.setDefaultFont(this, "MONOSPASE", "fonts/raleway_semibold.ttf");
        TyprfaceUtils.setDefaultFont(this, "SERIF", "fonts/font_roboto_medium.ttf");
       // TyprfaceUtils.setDefaultFont(this, "NORMAL", "fonts/font_roboto_light.ttf");
    }
}
