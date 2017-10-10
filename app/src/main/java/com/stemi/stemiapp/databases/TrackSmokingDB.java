package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.stemi.stemiapp.model.TrackSmoking;
import com.stemi.stemiapp.utils.GlobalClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by praburaam on 04/09/17.
 */

public class TrackSmokingDB extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = GlobalClass.DB_VERSION;

    private static final String DATABASE_NAME = GlobalClass.DB_NAME;

    public static final String TABLE_SMOKING = "tbl_smoking";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_DATE_TIME = "_datetime";
    public static final String COLUMN_SMOKED = "smoked";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_SMOKING + "( " + COLUMN_USER_ID
            + " text not null , " + COLUMN_DATE_TIME
            + " text not null , "+ COLUMN_SMOKED
            + " integer );";

    public TrackSmokingDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_SMOKING);
        onCreate(sqLiteDatabase);
    }

    public void addEntry(TrackSmoking stress){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USER_ID, stress.getUserId());
            cv.put(COLUMN_DATE_TIME, simpleDateFormat.format(stress.getDateTime()));
            cv.put(COLUMN_SMOKED, stress.isSmoked());
            Log.e("StatsFragment", "Saving "+new Gson().toJson(stress));
            db.insertOrThrow(TABLE_SMOKING, null, cv);
        }
        catch(Exception e){
            Log.e("StatsFragment", "Exception "+e.getLocalizedMessage());
        }
        db.close();
    }

    public boolean isTableEmpty(){
        String query = "SELECT * FROM "+ TABLE_SMOKING;
        SQLiteDatabase db = this.getReadableDatabase();
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

    public List<TrackSmoking> getAllInfo(String userid){
        List<TrackSmoking> smokingList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM "+ TABLE_SMOKING +" WHERE "+COLUMN_USER_ID+" = '"+userid+"' "
                    +"ORDER BY "+COLUMN_DATE_TIME;

            Cursor rows = db.rawQuery(query, null);
            rows.moveToFirst();

            while(!rows.isAfterLast()){
                TrackSmoking smoking = new TrackSmoking();
                smoking.setUserId(rows.getString(rows.getColumnIndex(COLUMN_USER_ID)));

                int withinlimitValue = rows.getInt(rows.getColumnIndex(COLUMN_SMOKED));
                smoking.setSmoked(withinlimitValue == 1);

                String timestamp = rows.getString(rows.getColumnIndex(COLUMN_DATE_TIME));

                Date dateValue = simpleDateFormat.parse(timestamp);
                smoking.setDateTime(dateValue);

                smokingList.add(smoking);
                rows.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return smokingList;
    }
    public List<TrackSmoking> getSmokingTrackingInfo(String userid, String date1, String date2){
        List<TrackSmoking> smokingList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM "+ TABLE_SMOKING +" WHERE "+COLUMN_DATE_TIME
                    +" BETWEEN '"+date1+"' AND '"+ date2
                    +"' AND "+COLUMN_USER_ID+" = '"+userid+"' ";

            Cursor rows = db.rawQuery(query, null);
            rows.moveToFirst();

            while(!rows.isAfterLast()){
                TrackSmoking smoking = new TrackSmoking();
                smoking.setUserId(rows.getString(rows.getColumnIndex(COLUMN_USER_ID)));

                int withinlimitValue = rows.getInt(rows.getColumnIndex(COLUMN_SMOKED));
                smoking.setSmoked(withinlimitValue == 1);

                String timestamp = rows.getString(rows.getColumnIndex(COLUMN_DATE_TIME));

                Date dateValue = simpleDateFormat.parse(timestamp);
                smoking.setDateTime(dateValue);

                smokingList.add(smoking);
                rows.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return smokingList;
    }

    public void createIfNotExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_SMOKING;
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

    public int getNumberOfDays(String userId){
        int dayCount = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String query = "SELECT * FROM " + TABLE_SMOKING
                    +" WHERE "+COLUMN_USER_ID+" = '"+userId+"'"
                    +" ORDER BY "+COLUMN_DATE_TIME+" ASC";
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            Log.e("db","query = "+query);
            while (!cursor.isAfterLast()) {
                int withinlimitValue = cursor.getInt(cursor.getColumnIndex(COLUMN_SMOKED));
                if(withinlimitValue == 0){
                    dayCount++;
                }
                else{
                    dayCount = 0;
                }
                cursor.moveToNext();

            }
            Log.e("db","dayCount = "+dayCount);
            cursor.close();
        }
        catch(Exception e){

        }
        return dayCount;
    }
}
