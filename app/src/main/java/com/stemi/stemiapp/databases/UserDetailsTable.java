package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.stemi.stemiapp.model.RegisteredUserDetails;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Pooja on 19-09-2017.
 */

public class UserDetailsTable {
    public static final String TABLE_NAME = "dbuserDetails";

    //column names for User Details
    private static final String KEY_ID = "id";
    private static final String USER_UID = "uniqueId";
    private static final String USER_NAME = "name";
    private static final String USER_AGE = "age";
    private static final String USER_GENDER = "gender";
    private static final String USER_PHONE = "phone";
    private static final String USER_ADDRESS = "address";
    private static final String USER_HEIGHT = "height";
    private static final String USER_WEIGHT = "weight";
    private static final String USER_WAIST = "waist";
    private static final String DO_YOU_SMOKE = "doYouSmoke";
    private static final String HAD_HEART_ATTACK = "hadHeartAttack";
    private static final String HAVE_DIABETES = "haveDiabetes";
    private static final String HIGH_BLOOD_PRESSURE = "highBloodPressure";
    private static final String HIGH_CHOLESTEROL = "highCholesterol";
    private static final String HAD_STROKE = "hadStroke";
    private static final String HAVE_ASTHMA = "haveAsthma";
    private static final String FAMILY_HEALTH = "familyHealth";
    private static final  String USER_PROFILR_URL = "profileUrl";

    //sql query to creating User Details table in database
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + USER_UID + " TEXT,"
            + USER_NAME + " TEXT,"
            + USER_AGE + " TEXT,"
            + USER_GENDER + " TEXT,"
            + USER_PHONE + " TEXT,"
            + USER_ADDRESS + " REAL,"
            + USER_HEIGHT + " TEXT,"
            + USER_WEIGHT + " TEXT,"
            + USER_WAIST + " TEXT,"
            + DO_YOU_SMOKE + " TEXT,"
            + HAD_HEART_ATTACK + " TEXT,"
            + HAVE_DIABETES + " TEXT,"
            + HIGH_BLOOD_PRESSURE + " TEXT,"
            + HIGH_CHOLESTEROL + " TEXT,"
            + HAD_STROKE + " TEXT,"
            + HAVE_ASTHMA + " TEXT,"
            + FAMILY_HEALTH + " TEXT, "
            + USER_PROFILR_URL + " TEXT"
            + ")";


    public String isExist(String userName) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String string = null;
        Log.e(TAG, "isExist: " + "SELECT " + USER_NAME + " FROM " + TABLE_NAME + " WHERE " + USER_NAME + "=" + "'" + userName + "'", null);
        Cursor cur = db.rawQuery("SELECT " + USER_NAME + " FROM " + TABLE_NAME + " WHERE " + "UPPER("+ USER_NAME + ")" + "=" + "\"" + userName.toUpperCase() + "\"", null);
        if (cur.moveToFirst()){
            for (int i = 0; i < cur.getCount(); i++){
                string = cur.getString(cur.getColumnIndex(USER_NAME));
                cur.moveToNext();
            }
        }
        cur.close();
        DatabaseManager.getInstance().closeDatabase();
        return string;
    }

    // To get Number of profile
    public long getProfilesCount() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        //db.close();
        return cnt;
    }

    //function for adding the note to database
    public void addEntry(RegisteredUserDetails registeredUserDetails) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_UID,registeredUserDetails.getUniqueId());
        values.put(USER_NAME,registeredUserDetails.getName());
        values.put(USER_AGE,registeredUserDetails.getAge());
        values.put(USER_GENDER,registeredUserDetails.getGender());
        values.put(USER_PHONE,registeredUserDetails.getPhone());
        values.put(USER_ADDRESS,registeredUserDetails.getAddress());
        values.put(USER_HEIGHT,registeredUserDetails.getHeight());
        values.put(USER_WEIGHT,registeredUserDetails.getWeight());
        values.put(USER_WAIST,registeredUserDetails.getWaist());
        values.put(DO_YOU_SMOKE,registeredUserDetails.getDo_you_smoke());
        values.put(HAD_HEART_ATTACK,registeredUserDetails.getHeart_attack());
        values.put(HAVE_DIABETES,registeredUserDetails.getDiabetes());
        values.put(HIGH_BLOOD_PRESSURE,registeredUserDetails.getBlood_pressure());
        values.put(HIGH_CHOLESTEROL,registeredUserDetails.getCholesterol());
        values.put(HAD_STROKE,registeredUserDetails.getHad_paralytic_stroke());
        values.put(HAVE_ASTHMA, registeredUserDetails.getHave_asthma());
        values.put(FAMILY_HEALTH,registeredUserDetails.getFamily_had_heart_attack());
        values.put(USER_PROFILR_URL,registeredUserDetails.getImgUrl());

        long id = db.insert(TABLE_NAME, null, values);
        Log.e("DATABASE VALUES", "addDataTODb: " + id);
        //closing the database connection
        DatabaseManager.getInstance().closeDatabase();

        //see that all database connection stuff is inside this method
        //so we don't need to open and close db connection outside this class

    }


    public void removeUserDetails(String name) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_NAME, USER_NAME + " = ?", new String[] { name });
        DatabaseManager.getInstance().closeDatabase();
    }


    public ArrayList<String> getRecords(){
        ArrayList<String> data=new ArrayList<String>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{USER_NAME},null, null, null, null, null);
        String fieldToAdd=null;
        while(cursor.moveToNext()){
            fieldToAdd=cursor.getString(0);
            data.add(fieldToAdd);
        }
        cursor.close();  // dont forget to close the cursor after operation done
        return data;
    }

}
