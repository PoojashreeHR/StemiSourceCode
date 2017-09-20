package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.stemi.stemiapp.model.BloodTestResult;
import com.stemi.stemiapp.utils.GlobalClass;

import java.text.SimpleDateFormat;

/**
 * Created by praburaam on 13/09/17.
 */

public class BloodTestDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = GlobalClass.DB_VERSION;

    private static final String DATABASE_NAME = GlobalClass.DB_NAME;

    public static final String TABLE_BLOOD_TEST = "tbl_blood_test";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_DATE_TAG = "datetag";
    public static final String COLUMN_VALUE = "value";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_BLOOD_TEST + "( " + COLUMN_USER_ID
            + " text not null , " + COLUMN_DATE_TAG
            + " text not null , "+ COLUMN_VALUE
            + " text not null );";
    private static final String TAG = "BloodTestDB";

    public BloodTestDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
        Log.e(TAG,"DATABASE_CREATE = "+DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_BLOOD_TEST);
        onCreate(sqLiteDatabase);
    }

    public void addEntry(String userId, String dateTag, BloodTestResult bloodTestResult){
        if(isEntryExists(userId,dateTag)){
            Log.e(TAG, "Updating entry");
            updateEntry(userId, dateTag, bloodTestResult);
        }
        else{
            Log.e(TAG, "Adding entry");
            SQLiteDatabase db = this.getWritableDatabase();
            try{

                ContentValues cv = new ContentValues();
                cv.put(COLUMN_USER_ID, userId);
                cv.put(COLUMN_DATE_TAG, dateTag);
                String valueJson = new Gson().toJson(bloodTestResult);
                cv.put(COLUMN_VALUE, valueJson);
                db.insertOrThrow(TABLE_BLOOD_TEST, null, cv);
            }
            catch(Exception e){
                Log.e(TAG, e.getLocalizedMessage());
            }
            db.close();
        }

    }

    public boolean isEntryExists(String userId, String dateTag){
        String query = "SELECT * FROM "+TABLE_BLOOD_TEST+" WHERE "+COLUMN_DATE_TAG+" = '"+dateTag+"'"
                +" AND "+COLUMN_USER_ID+" = '"+userId+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        try{

            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                count++;
                c.moveToNext();
            }
            c.close();
        }
        catch(Exception e){
            Log.e(TAG, e.getLocalizedMessage());
        }
        return count>0;
    }

    public void updateEntry(String userId, String dateTag, BloodTestResult bloodTestResult){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USER_ID, userId);
            cv.put(COLUMN_DATE_TAG, dateTag);
            String valueJson = new Gson().toJson(bloodTestResult);
            cv.put(COLUMN_VALUE, valueJson);
            String whereClause = COLUMN_USER_ID+" = '"+userId+"' AND "+COLUMN_DATE_TAG+" = '"+dateTag+"'";
            db.update(TABLE_BLOOD_TEST, cv, whereClause, null);
        }
        catch(Exception e){
            Log.e(TAG, e.getLocalizedMessage());
        }
        db.close();
    }

    public BloodTestResult getEntry(String userId, String dateTag){
        SQLiteDatabase db = this.getReadableDatabase();
        BloodTestResult bloodTestResult = new BloodTestResult();
        try{
            String query = "SELECT * FROM "+TABLE_BLOOD_TEST+" WHERE "+COLUMN_USER_ID+
                    " = '"+userId+"' AND "+COLUMN_DATE_TAG+" = '"+dateTag+"'";
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                String valueJson = cursor.getString(cursor.getColumnIndex(COLUMN_VALUE));
                bloodTestResult = new Gson().fromJson(valueJson, BloodTestResult.class);
                cursor.moveToNext();
            }

            cursor.close();
        }
        catch(Exception e){
            Log.e(TAG, e.getLocalizedMessage());
            if(e.getLocalizedMessage().contains("no such table")){
                onCreate(db);
            }
        }
        return bloodTestResult;
    }
}