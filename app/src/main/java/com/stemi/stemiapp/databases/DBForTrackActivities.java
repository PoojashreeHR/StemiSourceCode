package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.stemi.stemiapp.model.UserEventDetails;
import com.stemi.stemiapp.utils.GlobalClass;
import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 15-09-2017.
 */

public class DBForTrackActivities {
    public static final String USER_ACTIVITIES = "dbUserTrack";

    private static final String USER_BLOOD_REPORT = "userBloodReport";

    //column names for User Details
    private static final String KEY_ID = "id";
    private static final String UID = "user_id";
    private static final String DATE = "date";
    private static final String IS_WALKED = "isWalked";
    private static final String IS_CYCLED = "isCycled";
    private static final String IS_SWIMMED = "isSwimmed";
    private static final String DONE_AEROBICS = "doneAerobics";
    private static final String OTHERS = "orOthers";
    private static final String SMOKE_TODAY = "smokeToday";
    private static final String HOW_MANY_SMOKE = "howMany";
    private static final String STRESS_COUNT = "stressCount";
    private static final String TODAY_WEIGHT = "todaysWeight";
    private static final String BMI_VALUE = "bmiValue";



    public static final String CREATE_USER_ACTIVITY_TABLE = "CREATE TABLE " + USER_ACTIVITIES + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + UID + " TEXT,"
            + DATE + " TEXT,"
            + IS_WALKED + " TEXT,"
            + IS_CYCLED + " TEXT,"
            + IS_SWIMMED + " TEXT,"
            + DONE_AEROBICS + " TEXT,"
            + OTHERS + " TEXT,"
            + SMOKE_TODAY + " TEXT,"
            + HOW_MANY_SMOKE + " TEXT,"
            + STRESS_COUNT + " TEXT,"
            + TODAY_WEIGHT + " TEXT,"
            + BMI_VALUE + " TEXT"
            + ")";


    public boolean isTableEmpty(){
        String query = "SELECT * FROM "+ USER_ACTIVITIES;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        int count = 0;
        try{
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                count++;
                c.moveToNext();
            }
        }
        catch(Exception e){

        }
        return count==0;
    }


    //function for adding the note to database
    public void addEntry(UserEventDetails userEventDetails) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

        values.put(UID,userEventDetails.getUid());
        values.put(DATE,userEventDetails.getDate());
        values.put(IS_WALKED,userEventDetails.getIswalked());
        values.put(IS_CYCLED,userEventDetails.getIsCycled());
        values.put(IS_SWIMMED,userEventDetails.getIsSwimmed());
        values.put(DONE_AEROBICS,userEventDetails.getDoneAerobics());
        values.put(OTHERS,userEventDetails.getOthers());
        values.put(SMOKE_TODAY,userEventDetails.getSmokeToday());
        values.put(HOW_MANY_SMOKE,userEventDetails.getHowMany());
        values.put(STRESS_COUNT,userEventDetails.getStressCount());
        values.put(TODAY_WEIGHT,userEventDetails.getTodaysWeight());
        values.put(BMI_VALUE,userEventDetails.getBmiValue());

        long id = db.insert(USER_ACTIVITIES, null, values);
        Log.e("DATABASE VALUES", "addDataTODb: " + id);
        //closing the database connection
        DatabaseManager.getInstance().closeDatabase();
    }

    public boolean getDate(String date1){
        String query = "SELECT * FROM "+ USER_ACTIVITIES;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        boolean count = false;
        try{
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                String date = c.getString(2);
                if(date.equals(date1)){
                    count = true;
                    break;
                }else {
                    c.moveToNext();
                }
            }
        }
        catch(Exception e){
        }
        return count;
    }
    //update table
    public void updateUserTrack(UserEventDetails userEventDetails,int value) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        ContentValues cv = new ContentValues();
        switch (value){
            case 1:
                cv.put(IS_WALKED,userEventDetails.getIswalked());
                cv.put(IS_CYCLED,userEventDetails.getIsCycled());
                cv.put(IS_SWIMMED,userEventDetails.getIsSwimmed());
                cv.put(DONE_AEROBICS,userEventDetails.getDoneAerobics());
                cv.put(OTHERS,userEventDetails.getOthers());
                break;
            case 2:
                cv.put(STRESS_COUNT,userEventDetails.getStressCount());
                break;
            case 3:
                cv.put(SMOKE_TODAY,userEventDetails.getSmokeToday());
                cv.put(HOW_MANY_SMOKE,userEventDetails.getHowMany());
                break;
            case 4:
                cv.put(TODAY_WEIGHT,userEventDetails.getTodaysWeight());
                cv.put(BMI_VALUE,userEventDetails.getBmiValue());
                break;
        }
        long l = db.update(USER_ACTIVITIES, cv, DATE + "='" + userEventDetails.getDate()
                + "' AND "+ UID + "='"+ userEventDetails.getUid() + "'",null);
        Log.e(TAG, "removeMedicine: "+l );
        DatabaseManager.getInstance().closeDatabase();
    }
}
