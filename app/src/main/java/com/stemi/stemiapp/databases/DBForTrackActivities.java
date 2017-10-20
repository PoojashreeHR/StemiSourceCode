package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.stemi.stemiapp.activity.TrackActivity;
import com.stemi.stemiapp.model.BloodTestResult;
import com.stemi.stemiapp.model.MedicineDetails;
import com.stemi.stemiapp.model.MessageEvent;
import com.stemi.stemiapp.model.TrackMedication;
import com.stemi.stemiapp.model.UserEventDetails;
import com.stemi.stemiapp.utils.CommonUtils;
import com.stemi.stemiapp.utils.GlobalClass;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;

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
    private static final String DONE_YOGA = "yoga";
    private static final String DONE_MEDITATION = "meditation";
    private static final String DONE_HOBBIES = "gobbies";
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
            + DONE_YOGA + " TEXT,"
            + DONE_MEDITATION + " TEXT,"
            + DONE_HOBBIES + " TEXT,"
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
        values.put(DONE_YOGA,userEventDetails.getYoga());
        values.put(DONE_MEDITATION,userEventDetails.getMeditation());
        values.put(DONE_HOBBIES,userEventDetails.getHobbies());
        values.put(TODAY_WEIGHT,userEventDetails.getTodaysWeight());
        values.put(BMI_VALUE,userEventDetails.getBmiValue());

        long id = db.insert(USER_ACTIVITIES, null, values);
        Log.e("DATABASE VALUES", "addDataTODb: " + id);
        //closing the database connection
        DatabaseManager.getInstance().closeDatabase();
    }

    public boolean getDate(String date1,String userId){
        String query = "SELECT * FROM " + USER_ACTIVITIES + " WHERE " + UID + " = '" + userId + "'";
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
                cv.put(DONE_YOGA,userEventDetails.getYoga());
                cv.put(DONE_MEDITATION,userEventDetails.getMeditation());
                cv.put(DONE_HOBBIES,userEventDetails.getHobbies());
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


    //get Stress count data
    public int isEntryExists(UserEventDetails userEventDetails,int id,Context context) {
        int count;
        String query = "SELECT * FROM " + USER_ACTIVITIES + " WHERE " + UID + " = '" + userEventDetails.getUid() + "' AND "
                + DATE + " = '" + userEventDetails.getDate() + "'";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            //userEventDetails1 = new Gson().fromJson(valueJson, UserEventDetails.class);

            switch (id){
                case 1:
                    if(c.getString(c.getColumnIndex(IS_WALKED)) !=null || c.getString(c.getColumnIndex(IS_CYCLED)) !=null
                            || c.getString(c.getColumnIndex(IS_SWIMMED)) !=null || c.getString(c.getColumnIndex(DONE_AEROBICS)) !=null||c.getString(c.getColumnIndex(OTHERS)) !=null){
                        CommonUtils.buidDialog(context,1);
                    }else {
                        updateUserTrack(TrackActivity.userEventDetails,1);
                        EventBus.getDefault().post(new MessageEvent("Hello!"));
                        ((TrackActivity) context).setActionBarTitle("Track");
                    }
                    break;
                case 2:
                    String valueJson = c.getString(c.getColumnIndex(STRESS_COUNT));
                    if(valueJson != null){
                        CommonUtils.buidDialog(context,2);
                    }else {
                        updateUserTrack(TrackActivity.userEventDetails,2);
                        EventBus.getDefault().post(new MessageEvent("Hello!"));
                        ((TrackActivity) context).setActionBarTitle("Track");

                    }break;
                case 3:
                    if(c.getString(c.getColumnIndex(SMOKE_TODAY)) !=null){
                        CommonUtils.buidDialog(context,3);
                    }else {
                        updateUserTrack(TrackActivity.userEventDetails,3);
                        EventBus.getDefault().post(new MessageEvent("Hello!"));
                        ((TrackActivity) context).setActionBarTitle("Track");

                    }break;
                case 4:
                    if(c.getString(c.getColumnIndex(TODAY_WEIGHT))!=null){
                        CommonUtils.buidDialog(context,4);
                    }else {
                        updateUserTrack(TrackActivity.userEventDetails,4);
                        EventBus.getDefault().post(new MessageEvent("Hello!"));
                        ((TrackActivity) context).setActionBarTitle("Track");
                    }
            }
            c.close();
           // return count > 0;
        }
        return count = 0;
    }

    public ArrayList<UserEventDetails> getDetails(String uid, String date, int id){
        ArrayList<UserEventDetails> userDetailsTables = new ArrayList<>();
        String query = "SELECT * FROM " + USER_ACTIVITIES + " WHERE " + UID + " = '" + uid + "' AND "
                + DATE + " = '" + date+ "'";
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor c = db.rawQuery(query, null);
        UserEventDetails userEventDetails = new UserEventDetails();
        while (c.moveToNext()) {
            switch (id){
                case 1:
                    userEventDetails.setIswalked(c.getString(c.getColumnIndex(IS_WALKED)));
                    userEventDetails.setIsCycled(c.getString(c.getColumnIndex(IS_CYCLED)));
                    userEventDetails.setIsSwimmed(c.getString(c.getColumnIndex(IS_SWIMMED)));
                    userEventDetails.setDoneAerobics(c.getString(c.getColumnIndex(DONE_AEROBICS)));
                    userEventDetails.setOthers(c.getString(c.getColumnIndex(OTHERS)));
                    userDetailsTables.add(userEventDetails);
                    c.moveToNext();
                    break;
                case 2:
                    userEventDetails.setStressCount(c.getString(c.getColumnIndex(STRESS_COUNT)));
                    userEventDetails.setYoga(c.getString(c.getColumnIndex(DONE_YOGA)));
                    userEventDetails.setMeditation(c.getString(c.getColumnIndex(DONE_MEDITATION)));
                    userEventDetails.setHobbies(c.getString(c.getColumnIndex(DONE_HOBBIES)));
                    userDetailsTables.add(userEventDetails);
                    c.moveToNext();
                    break;
                case 3:
                    userEventDetails.setSmokeToday(c.getString(c.getColumnIndex(SMOKE_TODAY)));
                    userEventDetails.setHowMany(c.getString(c.getColumnIndex(HOW_MANY_SMOKE)));
                    userDetailsTables.add(userEventDetails);
                    c.moveToNext();
                    break;
                case 4:
                    userEventDetails.setTodaysWeight(c.getString(c.getColumnIndex(TODAY_WEIGHT)));
                    userDetailsTables.add(userEventDetails);
                    c.moveToNext();
                    break;
            }
        }
        return userDetailsTables;
    }
}
