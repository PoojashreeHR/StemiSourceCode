package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stemi.stemiapp.model.RegisteredUserDetails;
import com.stemi.stemiapp.model.UserEventDetails;

/**
 * Created by Pooja on 15-09-2017.
 */

public class DBForTrackActivities extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "dbUserActivities";
    private static final String USER_ACTIVITIES = "dbUserTrackActivities";
    private static final String USER_BLOOD_REPORT = "userBloodReport";

    //column names for User Details
    private static final String KEY_ID = "id";
    private static final String IS_WALKED = "isWalked";
    private static final String IS_CYCLED = "isCycled";
    private static final String IS_SWIMMED = "isSwimmed";
    private static final String DONE_AEROBICS = "doneAerobics";
    private static final String OTHERS = "orOthers";
    private static final String DATE = "date";
    private static final String UID = "user_id";
    private static final String SMOKE_TODAY = "smokeToday";
    private static final String HOW_MANY_SMOKE = "smokeToday";
    private static final String STRESS_TODAY = "stressToda";
    private static final String TODAY_WEIGHT = "todaysWeight";
    private static final String BMI_VALUE = "bmiValue";



    private static final String CREATE_USER_ACTIVITY_TABLE = "CREATE TABLE " + USER_ACTIVITIES + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + UID + " TEXT,"
            + DATE + " TEXT,"
            + IS_WALKED + " TEXT,"
            + IS_CYCLED + " TEXT,"
            + IS_SWIMMED + " TEXT,"
            + DONE_AEROBICS + " TEXT,"
            + OTHERS + " TEXT"
            + ")";

    public DBForTrackActivities(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_ACTIVITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_ACTIVITIES);
        onCreate(db);
    }

    //function for adding the note to database
    public void addEntry(UserEventDetails userEventDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(UID,userEventDetails.getUid());
        values.put(DATE,userEventDetails.getDate());
        values.put(IS_WALKED,userEventDetails.getIswalked());
        values.put(IS_CYCLED,userEventDetails.getIsCycled());
        values.put(IS_SWIMMED,userEventDetails.getIsSwimmed());
        values.put(DONE_AEROBICS,userEventDetails.getDoneAerobics());
        values.put(OTHERS,userEventDetails.getOthers());

        long id = db.insert(USER_ACTIVITIES, null, values);
        Log.e("DATABASE VALUES", "addDataTODb: " + id);
        //closing the database connection
        db.close();

        //see that all database connection stuff is inside this method
        //so we don't need to open and close db connection outside this class

    }
}
