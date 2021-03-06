package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stemi.stemiapp.model.TrackExercise;
import com.stemi.stemiapp.utils.GlobalClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by praburaam on 04/09/17.
 */

public class TrackExerciseDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = GlobalClass.DB_VERSION;

    private static final String DATABASE_NAME = GlobalClass.DB_NAME;

    public static final String TABLE_EXERCISE = "tbl_exercise";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_DATE_TIME = "_datetime";
    public static final String COLUMN_EXERCISED = "exercised";
    public static final String COLUMN_WEEK_NO = "weekNo";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_EXERCISE + "( " + COLUMN_USER_ID
            + " text not null , " + COLUMN_DATE_TIME
            + " text not null , "+ COLUMN_EXERCISED
            + " integer , "+ COLUMN_WEEK_NO+
            " integer );";

    public TrackExerciseDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_EXERCISE);
        onCreate(sqLiteDatabase);
    }

    public void addEntry(TrackExercise stress){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USER_ID, stress.getUserId());
            cv.put(COLUMN_DATE_TIME, simpleDateFormat.format(stress.getDateTime()));
            cv.put(COLUMN_EXERCISED, stress.isExercised());

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(stress.getDateTime());
            cv.put(COLUMN_WEEK_NO, calendar.get(Calendar.WEEK_OF_YEAR));

            if(entryExists(stress.getUserId(), simpleDateFormat.format(stress.getDateTime()))){
                String whereClause = COLUMN_USER_ID+" = '"+stress.getUserId()+"' AND "
                        +COLUMN_DATE_TIME+" = '"+simpleDateFormat.format(stress.getDateTime())+"'";
                Log.e("db", "Updating TABLE_EXERCISE with whereClause = "+ whereClause);
                db.update(TABLE_EXERCISE,cv,whereClause,null);
            }
            else{
                db.insert(TABLE_EXERCISE, null, cv);
            }

        }
        catch(Exception e){

        }
        db.close();
    }

    private boolean entryExists(String userId, String datetime) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean isEntryExists = false;
        try{
            String sql = "SELECT * FROM "+TABLE_EXERCISE+" WHERE "
                    +COLUMN_USER_ID+" = '"+userId+"' AND "
                    +COLUMN_DATE_TIME+" = '"+datetime+"'";

            Cursor cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                String user = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID));
                isEntryExists = true;
                cursor.moveToNext();
            }
        }
        catch (Exception e){

        }
        return isEntryExists;
    }

    public boolean isTableEmpty(){
        String query = "SELECT * FROM "+ TABLE_EXERCISE;
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

    public List<TrackExercise> getAllInfo(String userid){
        List<TrackExercise> exerciseList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM "+ TABLE_EXERCISE +" WHERE "+COLUMN_USER_ID+" = '"+userid+"' "
                    +"ORDER BY "+COLUMN_DATE_TIME+" ASC";

            Cursor rows = db.rawQuery(query, null);
            rows.moveToFirst();

            while(!rows.isAfterLast()){
                TrackExercise exercise = new TrackExercise();
                exercise.setUserId(rows.getString(rows.getColumnIndex(COLUMN_USER_ID)));

                int withinlimitValue = rows.getInt(rows.getColumnIndex(COLUMN_EXERCISED));
                exercise.setExercised(withinlimitValue == 1);

                String timestamp = rows.getString(rows.getColumnIndex(COLUMN_DATE_TIME));

                Date dateValue = simpleDateFormat.parse(timestamp);
                exercise.setDateTime(dateValue);

                exerciseList.add(exercise);
                rows.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return exerciseList;
    }
    public List<TrackExercise> getExerciseTrackingInfo(String userid, String date1, String date2){
        List<TrackExercise> exerciseList = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM "+ TABLE_EXERCISE +" WHERE "+COLUMN_DATE_TIME
                    +" BETWEEN '"+date1+"' AND '"+ date2
                    +"' AND "+COLUMN_USER_ID+" = '"+userid+"' ";

            Cursor rows = db.rawQuery(query, null);
            rows.moveToFirst();

            while(!rows.isAfterLast()){
                TrackExercise exercise = new TrackExercise();
                exercise.setUserId(rows.getString(rows.getColumnIndex(COLUMN_USER_ID)));

                int withinlimitValue = rows.getInt(rows.getColumnIndex(COLUMN_EXERCISED));
                exercise.setExercised(withinlimitValue == 1);

                String timestamp = rows.getString(rows.getColumnIndex(COLUMN_DATE_TIME));

                Date dateValue = simpleDateFormat.parse(timestamp);
                exercise.setDateTime(dateValue);

                exerciseList.add(exercise);
                rows.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return exerciseList;
    }

    public void createIfNotExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_EXERCISE;
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


    public int getNumberofDays(String userID){
        int dayCount = 0;
        ArrayList<String> dates =  new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String query = "SELECT " +COLUMN_DATE_TIME+ " FROM " + TABLE_EXERCISE
                + " WHERE " + COLUMN_USER_ID + " = '" + userID + "'  AND "+ COLUMN_EXERCISED+" = 1"
                + " ORDER BY " + COLUMN_DATE_TIME + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            Log.e("db", "query = " + query);


            while (!cursor.isAfterLast()) {
                String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_TIME));
                cursor.moveToNext();

                try {
                    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                    Date date1 = null;
                    Date date2 = null;
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    date1 = df.parse(date);
                    date2 = df.parse(currentDate);
                    long diff = Math.abs(date1.getTime() - date2.getTime());
                    long diffDays = diff / (24 * 60 * 60 * 1000);

                    if(date.equals(currentDate)){
                        dayCount = -1;
                    }else {
                        Log.e(TAG, "getNumberofDays: " + diffDays);
                        dayCount = (int) diffDays;
                        Log.e(TAG, "getNumberofDays: " + date);
                    }
                } catch (Exception e1) {
                    System.out.println("exception " + e1);
                }
            }
            return dayCount;
        }

    public int getNumberOfWeeks(String userId){
        int dayCount = 0;
        int weekCount = 0;
        int prevWeekNo = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String query = "SELECT "+COLUMN_WEEK_NO+" , SUM("+COLUMN_EXERCISED+") AS WEEK_COUNT FROM " + TABLE_EXERCISE
                    +" WHERE "+COLUMN_USER_ID+" = '"+userId+"'"
                    +" GROUP BY "+COLUMN_WEEK_NO
                    +" ORDER BY "+COLUMN_WEEK_NO+" ASC";
            Log.e("db","query = "+query);
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();

            while (!cursor.isAfterLast()) {
                int weekNo = cursor.getInt(cursor.getColumnIndex(COLUMN_WEEK_NO));
                int weekSum = cursor.getInt(cursor.getColumnIndex("WEEK_COUNT"));

                Log.e("db","weekNo = "+weekNo+" and weekSum = "+ weekSum);

                if(prevWeekNo == 0 || weekNo == prevWeekNo + 1){
                    if(weekSum >= 3){
                        weekCount++;
                    }
                    else{
                        int curWeekNo = calendar.get(Calendar.WEEK_OF_YEAR);
                        if(curWeekNo == weekNo){
                     //       weekCount++;
                        }
                        else{
                            weekCount = 0;
                        }
                    }
                }
                else{
                    if(weekSum >= 3){
                        weekCount = 1;
                    }
                    else{
                        weekCount = 0;
                    }
                }

                prevWeekNo = weekNo;
                cursor.moveToNext();

            }
            Log.e("db","weekcount = "+weekCount);

            cursor.close();
        }
        catch(Exception e){

        }
        return weekCount;
    }
}
