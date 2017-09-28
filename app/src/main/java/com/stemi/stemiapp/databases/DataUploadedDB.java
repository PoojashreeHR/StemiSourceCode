package com.stemi.stemiapp.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.stemi.stemiapp.utils.GlobalClass;

/**
 * Created by praburaam on 26/09/17.
 */

public class DataUploadedDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = GlobalClass.DB_VERSION;

    private static final String DATABASE_NAME = GlobalClass.DB_NAME;


    public static final String TABLE_UPLOAD_STATUS = "tbl_upload_status";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_UPLOADED = "uploaded";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_UPLOAD_STATUS + "( " + COLUMN_USER_ID
            + " text not null , " + COLUMN_UPLOADED
            + " integer ); ";

    public DataUploadedDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_UPLOAD_STATUS);
        onCreate(sqLiteDatabase);
    }

    public void addEntry(String userId, boolean uploaded){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_USER_ID, userId);
            contentValues.put(COLUMN_UPLOADED, uploaded);

            db.insertOrThrow(TABLE_UPLOAD_STATUS, null, contentValues);
        }
        catch(Exception e){
            Log.e("DataUploadedDB", "Exception e - "+e.getLocalizedMessage());
        }
    }

    public boolean getUploadStatus(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        boolean status = false;
        try{
            String sql = "SELECT * FROM "+TABLE_UPLOAD_STATUS+" WHERE "
                    +COLUMN_USER_ID+" = '"+userId+"'";

            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();

            while(!cursor.isAfterLast()){
                int value = cursor.getInt(cursor.getColumnIndex(COLUMN_UPLOADED));
                status = (value != 0);
                cursor.moveToNext();
            }
        }
        catch(Exception e){
            Log.e("DataUploadedDB", "Exception e - "+e.getLocalizedMessage());
        }
        return status;
    }

    public void createIfNotExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM " + TABLE_UPLOAD_STATUS;
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
