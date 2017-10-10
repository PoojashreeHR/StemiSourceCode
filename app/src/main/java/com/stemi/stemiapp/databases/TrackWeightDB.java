package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stemi.stemiapp.model.TrackWeight;
import com.stemi.stemiapp.utils.GlobalClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by praburaam on 11/09/17.
 */

public class TrackWeightDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = GlobalClass.DB_VERSION;

    private static final String DATABASE_NAME = GlobalClass.DB_NAME;

    public static final String TABLE_WEIGHT = "tbl_weight";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_MONTH_INDEX = "month_index";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_WEIGHT = "weight";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_WEIGHT + "( " + COLUMN_USER_ID
            + " text not null , " + COLUMN_MONTH_INDEX
            + " text not null , " + COLUMN_YEAR
            + " text not null , "+ COLUMN_WEIGHT
            + " integer );";

    public TrackWeightDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("StatsFragment", "create sql = "+ DATABASE_CREATE);
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_WEIGHT);
        onCreate(sqLiteDatabase);
    }

    public void addEntry(TrackWeight weight){
        if(isEntryExists(weight)){
            updateEntry(weight);
        }
        else{
            insertEntry(weight);
        }
    }

    public void insertEntry(TrackWeight weight){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USER_ID, weight.getUserId());
            cv.put(COLUMN_MONTH_INDEX, weight.getMonthIndex());
            cv.put(COLUMN_WEIGHT, weight.getWeight());
            cv.put(COLUMN_YEAR, weight.getYear());
            long i = db.insertOrThrow(TABLE_WEIGHT, null, cv);
            Log.e("StatsFragment", "i = "+i);

        }
        catch(Exception e){
            Log.e("StatsFragment", "Exception e = "+ e.getLocalizedMessage());
        }
        db.close();
    }

    public void updateEntry(TrackWeight weight){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USER_ID, weight.getUserId());
            cv.put(COLUMN_MONTH_INDEX, weight.getMonthIndex());
            cv.put(COLUMN_WEIGHT, weight.getWeight());
            cv.put(COLUMN_YEAR, weight.getYear());
            String whereCaluse = COLUMN_USER_ID+"='"+weight.getUserId()+"' AND "+COLUMN_MONTH_INDEX+" = '"+weight.getMonthIndex()+"'";
            db.update(TABLE_WEIGHT, cv,whereCaluse ,null);

        }
        catch(Exception e){

        }
        db.close();
    }

    public boolean isEntryExists(TrackWeight weight){
        String query = "SELECT * FROM "+TABLE_WEIGHT+" WHERE "+COLUMN_MONTH_INDEX+" = '"+weight.getMonthIndex()+"'"
                +" AND "+COLUMN_YEAR+" = '"+weight.getYear()+"'"
                +" AND "+COLUMN_USER_ID+" = '"+weight.getUserId()+"'";
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

        }
        return count>0;
    }

    public List<TrackWeight> getEntries(String userId){
        List<TrackWeight> trackWeightList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String query = "SELECT * FROM "+TABLE_WEIGHT+" WHERE " +COLUMN_USER_ID+" = '"+userId+"'"
                +" AND "+COLUMN_YEAR+" = '"+year+"'"
                ;
        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();

            while(!c.isAfterLast()){
                TrackWeight weight = new TrackWeight();
                weight.setUserId(c.getString(c.getColumnIndex(COLUMN_USER_ID)));
                weight.setMonthIndex(Integer.parseInt(c.getString(c.getColumnIndex(COLUMN_MONTH_INDEX))));
                weight.setWeight(c.getInt(c.getColumnIndex(COLUMN_WEIGHT)));
                weight.setYear(c.getString(c.getColumnIndex(COLUMN_YEAR)));
                trackWeightList.add(weight);
                c.moveToNext();
            }
        }
        catch(Exception e){
            if(e.getLocalizedMessage().contains("no such table")){
                onCreate(db);
            }
        }
        return trackWeightList;
    }

    public void createIfNotExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_WEIGHT;
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                cursor.moveToNext();
            }

            cursor.close();
        } catch (Exception e) {
            if (e.getLocalizedMessage().contains("no such table")) {
                onCreate(db);
            }
        }
    }

    public int getLastKnownWeight(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        int weight = 0;
        try {
            String query = "SELECT * FROM " + TABLE_WEIGHT+ " WHERE "+COLUMN_USER_ID+" = '"+userId+"'"
                    +" ORDER BY "+COLUMN_YEAR+" DESC , "
                    +COLUMN_MONTH_INDEX+" DESC LIMIT 1"
                    ;
            Log.e("db","query = "+query);
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();


            while (!cursor.isAfterLast()) {
                weight = cursor.getInt(cursor.getColumnIndex(COLUMN_WEIGHT));
                cursor.moveToNext();
            }
            Log.e("db","weight = "+weight);
            cursor.close();
        } catch (Exception e) {

        }
        return weight;
    }
}
