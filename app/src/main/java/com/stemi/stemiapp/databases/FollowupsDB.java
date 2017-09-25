package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stemi.stemiapp.utils.GlobalClass;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by praburaam on 25/09/17.
 */

public class FollowupsDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = GlobalClass.DB_VERSION;

    private static final String DATABASE_NAME = GlobalClass.DB_NAME;

    public static final String TABLE_FOLLOWUPS = "tbl_followups";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_DATE_TIME = "_datetime";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_FOLLOWUPS + "( " + COLUMN_USER_ID
            + " text not null , " + COLUMN_DATE_TIME
            + " text not null ); ";

    public FollowupsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_FOLLOWUPS);
        onCreate(sqLiteDatabase);
    }

    public void addEntry(String userId, Date eventTime){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USER_ID, userId);
            cv.put(COLUMN_DATE_TIME, simpleDateFormat.format(eventTime));
            Log.e("HospitalFragment", "Adding "+userId+" , "+simpleDateFormat.format(eventTime));
            db.insert(TABLE_FOLLOWUPS, null, cv);
        }
        catch(Exception e){

        }
        db.close();
    }

    public String getNextFollowupDate(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(new Date());
        String nextDate = null;
        try{
            String sql = "SELECT * FROM "+TABLE_FOLLOWUPS+
                    " WHERE "
                    +COLUMN_USER_ID+" = '"+userId+"' AND "
                    +COLUMN_DATE_TIME+" >= '"+today+ "' ORDER BY "+COLUMN_DATE_TIME+" LIMIT 1";
            Log.e("HospitalFragment", "sql = "+sql);

            Cursor c = db.rawQuery(sql, null);
            c.moveToFirst();

            while(!c.isAfterLast()){
                nextDate = c.getString(c.getColumnIndex(COLUMN_DATE_TIME));
                c.moveToNext();
            }
        }
        catch(Exception e){
            Log.e("HospitalFragment", "e = "+e.getLocalizedMessage());
        }
        return nextDate;
    }

    public void createIfNotExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_FOLLOWUPS;
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
}
